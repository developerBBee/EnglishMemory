package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.domain.repository.PreferenceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class SetSavedLastDateUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val repository: PreferenceRepository
) {
    suspend operator fun invoke(date: LocalDateTime) {
        withContext(dispatcher) {
            repository.setSavedLastDate(date)
        }
    }
}