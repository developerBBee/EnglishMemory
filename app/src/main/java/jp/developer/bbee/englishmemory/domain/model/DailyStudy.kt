package jp.developer.bbee.englishmemory.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 1日の勉強内容を表す
 */
@Entity
data class DailyStudy(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val studyDate: String,
    val english: String,
    val wordType: String,
)
