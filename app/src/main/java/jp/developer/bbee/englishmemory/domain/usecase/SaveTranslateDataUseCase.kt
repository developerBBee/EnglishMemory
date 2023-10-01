package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class SaveTranslateDataUseCase @Inject constructor(
    private val repository: TranslateRepository
) {
    open suspend operator fun invoke(data: List<TranslateData>) {
        withContext(Dispatchers.IO) {
            repository.saveTranslateData(data)
        }
    }
}