package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.common.response.Async
import jp.developer.bbee.englishmemory.domain.model.SettingPreferences
import jp.developer.bbee.englishmemory.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SavePreferenceUseCase @Inject constructor(
    private val repository: PreferenceRepository
) {

    operator fun invoke(pref: SettingPreferences, flag: Boolean): Flow<Async<Unit>> = flow {
        emit(Async.Loading())

        runCatching {
            repository.setPreferences(pref, flag)
        }.onFailure {
            emit(Async.Failure(error = it.message ?: "error"))
        }
    }
}