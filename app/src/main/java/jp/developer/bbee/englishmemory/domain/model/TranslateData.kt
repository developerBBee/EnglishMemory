package jp.developer.bbee.englishmemory.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 単語の翻訳データ
 */
@Entity(primaryKeys = ["english", "wordType"])
data class TranslateData(
    val english: String,
    val wordType: String,
    val translateToJapanese: String,
    val importance: String,
    val registrationDateUTC: String,
)