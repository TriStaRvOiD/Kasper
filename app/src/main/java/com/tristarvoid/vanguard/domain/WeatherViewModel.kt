/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Vanguard.
 * Vanguard is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Vanguard is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Vanguard. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.vanguard.domain

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tristarvoid.vanguard.data.weather.WeatherApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherApi: WeatherApi
) : ViewModel() {

    val temp = mutableStateOf("")
    val desc = mutableStateOf("")

    fun getWeather(
        lat: Double,
        long: Double
    ) {
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(
                    lat,
                    long
                )
                Log.d("Retrofit Weather response", response.body().toString())
                if (response.isSuccessful && response.body() != null) {
                    temp.value = response.body()!!.main.temp.toString()
                    desc.value = response.body()!!.weather[0].description
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