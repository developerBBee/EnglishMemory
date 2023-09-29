package jp.developer.bbee.englishmemory.domain.model

import androidx.room.Entity

/**
 * 学習状況
 */
@Entity(primaryKeys = ["english", "wordType"])
data class StudyStatus(
    val english: String,
    val wordType: String,
    val numberOfQuestion: Int = 0,
    val scoreRate: Int = 0,
    val countMiss: Int = 0,
    val countCorrect: Int = 0,
    val isLatestAnswerCorrect: Boolean = false,
    val isFavorite: Boolean = false,
)
