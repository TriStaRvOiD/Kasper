/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.di

import android.content.Context
import androidx.room.Room
import com.tristarvoid.kasper.data.steps.StepsDao
import com.tristarvoid.kasper.data.steps.StepsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StepDaoModule {

    @Provides
    @Singleton
    fun provideStepsDao(@ApplicationContext context: Context): StepsDao {
        val db by lazy {
            Room.databaseBuilder(
                context,
                StepsDatabase::class.java,
                "steps.db"
            ).build()
        }
        return db.stepsDao
    }
}