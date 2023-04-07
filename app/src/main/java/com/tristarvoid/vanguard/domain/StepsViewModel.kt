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

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tristarvoid.vanguard.data.repo.StatusDataStoreRepository
import com.tristarvoid.vanguard.data.sensor.MeasurableSensor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StepsViewModel @Inject constructor(
    stepSensor: MeasurableSensor,
    private val repository: StatusDataStoreRepository
) : ViewModel() {

    private val _steps = MutableStateFlow("")
    val steps = _steps.asStateFlow()

    private val sensor = stepSensor
    val isActive = mutableStateOf(false)

    init {
        updateStatus()
    }

    private fun updateStatus() {
        viewModelScope.launch {
            repository.readListeningState().collect { status ->
                isActive.value = status
            }
        }
    }

    fun start() {
        sensor.startListening()
        sensor.setOnSensorValuesChangedListener { values ->
            _steps.value = values[0].toInt().toString()
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveListeningState(true)
        }
        updateStatus()
    }

    fun stop() {
        sensor.stopListening()
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveListeningState(false)
        }
        updateStatus()
    }
}