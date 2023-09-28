package jp.developer.bbee.englishmemory.data.source.remote.api

import jp.developer.bbee.englishmemory.data.source.remote.dto.TranslateDataDto
import retrofit2.http.GET
import retrofit2.http.Header

interface AwsApi {
    @GET("/release")
    suspend fun getTranslateData(@Header("Authorization") token: String): TranslateDataDto
}