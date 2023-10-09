package jp.developer.bbee.englishmemory.data.repository

import jp.developer.bbee.englishmemory.data.source.local.EnglishMemoryDao
import jp.developer.bbee.englishmemory.data.source.remote.api.AwsApi
import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.domain.model.StudyStatus
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
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
}