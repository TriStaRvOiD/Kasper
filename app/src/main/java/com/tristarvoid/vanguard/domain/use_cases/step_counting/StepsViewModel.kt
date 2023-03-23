package com.tristarvoid.vanguard.domain.use_cases.step_counting

import androidx.lifecycle.ViewModel
import com.tristarvoid.vanguard.data.use_cases.sensor.MeasurableSensor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StepsViewModel(
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