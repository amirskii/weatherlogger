package com.example.weatherlogger.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.weatherlogger.R
import com.example.weatherlogger.factory.AppViewModelFactory
import com.example.weatherlogger.ui.main.MainViewModel
import com.example.weatherlogger.utils.ImageUtils.getArtResourceForWeatherCondition
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.details_fragment.*
import javax.inject.Inject

class DetailsFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsFragment()
    }

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private fun observeViewModel() {
        viewModel.getWeatherLocally().observe(this, Observer {
            val date = it.at.toString()
            val city_name = it.data.name
            val main = it.data.main
            val wind = it.data.wind
            val tempMax = main.temp_max.toString() + " °C"
            val tempMin = main.temp_min.toString() + " °C"
            val humidity_val = main.humidity.toString() + " %"
            val pressure_val = main.pressure.toString() + " hPa"
            val wind_speed = wind.speed.toString() + " km/h"
            val icon = getArtResourceForWeatherCondition(it.data.weather.first().id)
            val description = it.data.weather.first().description

            dDate.setText(date)
            dCity.setText(city_name)
            dHighTemp.setText(tempMax)
            dLowTemp.setText(tempMin)
            dHumidityVal.setText(humidity_val)
            dPressureVal.setText(pressure_val)
            Glide.with(context!!).load(icon).into(dIcon)
            dDescription.setText(description)

        })
    }}
