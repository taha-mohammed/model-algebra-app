package com.modelschool.algebra.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.modelschool.algebra.data.model.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("app_prefs")
private val id = stringPreferencesKey("id")
private val name = stringPreferencesKey("name")
private val classNo = intPreferencesKey("class_num")
private val level = intPreferencesKey("id")

class AppPrefs(appContext: Context) {

    private val store = appContext.dataStore

    suspend fun setValue(value: UserPreferences?) {
        store.edit { prefs ->
            if (value != null) {
                prefs[id] = value.id
                prefs[name] = value.name
                prefs[classNo] = value.classNo
                prefs[level] = value.level
            }
        }
    }

    suspend fun getValue(): UserPreferences? {
        return store.data.map { prefs ->
            prefs[id]?.let {
                UserPreferences(
                    it,
                    prefs[name]!!,
                    prefs[classNo]!!,
                    prefs[level]!!
                )
            }
        }.first()
    }
}