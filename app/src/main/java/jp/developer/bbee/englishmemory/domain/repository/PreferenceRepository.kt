package jp.developer.bbee.englishmemory.domain.repository

import jp.developer.bbee.englishmemory.domain.model.BookmarkPreferences
import jp.developer.bbee.englishmemory.domain.model.SettingPreferences
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface PreferenceRepository {
    fun getSavedLastDateFlow(): Flow<LocalDateTime?>
    suspend fun setSavedLastDate(date: LocalDateTime)

    fun getPreferencesFlow(): Flow<Map<SettingPreferences, Boolean>>
    suspend fun setPreferences(pref: SettingPreferences, flag: Boolean)

    fun getBookmarkPreferencesFlow(): Flow<BookmarkPreferences>
    suspend fun setBookmarkPreferences(prefs: BookmarkPreferences)
}