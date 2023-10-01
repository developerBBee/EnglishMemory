package jp.developer.bbee.englishmemory.testdouble

import jp.developer.bbee.englishmemory.common.response.Response
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.usecase.GetTranslateDataFromDbUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeGetTranslateDataFromDbUseCase(
    r: FakeTranslateRepository = FakeTranslateRepository()
) : GetTranslateDataFromDbUseCase(repository = r) {
    private var testEmit: Response<List<TranslateData>> = Response.Loading()

    override fun invoke(): Flow<Response<List<TranslateData>>> = flow {
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