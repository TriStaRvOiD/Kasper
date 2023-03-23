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