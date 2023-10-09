package jp.developer.bbee.englishmemory.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.developer.bbee.englishmemory.domain.model.DailyStudy
import jp.developer.bbee.englishmemory.domain.model.Recent
import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.domain.model.StudyStatus
import jp.developer.bbee.englishmemory.domain.model.TranslateData

@Dao
interface EnglishMemoryDao {
    @Insert(entity = TranslateData::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateTranslateData(translateDataList: List<TranslateData>)

    @Insert(entity = StudyStatus::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateStudyStatus(studyStatus: StudyStatus)

    @Insert(entity = Recent::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateRecent(recentList: List<Recent>)

    @Insert(entity = DailyStudy::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateDailyStudy(dailyStudyList: List<DailyStudy>)

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
    suspend fun getStudyData(): List<StudyData>

    @Query("SELECT * FROM StudyStatus WHERE english=:english and wordType=:wordType")
    suspend fun getStudyStatus(english: String, wordType: String): StudyStatus

    @Query("SELECT * FROM Recent")
    suspend fun getRecent(): List<Recent>

    @Query("SELECT * FROM DailyStudy")
    suspend fun getDailyStudy(): List<DailyStudy>
}