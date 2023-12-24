package jp.developer.bbee.englishmemory.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 全ての学習履歴
 */
@Entity
data class History(
    @PrimaryKey
    val studyDate: LocalDateTime,
    val english: String,
    val wordType: String,
    val correct: Boolean,
) {

    // dateChangeHourは、ユーザー設定された日付変更時刻
    fun toUserDate(dateChangeHour: Long): LocalDate {
        val dateTime = studyDate
        dateTime.minusHours(dateChangeHour)
        return dateTime.toLocalDate()
    }
}