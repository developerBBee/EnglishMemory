package jp.developer.bbee.englishmemory.testdouble

import jp.developer.bbee.englishmemory.common.response.Response
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import jp.developer.bbee.englishmemory.domain.usecase.GetTranslateDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class FakeGetTranslateDataUseCase(
    r: TranslateRepository = FakeTranslateRepository()
) : GetTranslateDataUseCase(repository = r) {
    private var testEmit: Response<List<TranslateData>> = Response.Loading()

    override fun invoke(token: String): Flow<Response<List<TranslateData>>> = flow {
        try {
            emit(testEmit)
        } catch (e: Exception) {
            emit(Response.Failure(e.message.toString()))
        }
    }

    fun setTestEmit(response: Response<List<TranslateData>>) {
        testEmit = response
    }
}