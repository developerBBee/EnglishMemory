package jp.developer.bbee.englishmemory.testdouble

import jp.developer.bbee.englishmemory.domain.model.History
import jp.developer.bbee.englishmemory.domain.model.Recent
import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.domain.model.StudyStatus
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

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

    override fun getStudyData(): Flow<List<StudyData>> {
        TODO()
    }

    override suspend fun updateStudyStatus(studyStatus: StudyStatus) {
        TODO()
    }

    override fun getRecent(): Flow<List<Recent>> {
        TODO()
    }

    override suspend fun updateRecent(recent: List<Recent>) {
        TODO()
    }

    override suspend fun updateHistory(history: History) {
        TODO()
    }

    override fun getHistory(dateTimeFrom: String): Flow<List<History>> {
        TODO()
    }

    override fun getStudyDataByWord(english: String, wordType: String): Flow<StudyData> {
        TODO()
    }
}