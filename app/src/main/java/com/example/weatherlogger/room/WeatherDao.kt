package com.example.weatherlogger.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherlogger.models.Weather


@Dao
interface WeatherDao {
    @Query("SELECT* FROM weather")
    fun selectWeather(): LiveData<Weather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weather: Weather)
}
