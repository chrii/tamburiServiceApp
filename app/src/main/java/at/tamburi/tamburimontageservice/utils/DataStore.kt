package at.tamburi.tamburimontageservice.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "task_cache")

object DataStoreConstants {
    val HAS_ACTIVE_TASK = booleanPreferencesKey("has_active_task")
    val ACTIVE_TASK_ID = intPreferencesKey("active_task")
    val ACTIVE_USER_ID = intPreferencesKey("active_user_id")
    val SESSION_TOKEN = stringPreferencesKey("session_token")
    val WORKFLOW_STATE = stringPreferencesKey("workflow_state")

    suspend fun getToken(context: Context): String {
        return context.dataStore.data.map {
            it[DataStoreConstants.SESSION_TOKEN] ?: throw Exception("Token not found")
        }.first()
    }
}
