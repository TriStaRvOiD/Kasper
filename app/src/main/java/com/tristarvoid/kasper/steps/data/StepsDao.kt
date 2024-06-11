/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.steps.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StepsDao {
    @Query("INSERT INTO StepsData (stepNanos, epochDay) VALUES (:stepNanos, :date)")
    fun insertRawEntry(stepNanos: Long, date: Long)

    @Query("SELECT COUNT(*) FROM StepsData WHERE epochDay = :date")
    fun getStepCountForDay(date: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM StepsData")
    suspend fun getTotalStepCount(): Int

    @Query("SELECT * FROM StepsData")
    fun getAllData(): Flow<List<StepsData>>

    @Query("SELECT * FROM StepsData WHERE epochDay = :date")
    fun getAllDataForDate(date: Long): Flow<StepsData>

    @Query("SELECT COUNT(stepNanos) / COUNT(DISTINCT epochDay) FROM StepsData")
    fun getAverageStepCount(): Flow<Int>
}