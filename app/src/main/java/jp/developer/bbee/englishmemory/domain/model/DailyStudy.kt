package jp.developer.bbee.englishmemory.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

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
) {

    fun getDate(dateChangeHour: Long): LocalDate {
        val dateTime = LocalDateTime.parse(studyDate)
        dateTime.minusHours(dateChangeHour)
        return dateTime.toLocalDate()
    }
}