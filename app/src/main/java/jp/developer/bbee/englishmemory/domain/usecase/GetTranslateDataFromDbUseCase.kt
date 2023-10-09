package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.common.response.Async
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class GetTranslateDataFromDbUseCase @Inject constructor(
    private val repository: TranslateRepository
) {
    open operator fun invoke(): Flow<Async<List<TranslateData>>> = flow {
        try {
            emit(Async.Loading())
            val result = withContext(Dispatchers.IO) {
                repository.getTranslateData()
            }
            emit(Async.Success(result))
        } catch (e: Exception) {
            emit(Async.Failure(e.message.toString()))
        }
    }
}