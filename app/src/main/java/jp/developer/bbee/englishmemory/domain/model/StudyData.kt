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
    fun getStudyStatus() = StudyStatus(
        english, wordType, numberOfQuestion, scoreRate, countMiss, countCorrect, isLatestAnswerCorrect, isFavorite
    )

    fun updateStudyData(
        english: String = this.english,
        wordType: String = this.wordType,
        translateToJapanese: String = this.translateToJapanese,
        importance: String = this.importance,
        registrationDateUTC: String = this.registrationDateUTC,
        numberOfQuestion: Int = this.numberOfQuestion,
        scoreRate: Double = this.scoreRate,
        countMiss: Int = this.countMiss,
        countCorrect: Int = this.countCorrect,
        isLatestAnswerCorrect: Boolean = this.isLatestAnswerCorrect,
        isFavorite: Boolean = this.isFavorite
    ): StudyData {
        return StudyData(english, wordType, translateToJapanese, importance, registrationDateUTC, numberOfQuestion, scoreRate, countMiss, countCorrect, isLatestAnswerCorrect, isFavorite)
    }
}
