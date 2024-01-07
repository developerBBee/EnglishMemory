package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class GetSavedLastDateUseCase @Inject constructor(
    private val repository: PreferenceRepository
) {
    operator fun invoke(): Flow<LocalDateTime?> = repository.getSavedLastDateFlow()
}