package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import javax.inject.Inject

class GetRecentUseCase @Inject constructor(
    private val repository: TranslateRepository
) {
    operator fun invoke() = repository.getRecent()
}