package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStudyDataByWordUseCase @Inject constructor(
    private val translateRepository: TranslateRepository
) {
    operator fun invoke(english: String, wordType: String): Flow<StudyData> {
        return translateRepository.getStudyDataByWord(english, wordType)
    }
}