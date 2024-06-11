/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.weather.presentation.view_models

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.Priority
import com.tristarvoid.kasper.network.data.ConnectivityObserver
import com.tristarvoid.kasper.network.data.NetworkConnectivityObserver
import com.tristarvoid.kasper.location.data.LocationClient
import com.tristarvoid.kasper.quality.data.retrofit.QualityApi
import com.tristarvoid.kasper.weather.data.retrofit.WeatherApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    locationClient: LocationClient,
    networkConnectivityObserver: NetworkConnectivityObserver,
    private val weatherApi: WeatherApi,
    private val qualityApi: QualityApi
) : ViewModel() {

    private val networkStatus = networkConnectivityObserver.observe()

    val temp = mutableStateOf("")
    val desc = mutableStateOf("")
    val quality = mutableStateOf("")

    init {
        initializeWeatherUpdates(locationClient)
    }

    private fun initializeWeatherUpdates(locationClient: LocationClient) {
        locationClient
            .getLocationUpdates(300000L, Priority.PRIORITY_LOW_POWER)
            .catch { e ->
                e.printStackTrace()
            }
            .onEach { location ->
                val lat = location.latitude
                val long = location.longitude
                getWeather(lat = lat, long = long)
                getQuality(lat = lat, long = long)
            }
            .launchIn(viewModelScope)
    }

    private fun getWeather(
        lat: Double,
        long: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            networkStatus.collect { status ->
                if (status == ConnectivityObserver.Status.Available) {
                    try {
                        val response = weatherApi.getWeather(
                            lat,
                            long
                        )
                        if (response.isSuccessful && response.body() != null) {
                            val body = response.body()!!
                            temp.value = body.main.temp.toInt().toString()
                            desc.value = body.weather[0].description
                        }
                    } catch (e: IOException) {
                        Log.d("Weather", e.message.toString())
                        return@collect
                    }
                }
            }
        }
    }

    private fun getQuality(
        lat: Double,
        long: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            networkStatus.collect { status ->
                if (status == ConnectivityObserver.Status.Available) {
                    try {
                        val response = qualityApi.getQuality(
                            lat,
                            long
                        )
                        if (response.isSuccessful && response.body() != null) {
                            when (response.body()!!.list[0].main.aqi) {
                                1 -> {
                                    quality.value = "good"
                                }

                                2 -> {
                                    quality.value = "fair"
                                }

                                3 -> {
                                    quality.value = "moderate"
                                }

                                4 -> {
                                    quality.value = "poor"
                                }

                                5 -> {
                                    quality.value = "very poor"
                                }
                            }
                        }
                    } catch (e: IOException) {
                        Log.d("Weather", e.message.toString())
                        return@collect
                    }
                }
            }
        }
    }
}