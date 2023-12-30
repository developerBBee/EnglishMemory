package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.domain.repository.PreferenceRepository
import javax.inject.Inject

class GetBookmarkPreferenceUseCase @Inject constructor(
    private val repository: PreferenceRepository
) {
    operator fun invoke() = repository.getBookmarkPreferencesFlow()
}