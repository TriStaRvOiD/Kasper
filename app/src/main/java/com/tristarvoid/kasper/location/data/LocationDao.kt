/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.location.data

import androidx.room.Dao
import androidx.room.Query
import com.tristarvoid.kasper.core.presentation.utils.Region
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("INSERT INTO LocationData (latitude, longitude, epochDay) VALUES (:latitude, :longitude, :date)")
    suspend fun insertEntry(latitude: Double, longitude: Double, date: Long)

    @Query("SELECT latitude, longitude FROM LocationData WHERE epochDay = :date")
    fun getRegionList(date: Long): Flow<List<Region>>

}