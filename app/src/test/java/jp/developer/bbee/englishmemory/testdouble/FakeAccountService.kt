package jp.developer.bbee.englishmemory.testdouble

import jp.developer.bbee.englishmemory.service.AccountService

class FakeAccountService : AccountService {
    private var testHasUser = false
    var createAnonymousCheck: Boolean = false
    var userTokenCheck: Boolean = false
    private var fakeToken: String = ""; // empty string means error, not empty means success

    override val hasUser: Boolean
        get() = testHasUser

    override suspend fun createAnonymousAccount() {
        createAnonymousCheck = true
    }

    override suspend fun useTokenCallApi(callApi: (String) -> Unit) {
        userTokenCheck = true
        callApi(fakeToken)
    }

    fun setHasUser(hasUser: Boolean) {
        testHasUser = hasUser
    }

    fun setFakeToken(fakeToken: String) {
        this.fakeToken = fakeToken
    }

    fun testClear() {
        testHasUser = false
        createAnonymousCheck = false
        userTokenCheck = false
        fakeToken = ""
    }
}