/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.data.repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.goalDataStore: DataStore<Preferences> by preferencesDataStore(name = "goal_value")

class GoalDataStoreRepository(context: Context) {

    private object PreferencesKey {
        val goalValue = intPreferencesKey(name = "goal_value")
    }

    private val goalDataStore = context.goalDataStore

    suspend fun saveGoalValue(goalValue: Int) {
        goalDataStore.edit { preferences ->
            preferences[PreferencesKey.goalValue] = goalValue
        }
    }

    fun readGoalValue(): Flow<Int> {
        return goalDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val goalState = preferences[PreferencesKey.goalValue] ?: 0
                goalState
            }
    }
}