package jp.developer.bbee.englishmemory.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TranslateData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val english: String,
    val wordType: String,
    val translateToJapanese: String,
    val importance: String,
    val registrationDateUTC: String,
)