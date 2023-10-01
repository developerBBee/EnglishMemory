package jp.developer.bbee.englishmemory.testdouble

import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import jp.developer.bbee.englishmemory.domain.usecase.SaveTranslateDataUseCase

class FakeSaveTranslateDataUseCase(
    private val r: TranslateRepository = FakeTranslateRepository()
) : SaveTranslateDataUseCase(repository = r) {
    override suspend fun invoke(data: List<TranslateData>) {
        r.saveTranslateData(data)
    }
}