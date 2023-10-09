package jp.developer.bbee.englishmemory.service

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AccountService {
    override val hasUser: Boolean
        get() = auth.currentUser != null

    override suspend fun createAnonymousAccount() {
        auth.signInAnonymously().await()
    }

    override suspend fun useTokenCallApi(callApi: (String)->Unit) {
        auth.currentUser?.getIdToken(true)?.addOnCompleteListener {
            var token = ""
            try {
                token = it.result.token ?: ""
            } catch (_: Exception) { }
            callApi(token)
        }
    }
}