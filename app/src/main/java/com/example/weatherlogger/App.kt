package com.example.weatherlogger

import android.content.Context
import com.example.weatherlogger.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {

    private val appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

    override fun onCreate() {
        super.onCreate()

        App.instance = this

        appComponent.inject(this)

    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return appComponent
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    companion object {

        private lateinit var instance: App

    }

}
