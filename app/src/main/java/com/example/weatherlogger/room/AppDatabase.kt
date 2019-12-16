package com.example.weatherlogger.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherlogger.models.Weather

@Database(entities = [(Weather::class)], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
