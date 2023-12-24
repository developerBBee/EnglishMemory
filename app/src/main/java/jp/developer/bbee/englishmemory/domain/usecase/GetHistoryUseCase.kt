package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.domain.model.History
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val translateRepository: TranslateRepository,
) {
    operator fun invoke(dateTimeFrom: String): Flow<List<History>> {
        return translateRepository.getHistory(dateTimeFrom)
    }
}