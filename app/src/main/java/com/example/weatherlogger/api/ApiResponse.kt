package com.example.weatherlogger.api

import com.google.gson.Gson
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ApiResponse<T> {
    val code: Int
    val body: T?
    private val gson: Gson
    var serverDate: Date? = null
    val error: Throwable?

    val isSuccessful: Boolean
        get() = code in 200..300

    init {
        gson = Gson()
    }

    constructor(error: Throwable) {
        code = 500
        body = null
        this.error = error
    }

    constructor(response: Response<T>) {
        code = response.code()
        try {
            response.headers().get("Date")
            val format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US)
            serverDate = format.parse(response.headers().get("Date"))
        } catch (e: Exception) {
        }

        if (response.isSuccessful) {
            body = response.body()
            this.error = null
        } else {
            var message: String? = null
            response.errorBody()?.let {
                try {
                    message = response.errorBody()!!.string()
                } catch (ignored: IOException) {
                }
            }

            message?.apply {
                if(isNullOrEmpty() || trim { it <= ' ' }.isEmpty()) {
                    message = response.message()
                }
            }
            if (message != null) {
                this.error = Exception(message!!)
            } else {
                this.error = Exception("Unknown exception")
            }
            body = null
        }

    }

}