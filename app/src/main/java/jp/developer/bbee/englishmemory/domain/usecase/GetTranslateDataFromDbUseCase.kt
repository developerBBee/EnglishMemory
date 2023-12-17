package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.common.response.Async
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTranslateDataFromDbUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val repository: TranslateRepository
) {
    operator fun invoke(): Flow<Async<List<TranslateData>>> = flow {
        try {
            emit(Async.Loading())
            val result = withContext(dispatcher) {
                repository.getTranslateData()
            }
            emit(Async.Success(result))
        } catch (e: Exception) {
            emit(Async.Failure(e.message.toString()))
        }
    }
}