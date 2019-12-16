package com.example.weatherlogger.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherlogger.models.Weather


/**
 * Developed by skydoves on 2018-01-27.
 * Copyright (c) 2018 skydoves rights reserved.
 */

@Dao
interface WeatherDao {
    @Query("SELECT* FROM weather")
    fun selectWeather(): LiveData<Weather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weather: Weather)
}
