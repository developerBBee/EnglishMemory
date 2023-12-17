package jp.developer.bbee.englishmemory.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AccountService {
    override val hasNoUser: Boolean
        get() = auth.currentUser == null

    override fun tokenFlow(): Flow<String> = flow {
        if (hasNoUser) {
            Log.d("AccountServiceImpl", "createAnonymousAccount")
            auth.signInAnonymously().await()
        }

        val token = auth.currentUser?.getIdToken(true)?.await()?.token
        if (token.isNullOrEmpty()) {
            throw RuntimeException("認証できませんでした。\nネットワーク接続を確認してください。")
        }
        emit(token)
    }
}