package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.common.response.Response
import jp.developer.bbee.englishmemory.data.source.remote.dto.TranslateDataDto
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class GetTranslateDataUseCase @Inject constructor(
    private val repository: TranslateRepository
) {
    open operator fun invoke(token: String): Flow<Response<List<TranslateData>>> = flow {
        try {
            emit(Response.Loading())
            val result = withContext(Dispatchers.IO) {
                repository.getTranslateData(token)
            }
            emit(Response.Success(result))
        } catch (e: Exception) {
            emit(Response.Failure(e.message.toString()))
        }
    }
}