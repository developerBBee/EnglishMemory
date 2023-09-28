package jp.developer.bbee.englishmemory.data.source.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import jp.developer.bbee.englishmemory.domain.model.TranslateData

@JsonClass(generateAdapter = true)
data class Result(
    @Json(name = "English")
    val english: String,
    @Json(name = "Importance")
    val importance: String,
    @Json(name = "RegistrationDateUTC")
    val registrationDateUTC: String,
    @Json(name = "TranslateToJapanese")
    val translateToJapanese: String,
    @Json(name = "WordType")
    val wordType: String
) {
    fun toTransLateData(): TranslateData {
        return TranslateData(
            english = english,
            wordType = wordType,
            translateToJapanese = translateToJapanese,
            importance = importance,
            registrationDateUTC = registrationDateUTC,
        )
    }
}