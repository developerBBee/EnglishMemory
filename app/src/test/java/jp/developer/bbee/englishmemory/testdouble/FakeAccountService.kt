package jp.developer.bbee.englishmemory.testdouble

import jp.developer.bbee.englishmemory.service.AccountService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAccountService : AccountService {
    var testHasUser = false
    var createAnonymousCheck: Boolean = false
    var userTokenCheck: Boolean = false
    var fakeToken: String = ""; // empty string means error, not empty means success
    var failureMessage: String = ""
    var delayTime: Long = 0
    override val hasNoUser: Boolean
        get() = !testHasUser

    override fun tokenFlow(): Flow<String> = flow {
        if (hasNoUser) {
            createAnonymousCheck = true
        }
        delay(delayTime)
        if (fakeToken.isEmpty()) throw RuntimeException(failureMessage)
        userTokenCheck = true
        emit(fakeToken)
    }
}