package com.example.weatherlogger.api

import androidx.lifecycle.LiveData
import com.example.weatherlogger.models.WeatherResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {
    @GET("weather")
    fun getWeather(@Query("lat") lat:Double,
                   @Query("lon") lon: Double,
                   @Query("appid") appid: String,
                   @Query("units") units: String): LiveData<ApiResponse<WeatherResponse>>
}