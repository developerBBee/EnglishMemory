package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.common.response.Async
import jp.developer.bbee.englishmemory.domain.model.BookmarkPreferences
import jp.developer.bbee.englishmemory.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveBookmarkPreferenceUseCase @Inject constructor(
    private val repository: PreferenceRepository
) {
    operator fun invoke(prefs: BookmarkPreferences): Flow<Async<Unit>> = flow {
        emit(Async.Loading())

        runCatching {
            repository.setBookmarkPreferences(prefs)
            emit(Async.Success(data = Unit))
        }.onFailure {
            emit(Async.Failure(error = it.message ?: "error"))
        }
    }
}