package jp.developer.bbee.englishmemory.data.source.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import jp.developer.bbee.englishmemory.domain.model.SettingPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceStore @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>
) {

    fun getPreferencesFlow(): Flow<Map<SettingPreferences, Boolean>> = preferencesDataStore.data
        .map { preferences ->
            SettingPreferences.entries.associateWith { key ->
                preferences[booleanPreferencesKey(key.name)] ?: false
            }
        }

    suspend fun setPreferences(pref: SettingPreferences, flag: Boolean) {
        preferencesDataStore.edit { preferences ->
            preferences[booleanPreferencesKey(pref.name)] = flag
        }
    }
}