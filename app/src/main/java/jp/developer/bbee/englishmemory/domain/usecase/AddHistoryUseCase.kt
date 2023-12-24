package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.domain.model.History
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddHistoryUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val repository: TranslateRepository
) {
    suspend operator fun invoke(history: History) {
        withContext(dispatcher) {
            repository.updateHistory(history)
        }
    }
}