package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.domain.model.Recent
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateRecentUseCase @Inject constructor(
    private val repository: TranslateRepository
) {
    suspend operator fun invoke(recent: List<Recent>) = withContext(Dispatchers.IO) {
        repository.updateRecent(recent)
    }
}