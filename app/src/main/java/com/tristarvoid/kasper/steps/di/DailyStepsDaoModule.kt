/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.steps.di

import android.content.Context
import androidx.room.Room
import com.tristarvoid.kasper.steps.data.DailyStepsDao
import com.tristarvoid.kasper.steps.data.DailyStepsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DailyStepsDaoModule {

    @Provides
    @Singleton
    fun provideDailyStepsDao(@ApplicationContext context: Context): DailyStepsDao {
        val db by lazy {
            Room.databaseBuilder(
                context,
                DailyStepsDatabase::class.java,
                "daily_steps.db"
            ).build()
        }
        return db.dailyStepsDao
    }
}