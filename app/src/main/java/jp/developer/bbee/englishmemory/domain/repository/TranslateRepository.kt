package jp.developer.bbee.englishmemory.domain.repository

import jp.developer.bbee.englishmemory.domain.model.History
import jp.developer.bbee.englishmemory.domain.model.Recent
import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.domain.model.StudyStatus
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import kotlinx.coroutines.flow.Flow

interface TranslateRepository {
    suspend fun getTranslateData(token: String): List<TranslateData>
    suspend fun getTranslateData(): List<TranslateData>
    suspend fun saveTranslateData(translateDataList: List<TranslateData>)
    fun getStudyData(): Flow<List<StudyData>>
    suspend fun updateStudyStatus(studyStatus: StudyStatus)
    fun getRecent(): Flow<List<Recent>>
    suspend fun updateRecent(recent: List<Recent>)
    suspend fun updateHistory(history: History)
    fun getHistory(dateTimeFrom: String): Flow<List<History>>
    fun getStudyDataByWord(english: String, wordType: String): Flow<StudyData>
}