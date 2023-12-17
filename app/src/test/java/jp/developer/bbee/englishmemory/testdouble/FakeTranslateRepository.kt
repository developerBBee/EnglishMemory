package jp.developer.bbee.englishmemory.testdouble

import jp.developer.bbee.englishmemory.domain.model.Recent
import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.domain.model.StudyStatus
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTranslateRepository : TranslateRepository {
    var failureGetTranslateDataByToken = false
    var failureGetTranslateData = false
    var failureSaveTranslateData = false
    var failureMessage: String = ""
    var delayTime: Long = 0
    var testGetResult: List<TranslateData> = emptyList()
    var testSaveData: List<TranslateData>? = null

    override suspend fun getTranslateData(token: String): List<TranslateData> {
        delay(delayTime)
        if (failureGetTranslateDataByToken) throw RuntimeException(failureMessage)
        return testGetResult
    }

    override suspend fun getTranslateData(): List<TranslateData> {
        delay(delayTime)
        if (failureGetTranslateData) throw RuntimeException(failureMessage)
        return testGetResult
    }

    override suspend fun saveTranslateData(translateDataList: List<TranslateData>) {
        delay(delayTime)
        if (failureSaveTranslateData) throw RuntimeException(failureMessage)
        testSaveData = translateDataList
        return
    }

    override suspend fun getStudyData(): List<StudyData> {
        return emptyList()
    }

    override suspend fun updateStudyStatus(studyStatus: StudyStatus) {
        return
    }

    override fun getRecent(): Flow<List<Recent>> {
        return flow { emit(emptyList())  }
    }

    override suspend fun updateRecent(recent: List<Recent>) {
        return
    }
}