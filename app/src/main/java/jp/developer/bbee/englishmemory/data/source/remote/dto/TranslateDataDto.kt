package jp.developer.bbee.englishmemory.data.source.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import jp.developer.bbee.englishmemory.domain.model.TranslateData

@JsonClass(generateAdapter = true)
data class TranslateDataDto(
    @Json(name = "message")
    val message: String,
    @Json(name = "result")
    val results: List<Result>
)

fun TranslateDataDto.toTranslateDataList(): List<TranslateData> {
    return results.map {
        it.toTranslateData()
    }
}