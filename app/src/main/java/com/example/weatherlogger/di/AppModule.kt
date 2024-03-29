package com.example.weatherlogger.di

import android.app.Application
import androidx.room.Room
import com.example.weatherlogger.BuildConfig
import com.example.weatherlogger.api.ApiService
import com.example.weatherlogger.api.LiveDataCallAdapterFactory
import com.example.weatherlogger.repository.GpsUtils
import com.example.weatherlogger.repository.LocationLiveData
import com.example.weatherlogger.room.AppDatabase
import com.example.weatherlogger.room.WeatherDao
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {
    private val DEFAULT_TIME_OUT = 120L
    private val apiURL = "http://api.openweathermap.org/data/2.5/"


    @Provides
    @Singleton
    internal fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
        return interceptor
    }

    @Provides
    @Singleton
    @Named("ApiService")
    fun provideRetrofit(interceptor: HttpLoggingInterceptor): Retrofit {
        val client = OkHttpClient().newBuilder()
                .addInterceptor(interceptor)
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .build()

        return Retrofit.Builder()
                .baseUrl(apiURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .client(client)
                .build()
    }

    @Provides
    @Singleton
    fun provideApiService(@Named("ApiService") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationData(application: Application) = LocationLiveData(application)

    @Provides
    @Singleton
    fun provideGpsUtils(application: Application) = GpsUtils(application)

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application.applicationContext, AppDatabase::class.java,"main.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(database: AppDatabase): WeatherDao {
        return database.weatherDao()
    }

}