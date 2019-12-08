package com.example.weatherlogger.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherlogger.api.ApiResponse
import com.example.weatherlogger.api.ApiService
import com.example.weatherlogger.models.Resource
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MyRepository @Inject constructor(val service: ApiService,
                                       val locationData: LocationLiveData,
                                       val gpsUtils: GpsUtils) {


    init {
    }

    fun <T>getNetworkData(serviceCall: () -> LiveData<ApiResponse<T>>): LiveData<Resource<T>> {
        return object: NetworkOnlyRepository<T, T>() {
            override fun saveLoadedData(item: T) {
            }

            override fun fetchService(): LiveData<ApiResponse<T>> {
                return serviceCall()
            }

            override fun onFetchFailed(error: Throwable?) {
                Log.d("111", "onFetchFailed : $error")
            }

        }.asLiveData()
    }

}
