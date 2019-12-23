package com.example.weatherlogger.room

import androidx.room.TypeConverter
import com.example.weatherlogger.models.WeatherResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    internal var gson = Gson()

    @TypeConverter
    fun stringToWeatherResponse(data: String): WeatherResponse {
        return gson.fromJson<WeatherResponse>(data, WeatherResponse::class.java)
    }

    @TypeConverter
    fun weatherResponseToString(weatherResponse: WeatherResponse): String {
        return gson.toJson(weatherResponse)
    }

}


