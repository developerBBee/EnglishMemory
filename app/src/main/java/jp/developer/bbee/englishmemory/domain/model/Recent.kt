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
    val correct: Boolean,
) {
    constructor(studyData: StudyData, correct: Boolean) : this(
        0,
        studyData.english,
        studyData.wordType,
        correct,
    )

    fun updateRecentList(recentList: List<Recent>): List<Recent> {
        val newRecentList = mutableListOf<Recent>()
        recentList.forEach {
            if (it.recentNumber < 99) {
                newRecentList.add(
                    Recent(it.recentNumber + 1, it.english, it.wordType, it.correct)
                )
            }
        }
        newRecentList.add(
            Recent(0, this.english, this.wordType, this.correct)
        )
        return newRecentList
    }
}
