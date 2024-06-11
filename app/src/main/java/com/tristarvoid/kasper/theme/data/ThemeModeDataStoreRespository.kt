/*
 * Copyright (C) Aditya 2023-2024 <github.com/TriStaRvOiD>
 * This file is part of Kasper.
 * Kasper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Kasper is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Kasper. If not, see <https://www.gnu.org/licenses/>.
 */

package com.tristarvoid.kasper.theme.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tristarvoid.kasper.core.presentation.utils.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_mode_pref")

class ThemeModeDataStoreRepository(context: Context) {

    private object PreferencesKey {
        val themeMode = stringPreferencesKey("theme_mode")
    }

    private val themeDataStore = context.themeDataStore

    suspend fun saveThemeMode(themeMode: String) {
        var success = false
        for (mode in ThemeMode.themeModesArray) {
            if (mode.toString() == themeMode) {
                themeDataStore.edit { preferences ->
                    preferences[PreferencesKey.themeMode] = themeMode
                }
                success = true
                break
            }
        }
        if (!success) throw Exception("Invalid theme mode string")
    }

    fun readThemeMode(): Flow<String> {
        return themeDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val themeState =
                    preferences[PreferencesKey.themeMode] ?: ThemeMode.SYSTEM.toString()
                themeState
            }
    }
}