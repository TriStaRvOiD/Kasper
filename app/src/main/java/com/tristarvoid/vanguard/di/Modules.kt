package com.tristarvoid.vanguard.di

import android.widget.Toast
import com.tristarvoid.vanguard.data.use_cases.sensor.MeasurableSensor
import com.tristarvoid.vanguard.data.StepSensor
import com.tristarvoid.vanguard.domain.use_cases.step_counting.StepsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ToastProvider = module{
    single{
        Toast(androidApplication())
    }
}

val SensorProvider = module{
    single<MeasurableSensor> {
        StepSensor(androidContext())
    }
}

val ViewModelProvider = module{
    viewModel{
        StepsViewModel(get())
    }
}