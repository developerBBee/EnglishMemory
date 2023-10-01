package jp.developer.bbee.englishmemory.testdouble

import jp.developer.bbee.englishmemory.service.AccountService

class FakeAccountService : AccountService {
    private var testHasUser = false
    var createAnonymousCheck: Boolean = false
    var userTokenCheck: Boolean = false

    override val hasUser: Boolean
        get() = testHasUser

    override suspend fun createAnonymousAccount() {
        createAnonymousCheck = true
    }

    override suspend fun useTokenCallApi(callApi: (String) -> Unit) {
        userTokenCheck = true
        callApi("")
    }

    fun setHasUser(hasUser: Boolean) {
        testHasUser = hasUser
    }

    fun testClear() {
        testHasUser = false
        createAnonymousCheck = false
        userTokenCheck = false
    }
}