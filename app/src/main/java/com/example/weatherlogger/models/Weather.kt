package com.example.weatherlogger.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/*
    As I understand we need to store only one(current) instance of weather
*/

@Entity(tableName = "weather")
data class Weather(
    val data: WeatherResponse,
    val at: Date,
    @PrimaryKey val id: Int = 0
)
