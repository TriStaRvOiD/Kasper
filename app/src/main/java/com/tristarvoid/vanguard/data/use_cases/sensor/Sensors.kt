package com.tristarvoid.vanguard.data.use_cases.sensor

import android.content.Context
import android.hardware.Sensor

import android.content.pm.PackageManager
import com.tristarvoid.vanguard.data.use_cases.sensor.AndroidSensor

class StepSensor(
    context: Context
) : AndroidSensor(
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_STEP_COUNTER,
    sensorType = Sensor.TYPE_STEP_COUNTER
)