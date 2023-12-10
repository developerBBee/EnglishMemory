package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.domain.model.SettingPreferences
import jp.developer.bbee.englishmemory.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPreferenceUseCase @Inject constructor(
    private val repository: PreferenceRepository
) {

    operator fun invoke(): Flow<Map<SettingPreferences, Boolean>> = repository.getPreferencesFlow()
}