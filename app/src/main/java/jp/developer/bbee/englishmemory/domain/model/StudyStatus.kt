package jp.developer.bbee.englishmemory.domain.model

import androidx.room.Entity

/**
 * 学習状況
 */
@Entity(primaryKeys = ["english", "wordType"])
data class StudyStatus(
    val english: String,
    val wordType: String,
    val numberOfQuestion: Int = 0, // 出題回数
    val scoreRate: Double = 0.0, // 正解率
    val countMiss: Int = 0, // 間違えた回数
    val countCorrect: Int = 0, // 正解した回数
    val isLatestAnswerCorrect: Boolean = false, // 最新の回答が正解かどうか
    val isFavorite: Boolean = false, // お気に入りかどうか
)
