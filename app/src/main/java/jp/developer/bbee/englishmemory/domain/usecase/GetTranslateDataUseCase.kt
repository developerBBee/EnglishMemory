package jp.developer.bbee.englishmemory.domain.usecase

import jp.developer.bbee.englishmemory.common.response.NetworkResponse
import jp.developer.bbee.englishmemory.data.source.remote.dto.TranslateDataDto
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class GetTranslateDataUseCase(
    private val repository: TranslateRepository
) {
    operator fun invoke(): Flow<NetworkResponse<TranslateDataDto>> = flow {
        try {
            emit(NetworkResponse.Loading())
            val result = withContext(Dispatchers.IO) {
                repository.getTranslateData()
            }
            emit(NetworkResponse.Success(result))
        } catch (e: Exception) {
            emit(NetworkResponse.Failure(e.message.toString()))
        }
    }
}