package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveTranslateDataUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val repository: TranslateRepository
) {
    suspend operator fun invoke(data: List<TranslateData>) {
        withContext(dispatcher) {
            repository.saveTranslateData(data)
        }
    }
}