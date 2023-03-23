package com.tristarvoid.vanguard.di

import android.content.Context
import android.widget.Toast
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ToastModule {
    @Provides
    @Singleton
    fun provideToast(
        @ApplicationContext context: Context
    ) = Toast(context)
}