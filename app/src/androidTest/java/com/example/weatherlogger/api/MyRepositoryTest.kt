/*
 * The MIT License (MIT)
 *
 * Designed and developed by 2018 skydoves (Jaewoong Eum)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.example.weatherlogger.api

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.weatherlogger.api.ApiResponseUtil.successCall
import com.example.weatherlogger.models.Resource
import com.example.weatherlogger.models.Weather
import com.example.weatherlogger.models.WeatherResponse
import com.example.weatherlogger.repository.GpsUtils
import com.example.weatherlogger.repository.LocationLiveData
import com.example.weatherlogger.repository.MyRepository
import com.example.weatherlogger.room.WeatherDao
import com.example.weatherlogger.utils.MockTestUtil
import com.nhaarman.mockitokotlin2.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*


@RunWith(JUnit4::class)
class MyRepositoryTest {
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val APP_ID = "TES_APP_ID"
    val UNITS = "metric"
    val lat = 43.2220
    val lon = 76.8512

    private lateinit var repository: MyRepository
    private val weatherDao = mock<WeatherDao>()
    private val service = mock<ApiService>()
    private val locationData = mock<LocationLiveData>()
    private val gpsUtils = mock<GpsUtils>()

    @Before
    fun init() {
        val context = mock<Context>()
        whenever(context.applicationContext).thenReturn(context)

        repository = MyRepository(service, locationData, gpsUtils, weatherDao)

        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
            override fun isMainThread() = true
            override fun postToMainThread(runnable: Runnable) = runnable.run()
        })
    }

    @After
    fun terminate() {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    @Test
    fun loadWeatherFromNetwork() {
        val loadFromDB = MutableLiveData<Weather>()
        whenever(weatherDao.selectWeather()).thenReturn(loadFromDB)

        val mockResponse = MockTestUtil.mockWeatherResponse()
        val call = successCall(mockResponse)
        whenever(service.getWeather(any(), any(), any(), any())).thenReturn(call)

        val data = repository.getWeather(lat, lon, MockTestUtil.mockTime())
        verify(service).getWeather(any(), any(), any(), any())
        val observer = mock<Observer<Resource<WeatherResponse>>>()
        data.observeForever(observer)
        verify(observer).onChanged(Resource.success(mockResponse))
        verify(weatherDao).insertWeather(any())
        verifyNoMoreInteractions(service)

        val updateData = MutableLiveData<Weather>().apply { value = Weather(0, Date()) }
        whenever(weatherDao.selectWeather()).thenReturn(updateData)
        val data2 = repository.loadWeatherLocally()
        val observer2 = mock<Observer<Weather>>()
        data2.observeForever(observer2)
        verifyNoMoreInteractions(service)
        verify(observer2).onChanged(any())
    }
}
