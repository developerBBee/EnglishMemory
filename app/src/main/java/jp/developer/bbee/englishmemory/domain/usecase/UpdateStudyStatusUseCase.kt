package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.domain.model.StudyStatus
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateStudyStatusUseCase @Inject constructor(
    private val repository: TranslateRepository
) {
    suspend operator fun invoke(studyStatus: StudyStatus) = withContext(Dispatchers.IO) {
        repository.updateStudyStatus(studyStatus)
    }
}