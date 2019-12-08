package com.example.weatherlogger.models

import java.util.*

/**
 * Developed by skydoves on 2018-03-12.
 * Copyright (c) 2018 skydoves rights reserved.
 */

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
class Resource<out T>(val status: Status, val data: T?, val message: String?, val fetchStatus: FetchStatus, val error: Throwable?=null, val serverTime: Date?=null) {

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }

        val resource = o as Resource<*>?

        if (status !== resource!!.status) {
            return false
        }
        if (if (message != null) message != resource!!.message else resource!!.message != null) {
            return false
        }
        return if (data != null) data == resource.data else resource.data == null
    }

    override fun toString(): String {
        return "Resource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}'
    }

    companion object {
        private var fetchStatus = FetchStatus()

        fun <T> success(data: T?, serverTime: Date?=null): Resource<T> {
            fetchStatus = FetchStatus(false, true, false, false)
            return Resource(Status.SUCCESS, data, null, fetchStatus, null, serverTime)
        }

        fun <T> error(msg: String, data: T?, error: Throwable?): Resource<T> {
            fetchStatus = FetchStatus(false, false, true, true)
            return Resource(Status.ERROR, data, msg, fetchStatus, error=error)
        }

        fun <T> loading(data: T?, nextPage: Int?): Resource<T> {
            fetchStatus = FetchStatus(true, false, false, false)
            return Resource(Status.LOADING, data, null, fetchStatus)
        }
    }


    fun setOnLast(): FetchStatus {
        return FetchStatus(false, true, false, false)
    }
}
