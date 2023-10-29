package jp.developer.bbee.englishmemory.testdouble

import jp.developer.bbee.englishmemory.domain.model.Recent
import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.domain.model.StudyStatus
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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

    override suspend fun getStudyData(): List<StudyData> {
        return emptyList()
    }

    override suspend fun updateStudyStatus(studyStatus: StudyStatus) {
        testSaveCheck = true
        return
    }

    override fun getRecent(): Flow<List<Recent>> {
        return flow { emit(emptyList())  }
    }

    override suspend fun updateRecent(recent: List<Recent>) {
        testSaveCheck = true
        return
    }

    fun testClear() {
        testSaveCheck = false
    }
}