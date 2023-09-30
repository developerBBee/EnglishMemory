package jp.developer.bbee.englishmemory.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.developer.bbee.englishmemory.domain.model.DailyStudy
import jp.developer.bbee.englishmemory.domain.model.Recent
import jp.developer.bbee.englishmemory.domain.model.StudyStatus
import jp.developer.bbee.englishmemory.domain.model.TranslateData

@Database(
    entities = [TranslateData::class, StudyStatus::class, Recent::class, DailyStudy::class],
    version = 1
)
abstract class EnglishMemoryDatabase : RoomDatabase() {
    abstract fun getEnglishMemoryDao(): EnglishMemoryDao
}