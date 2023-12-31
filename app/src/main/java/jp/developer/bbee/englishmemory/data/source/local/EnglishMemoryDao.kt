package jp.developer.bbee.englishmemory.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.developer.bbee.englishmemory.domain.model.History
import jp.developer.bbee.englishmemory.domain.model.Recent
import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.domain.model.StudyStatus
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import kotlinx.coroutines.flow.Flow

@Dao
interface EnglishMemoryDao {
    @Insert(entity = TranslateData::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateTranslateData(translateDataList: List<TranslateData>)

    @Insert(entity = StudyStatus::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateStudyStatus(studyStatus: StudyStatus)

    @Insert(entity = Recent::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateRecent(recentList: List<Recent>)

    @Insert(entity = History::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateHistory(history: History)

    @Query("SELECT * FROM TranslateData")
    suspend fun getTranslateData(): List<TranslateData>

    @Query("""
        SELECT
            TranslateData.english AS english,
            TranslateData.wordType AS wordType,
            TranslateData.translateToJapanese AS translateToJapanese,
            TranslateData.importance AS importance,
            TranslateData.registrationDateUTC AS registrationDateUTC,
            StudyStatus.numberOfQuestion AS numberOfQuestion,
            StudyStatus.scoreRate AS scoreRate,
            StudyStatus.countMiss AS countMiss,
            StudyStatus.countCorrect AS countCorrect,
            StudyStatus.isLatestAnswerCorrect AS isLatestAnswerCorrect,
            StudyStatus.isFavorite AS isFavorite
        FROM TranslateData
        LEFT OUTER JOIN StudyStatus
        ON  TranslateData.english = StudyStatus.english
        AND TranslateData.wordType = StudyStatus.wordType
    """)
    fun getStudyData(): Flow<List<StudyData>>

    @Query("SELECT * FROM StudyStatus WHERE english = :english and wordType = :wordType")
    suspend fun getStudyStatus(english: String, wordType: String): StudyStatus

    @Query("SELECT * FROM Recent")
    fun getRecent(): Flow<List<Recent>>

    @Query("SELECT * FROM History WHERE studyDate >= :dateTimeFrom")
    fun getHistory(dateTimeFrom: String): Flow<List<History>>

    @Query("""
        SELECT
            TranslateData.english AS english,
            TranslateData.wordType AS wordType,
            TranslateData.translateToJapanese AS translateToJapanese,
            TranslateData.importance AS importance,
            TranslateData.registrationDateUTC AS registrationDateUTC,
            StudyStatus.numberOfQuestion AS numberOfQuestion,
            StudyStatus.scoreRate AS scoreRate,
            StudyStatus.countMiss AS countMiss,
            StudyStatus.countCorrect AS countCorrect,
            StudyStatus.isLatestAnswerCorrect AS isLatestAnswerCorrect,
            StudyStatus.isFavorite AS isFavorite
        FROM TranslateData
        LEFT OUTER JOIN StudyStatus
        ON  TranslateData.english = StudyStatus.english
        AND TranslateData.wordType = StudyStatus.wordType
        WHERE TranslateData.english = :english AND TranslateData.wordType = :wordType
    """)
    fun getStudyDataByWord(english: String, wordType: String): Flow<StudyData>


}