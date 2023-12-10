package jp.developer.bbee.englishmemory.domain.repository

import jp.developer.bbee.englishmemory.domain.model.SettingPreferences
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    fun getPreferencesFlow(): Flow<Map<SettingPreferences, Boolean>>
    suspend fun setPreferences(pref: SettingPreferences, flag: Boolean)
}