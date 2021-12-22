package com.modelschool.algebra.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.modelschool.algebra.data.model.Student
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("app_prefs")
private val id = stringPreferencesKey("id")
private val name = stringPreferencesKey("name")
private val grade = stringPreferencesKey("grade")
private val classNo = intPreferencesKey("class_num")
private val level = intPreferencesKey("level")

class AppPrefs(appContext: Context) {

    private val store = appContext.dataStore

    suspend fun setValue(value: Student?) {
        store.edit { prefs ->
            if (value != null) {
                prefs[id] = value.id
                prefs[name] = value.name
                prefs[grade] = value.grade
                prefs[classNo] = value.classNo
                prefs[level] = value.level
            }
        }
    }

    suspend fun getValue(): Student? {
        return store.data.map { prefs ->
            if(prefs[id] != null) {
                Student(
                    id = prefs[id]!!,
                    name = prefs[name]!!,
                    classNo = prefs[classNo]!!,
                    grade = prefs[grade]!!,
                    level = prefs[level]!!
                )
            }else{
                null
            }
        }.first()
    }
}