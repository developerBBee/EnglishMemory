package jp.developer.bbee.englishmemory.testdouble

import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository

class FakeTranslateRepository : TranslateRepository {
    var testSaveCheck = false

    override suspend fun getTranslateData(token: String): List<TranslateData> {
        return emptyList()
    }

    override suspend fun getTranslateData(): List<TranslateData> {
        return emptyList()
    }

    override suspend fun saveTranslateData(translateDataList: List<TranslateData>) {
        testSaveCheck = true
        return
    }

    fun testClear() {
        testSaveCheck = false
    }
}