/*
 * Copyright (C) Aditya 2023 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.statusDataStore: DataStore<Preferences> by preferencesDataStore(name = "status_pref")

class StatusDataStoreRepository(context: Context) {

    private object PreferencesKey {
        val statusKey = booleanPreferencesKey(name = "status_of_listener")
    }

    private val statusDataStore = context.statusDataStore

    suspend fun saveListeningState(value: Boolean) {
        statusDataStore.edit { preferences ->
            preferences[PreferencesKey.statusKey] = value
        }
    }

    fun readListeningState(): Flow<Boolean> {
        return statusDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val statusState = preferences[PreferencesKey.statusKey] ?: false
                statusState
            }
    }
}