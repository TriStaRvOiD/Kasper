package com.tristarvoid.kasper.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_value")

class ThemeDataStoreRepository(context: Context) {

    private object PreferencesKey {
        val themeValue = stringPreferencesKey("theme_value")
    }

    private val themeDataStore = context.themeDataStore

    suspend fun saveThemeValue(themeValue: String) {
        themeDataStore.edit { preferences ->
            preferences[PreferencesKey.themeValue] = themeValue
        }
    }

    fun readThemeValue(): Flow<String> {
        return themeDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val themeState = preferences[PreferencesKey.themeValue] ?: "light"
                themeState
            }
    }
}