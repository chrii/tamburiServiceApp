package at.tamburi.tamburimontageservice.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "task_cache")
val HAS_ACTIVE_TASK = booleanPreferencesKey("has_active_task")
val ACTIVE_TASK_ID = intPreferencesKey("active_task")