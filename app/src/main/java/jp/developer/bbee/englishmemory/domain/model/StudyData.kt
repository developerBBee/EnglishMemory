package jp.developer.bbee.englishmemory.domain.model

/**
 * Joined TranslateData and StudyStatus
 */
data class StudyData(
    val english: String,
    val wordType: String,
    val translateToJapanese: String,
    val importance: String,
    val registrationDateUTC: String,
    val numberOfQuestion: Int = 0, // 出題回数
    val scoreRate: Double = 0.0, // 正解率
    val countMiss: Int = 0, // 間違えた回数
    val countCorrect: Int = 0, // 正解した回数
    val isLatestAnswerCorrect: Boolean = false, // 最新の回答が正解かどうか
    val isFavorite: Boolean = false, // お気に入りかどうか
) {
    fun toStudyStatus() = StudyStatus(
        english, wordType, numberOfQuestion, scoreRate, countMiss, countCorrect, isLatestAnswerCorrect, isFavorite
    )
}
