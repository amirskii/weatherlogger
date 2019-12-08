package com.example.weatherlogger.ui.main

import androidx.lifecycle.ViewModel
import com.example.weatherlogger.repository.MyRepository
import javax.inject.Inject

class MainViewModel @Inject
constructor(private val repository: MyRepository): ViewModel() {
    fun getLocationData() = repository.locationData
}
