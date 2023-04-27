/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Vanguard.
 * Vanguard is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Vanguard is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Vanguard. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.data.steps

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface StepsDao {

    @Upsert
    suspend fun upsertEntry(stepsData: StepsData)

    @Query("SELECT * FROM stepsdata WHERE id = :date")
    fun getEverything(date: Long): Flow<StepsData>

    @Query("SELECT * FROM stepsdata")
    fun getEverythingAlt(): Flow<List<StepsData>>

    @Query("SELECT AVG(currentSteps) FROM stepsdata")
    fun getAvgStepValue(): Flow<Int>

    @Query("SELECT goal - currentSteps FROM stepsdata WHERE id = :date")
    fun getRemainingStepValue(date: Long): Flow<Int>

    @Query("SELECT (SELECT COUNT(*) FROM stepsdata WHERE id = :date) == 0")
    suspend fun entryPresence(date: Long): Int
}