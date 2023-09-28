package jp.developer.bbee.englishmemory.data.source.remote.api

import jp.developer.bbee.englishmemory.common.auth.ApiAuth
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {
    companion object {
        private const val HEADER_AUTH = "Authorization"
        const val HEADER_PLACEHOLDER = "$HEADER_AUTH: placeholder"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = ApiAuth.getToken()
        if (token.isEmpty()) {
            return chain.proceed(request)
        }

        val newRequest = request.newBuilder()
            .addHeader(HEADER_AUTH, token)
            .build()
        return chain.proceed(newRequest)
    }
}