package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStudyDataUseCase @Inject constructor(
    private val repository: TranslateRepository
) {
    operator fun invoke(): Flow<List<StudyData>> = repository.getStudyData()

}