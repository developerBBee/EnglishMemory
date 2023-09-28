package jp.developer.bbee.englishmemory.data.source.remote.api

import jp.developer.bbee.englishmemory.data.source.remote.dto.TranslateDataDto
import retrofit2.http.GET
import retrofit2.http.Headers

interface AwsApi {
    @GET("/")
    @Headers(AuthorizationInterceptor.HEADER_PLACEHOLDER)
    fun getTranslateData(): TranslateDataDto
}