package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.common.response.Async
import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetStudyDataUseCase @Inject constructor(
    private val repository: TranslateRepository
) {
    operator fun invoke(): Flow<Async<List<StudyData>>> = flow {
        try {
            emit(Async.Loading())
            val result = withContext(Dispatchers.IO) {
                repository.getStudyData()
            }
            emit(Async.Success(result))
        } catch (e: Exception) {
            emit(Async.Failure(e.message.toString()))
        }
    }
}