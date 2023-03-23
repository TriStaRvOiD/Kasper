package com.tristarvoid.vanguard.di

import android.app.Application
import com.tristarvoid.vanguard.data.use_cases.sensor.MeasurableSensor
import com.tristarvoid.vanguard.data.use_cases.sensor.StepSensor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StepModule {
    @Provides
    @Singleton
    fun provideLightSensor(app: Application): MeasurableSensor {
        return StepSensor(app)
    }
}