package jp.developer.bbee.englishmemory.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 最近学習した単語の履歴
 */
@Entity
data class Recent(
    @PrimaryKey
    val recentNumber: Int,
    val english: String,
    val wordType: String,
)
