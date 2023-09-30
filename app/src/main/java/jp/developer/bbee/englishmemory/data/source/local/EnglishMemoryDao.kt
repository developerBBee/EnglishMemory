package jp.developer.bbee.englishmemory.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.developer.bbee.englishmemory.domain.model.DailyStudy
import jp.developer.bbee.englishmemory.domain.model.Recent
import jp.developer.bbee.englishmemory.domain.model.StudyStatus
import jp.developer.bbee.englishmemory.domain.model.TranslateData

@Dao
interface EnglishMemoryDao {
    @Insert(entity = TranslateData::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateTranslateData(translateDataList: List<TranslateData>)

    @Insert(entity = StudyStatus::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateStudyStatus(studyStatusList: List<StudyStatus>)

    @Insert(entity = Recent::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateRecent(recentList: List<Recent>)

    @Insert(entity = DailyStudy::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateDailyStudy(dailyStudyList: List<DailyStudy>)

    @Query("SELECT * FROM TranslateData")
    suspend fun getTranslateData(): List<TranslateData>

    @Query("SELECT * FROM StudyStatus")
    suspend fun getStudyStatus(): List<StudyStatus>

    @Query("SELECT * FROM Recent")
    suspend fun getRecent(): List<Recent>

    @Query("SELECT * FROM DailyStudy")
    suspend fun getDailyStudy(): List<DailyStudy>
}