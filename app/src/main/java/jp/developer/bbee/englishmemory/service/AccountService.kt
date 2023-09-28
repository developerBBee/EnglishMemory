package jp.developer.bbee.englishmemory.service

interface AccountService {
    val hasUser: Boolean
    suspend fun createAnonymousAccount()
    suspend fun useTokenCallApi(callApi: (String)->Unit)
}