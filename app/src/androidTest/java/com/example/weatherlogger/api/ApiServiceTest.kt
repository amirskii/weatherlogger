package com.example.weatherlogger.api

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.example.weatherlogger.utils.LiveDataTestUtil
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.Okio
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * Created by skydoves on 2018. 3. 13.
 * Copyright (c) 2018 skydoves All rights reserved.
 */

@RunWith(JUnit4::class)
class ApiServiceTest {
    private lateinit var service: ApiService
    private lateinit var mockWebServer: MockWebServer
    val APP_ID = "TES_APP_ID"
    val UNITS = "metric"

    @Throws(IOException::class)
    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(ApiService::class.java)

        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
            override fun isMainThread() = true
            override fun postToMainThread(runnable: Runnable) = runnable.run()
        })
    }

    @Throws(IOException::class)
    @After
    fun stopService() {
        mockWebServer.shutdown()
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    @Test
    fun getWeatherTest() {
        enqueueResponse("weather.json")
        val currentWeather =
            LiveDataTestUtil.getValue(service.getWeather(43.2220, 76.8512, APP_ID, UNITS)).body

        assertThat(currentWeather, notNullValue())
        assertThat(currentWeather?.main?.temp, `is`(-6))
        assertThat(currentWeather?.coord?.lat, `is`(43.22))
        assertThat(currentWeather?.coord?.lon, `is`(76.85))
    }

    @Throws(IOException::class)
    private fun enqueueResponse(fileName: String) {
        enqueueResponse(fileName, emptyMap())
    }

    @Throws(IOException::class)
    private fun enqueueResponse(fileName: String, headers: Map<String, String>) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream("api-response/$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(mockResponse.setBody(source.readString(StandardCharsets.UTF_8)))
    }
}
