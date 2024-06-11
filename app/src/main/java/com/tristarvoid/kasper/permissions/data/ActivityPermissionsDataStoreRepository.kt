/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.permissions.data

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

val Context.activityPermissionDataStore: DataStore<Preferences> by preferencesDataStore(name = "activity_perm")

class ActivityPermissionsDataStoreRepository(context: Context) {

    private object PreferencesKey {
        val requestValue = intPreferencesKey(name = "activity_request_count")
    }

    private val activityDataStore = context.activityPermissionDataStore

    suspend fun saveRequestCount(requestValue: Int) {
        activityDataStore.edit { preferences ->
            preferences[PreferencesKey.requestValue] = requestValue
        }
    }

    fun readRequestCount(): Flow<Int> {
        return activityDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val requestState = preferences[PreferencesKey.requestValue] ?: 0
                requestState
            }
    }
}