package jp.developer.bbee.englishmemory.data.repository

import jp.developer.bbee.englishmemory.data.source.datastore.PreferenceStore
import jp.developer.bbee.englishmemory.domain.model.SettingPreferences
import jp.developer.bbee.englishmemory.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferenceRepositoryImpl @Inject constructor(
    private val store: PreferenceStore
) : PreferenceRepository {

    override fun getPreferencesFlow(): Flow<Map<SettingPreferences, Boolean>> =
        store.getPreferencesFlow()

    override suspend fun setPreferences(pref: SettingPreferences, flag: Boolean) {
        store.setPreferences(pref, flag)
    }
}