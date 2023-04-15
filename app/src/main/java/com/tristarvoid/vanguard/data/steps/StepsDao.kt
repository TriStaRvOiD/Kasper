/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Vanguard.
 * Vanguard is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Vanguard is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Vanguard. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.vanguard.data.steps

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface StepsDao {

    @Upsert
    suspend fun upsertEntry(steps: Steps)

    @Query("SELECT currentSteps FROM steps WHERE id = :date")
    suspend fun getStepValues(date: Long): Int

    @Query("SELECT goal FROM steps WHERE id = :date")
    suspend fun getGoalValue(date: Long): Int

    @Query("SELECT calories FROM steps WHERE id = :date")
    suspend fun getCalorieValue(date: Long): Int

    @Query("SELECT (SELECT COUNT(*) FROM steps) == 0")
    suspend fun entryPresence(): Int
}