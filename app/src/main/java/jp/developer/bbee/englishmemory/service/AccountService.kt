package jp.developer.bbee.englishmemory.service

import kotlinx.coroutines.flow.Flow

interface AccountService {
    val hasNoUser: Boolean
    fun tokenFlow(): Flow<String>
}