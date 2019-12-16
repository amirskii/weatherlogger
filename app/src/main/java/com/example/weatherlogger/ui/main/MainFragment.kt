package com.example.weatherlogger.ui.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherlogger.R
import com.example.weatherlogger.factory.AppViewModelFactory
import com.example.weatherlogger.models.Status
import com.example.weatherlogger.models.Temperature
import com.example.weatherlogger.repository.GPS_REQUEST
import com.example.weatherlogger.repository.LOCATION_REQUEST
import com.example.weatherlogger.ui.WeatherAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*
import javax.inject.Inject

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }
    private val adapter by lazy { WeatherAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        temperatureRv.layoutManager = LinearLayoutManager(context)
        temperatureRv.adapter = adapter
        observeViewModel()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private fun observeViewModel() {
        viewModel.weatherAt.observe(this, Observer {
            it?.let { resource ->
                when(resource.status) {
                    Status.SUCCESS -> {
                        it.data?.let {
                            Log.d("111", "" + it)
                            adapter.add(Temperature(it.main.temp, resource.serverTime ?: Date()))
                        }
                    }
                    Status.ERROR -> {}
                    Status.LOADING -> {}
                }

            }
        })

    }

    private fun startLocationUpdate() {
        viewModel.getLocationData().observe(this, Observer {
            latLongTv.text =  getString(R.string.latLong, it.longitude, it.latitude)
        })
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                viewModel.isGPSEnabled = true
                invokeLocationAction()
            }
        }
    }

    private fun invokeLocationAction() {
        when {
            !viewModel.isGPSEnabled -> latLongTv.text = getString(R.string.enable_gps)
            isPermissionsGranted() -> startLocationUpdate()
            shouldShowRequestPermissionRationale() -> latLongTv.text = getString(R.string.permission_request)
            else -> ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST
            )
        }
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }
}
