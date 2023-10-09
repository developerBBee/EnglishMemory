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
) {
    fun updateRecentList(
        recentList: List<Recent>,
        translateData: TranslateData,
    ): List<Recent> {
        val newRecentList = mutableListOf<Recent>()
        recentList.forEach {
            if (it.recentNumber < 99) {
                newRecentList.add(
                    Recent(it.recentNumber + 1, it.english, it.wordType)
                )
            }
        }
        newRecentList.add(
            Recent(0, translateData.english, translateData.wordType)
        )
        return newRecentList
    }
}
