package jp.developer.bbee.englishmemory.testdouble

import jp.developer.bbee.englishmemory.common.response.Async
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.usecase.GetTranslateDataFromDbUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeGetTranslateDataFromDbUseCase(
    r: FakeTranslateRepository = FakeTranslateRepository()
) : GetTranslateDataFromDbUseCase(repository = r) {
    private var testEmit: Async<List<TranslateData>> = Async.Loading()

    override fun invoke(): Flow<Async<List<TranslateData>>> = flow {
        try {
            emit(testEmit)
        } catch (e: Exception) {
            emit(Async.Failure(e.message.toString()))
        }
    }

    fun setTestEmit(async: Async<List<TranslateData>>) {
        testEmit = async
    }
}