package jp.developer.bbee.englishmemory.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import jp.developer.bbee.englishmemory.common.constant.DATETIME_FORMATTER
import jp.developer.bbee.englishmemory.domain.model.History
import jp.developer.bbee.englishmemory.domain.model.Recent
import jp.developer.bbee.englishmemory.domain.model.StudyStatus
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import java.time.LocalDateTime

@Database(
    entities = [TranslateData::class, StudyStatus::class, Recent::class, History::class],
    version = 1
)
@TypeConverters(DateConverters::class)
abstract class EnglishMemoryDatabase : RoomDatabase() {
    abstract fun getEnglishMemoryDao(): EnglishMemoryDao
}

class DateConverters {
    @TypeConverter
    fun fromString(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, DATETIME_FORMATTER) }
    }

    @TypeConverter
    fun fromLocalDateTime(data: LocalDateTime?): String? {
        return data?.format(DATETIME_FORMATTER)
    }
}