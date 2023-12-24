package jp.developer.bbee.englishmemory.data.repository

import jp.developer.bbee.englishmemory.data.source.local.EnglishMemoryDao
import jp.developer.bbee.englishmemory.data.source.remote.api.AwsApi
import jp.developer.bbee.englishmemory.domain.model.History
import jp.developer.bbee.englishmemory.domain.model.Recent
import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.domain.model.StudyStatus
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TranslateRepositoryImpl @Inject constructor(
    private val awsApi: AwsApi,
    private val dao: EnglishMemoryDao,
) : TranslateRepository {
    override suspend fun getTranslateData(token: String): List<TranslateData> {
        return awsApi.getTranslateData(token).toTransLateDataList()
    }

    override suspend fun getTranslateData(): List<TranslateData> {
        return dao.getTranslateData()
    }

    override suspend fun saveTranslateData(translateDataList: List<TranslateData>) {
        dao.insertUpdateTranslateData(translateDataList)
    }

    override suspend fun getStudyData(): List<StudyData> {
        return dao.getStudyData()
    }

    override suspend fun updateStudyStatus(studyStatus: StudyStatus) {
        dao.insertUpdateStudyStatus(studyStatus)
    }

    override fun getRecent(): Flow<List<Recent>> {
        return dao.getRecent()
    }

    override suspend fun updateRecent(recent: List<Recent>) {
        dao.insertUpdateRecent(recent)
    }

    override suspend fun updateHistory(history: History) {
        dao.insertUpdateHistory(history)
    }

    override fun getHistory(dateTimeFrom: String): Flow<List<History>> {
        return dao.getHistory(dateTimeFrom)
    }

    override fun getStudyDataByWord(english: String, wordType: String): Flow<StudyData> {
        return dao.getStudyDataByWord(english, wordType)
    }
}