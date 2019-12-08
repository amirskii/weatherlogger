package com.example.weatherlogger.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.weatherlogger.api.ApiResponse
import com.example.weatherlogger.models.Resource


abstract class NetworkOnlyRepository<ResultType, RequestType>
internal constructor() {

    private val result: MediatorLiveData<Resource<ResultType>> = MediatorLiveData()

    init {
        result.postValue(Resource.loading(null, null))
        val apiResponse = fetchService()
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when(response?.isSuccessful) {
                true -> {
                    saveLoadedData(response.body as ResultType)
                    setValue(Resource.success(response.body as ResultType?, response.serverDate))
                }
                false -> {
                    onFetchFailed(response.error)
                    setValue(Resource.error(response.error.toString(), null, response.error))
                }
            }
        }

    }

    @WorkerThread
    protected abstract fun saveLoadedData(item: ResultType)

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        result.value = newValue
    }

    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }

    @MainThread
    protected abstract fun fetchService(): LiveData<ApiResponse<RequestType>>

    @MainThread
    protected abstract fun onFetchFailed(error: Throwable?)
}
