/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
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

    @Query("SELECT EXISTS(SELECT * FROM stepsdata WHERE ID = :date)")
    fun isEntryPresent(date: Long): Boolean

    @Query("UPDATE stepsdata SET stepCount = stepCount + 1 WHERE id = :date")
    fun incrementStepCount(date: Long)

    @Query("SELECT * FROM stepsdata WHERE EXISTS(SELECT * FROM stepsdata)")
    fun getEverything(): Flow<List<StepsData>>

    @Query("SELECT * FROM stepsdata WHERE EXISTS(SELECT * FROM stepsdata) AND id = :date")
    fun getEverything(date: Long): Flow<StepsData>

    @Query("SELECT AVG(stepCount) FROM stepsdata WHERE EXISTS(SELECT * FROM stepsdata)")
    fun getAverageStepValue(): Flow<Int>
}