package com.tristarvoid.vanguard.di

import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.tristarvoid.vanguard.data.repo.DataStoreRepository
import com.tristarvoid.vanguard.data.use_cases.sensor.MeasurableSensor
import com.tristarvoid.vanguard.data.use_cases.sensor.StepSensor
import com.tristarvoid.vanguard.domain.use_cases.OnboardViewModel
import com.tristarvoid.vanguard.domain.use_cases.SplashScreenViewModel
import com.tristarvoid.vanguard.domain.use_cases.StepsViewModel
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

val DataStoreProvider = module {
    single {
        DataStoreRepository(androidApplication())
    }
}

val ViewModelResolver = module{
    viewModel{
        StepsViewModel(get())
    }

    viewModel{
        OnboardViewModel(get())
    }

    viewModel{
        SplashScreenViewModel(get())
    }
}