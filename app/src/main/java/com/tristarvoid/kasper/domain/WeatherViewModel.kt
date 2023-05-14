/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.domain

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tristarvoid.kasper.data.retrofit.pollution.QualityApi
import com.tristarvoid.kasper.data.retrofit.weather.WeatherApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherApi: WeatherApi,
    private val qualityApi: QualityApi
) : ViewModel() {

    val temp = mutableStateOf("")
    val desc = mutableStateOf("")
    val quality = mutableStateOf("")

    fun getWeather(
        lat: Double,
        long: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
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
                Log.d("Weather", "Internet")
                return@launch
            } catch (e: HttpException) {
                Log.d("Weather", "Internet")
                return@launch
            }
        }
    }

    fun getQuality(
        lat: Double,
        long: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = qualityApi.getQuality(
                    lat,
                    long
                )
                if (response.isSuccessful && response.body() != null) {
                    when (response.body()!!.list[0].main.aqi) {
                        1 -> {quality.value = "good"}
                        2 -> {quality.value = "fair"}
                        3 -> {quality.value = "moderate"}
                        4 -> {quality.value = "poor"}
                        5 -> {quality.value = "very poor"}
                    }
                }
            } catch (e: IOException) {
                Log.d("Weather", "Internet")
                return@launch
            } catch (e: HttpException) {
                Log.d("Weather", "Internet")
                return@launch
            }
        }
    }
}