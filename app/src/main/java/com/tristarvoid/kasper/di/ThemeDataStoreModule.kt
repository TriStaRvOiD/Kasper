package com.tristarvoid.kasper.di

import android.content.Context
import com.tristarvoid.kasper.data.datastore.ThemeDataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ThemeDataStoreModule {

    @Provides
    @Singleton
    fun provideThemeRepository(
        @ApplicationContext context: Context
    ) = ThemeDataStoreRepository(context = context)
}