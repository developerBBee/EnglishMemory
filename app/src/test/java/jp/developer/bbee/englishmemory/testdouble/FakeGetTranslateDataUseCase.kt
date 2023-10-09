package jp.developer.bbee.englishmemory.testdouble

import jp.developer.bbee.englishmemory.common.response.Async
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import jp.developer.bbee.englishmemory.domain.usecase.GetTranslateDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeGetTranslateDataUseCase(
    r: TranslateRepository = FakeTranslateRepository()
) : GetTranslateDataUseCase(repository = r) {
    private var testEmit: Async<List<TranslateData>> = Async.Loading()

    override fun invoke(token: String): Flow<Async<List<TranslateData>>> = flow {
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