package jp.developer.bbee.englishmemory.domain.usecase

import androidx.datastore.preferences.core.Preferences
import jp.developer.bbee.englishmemory.common.response.Async
import jp.developer.bbee.englishmemory.domain.model.SettingPreferences
import jp.developer.bbee.englishmemory.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SavePreferenceUseCase @Inject constructor(
    private val repository: PreferenceRepository
) {

    operator fun invoke(pref: SettingPreferences, flag: Boolean): Flow<Async<Preferences>> = flow {
        emit(Async.Loading())

        runCatching {
            repository.setPreferences(pref, flag)
        }.onFailure {
            emit(Async.Failure(error = it.message ?: "error"))
        }
    }
}