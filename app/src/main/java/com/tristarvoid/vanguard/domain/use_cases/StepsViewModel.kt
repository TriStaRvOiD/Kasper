/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Vanguard.
 * Vanguard is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Vanguard is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Vanguard. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.vanguard.domain.use_cases

import androidx.lifecycle.ViewModel
import com.tristarvoid.vanguard.data.use_cases.sensor.MeasurableSensor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StepsViewModel @Inject constructor(
    stepSensor: MeasurableSensor
) : ViewModel() {
    private val _steps = MutableStateFlow("")
    val steps = _steps.asStateFlow()

    init {
        stepSensor.startListening()
        stepSensor.setOnSensorValuesChangedListener { values ->
            _steps.value = values[0].toInt().toString()
        }
    }
}