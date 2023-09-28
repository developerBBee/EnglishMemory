package jp.developer.bbee.englishmemory.common.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object ApiAuth {
    private lateinit var auth: FirebaseAuth

    fun init() {
        auth = Firebase.auth
    }

    fun getAuth(): FirebaseAuth {
        return auth
    }

    fun getToken(): String {
        return auth.currentUser?.getIdToken(true)?.result?.token ?: ""
    }
}