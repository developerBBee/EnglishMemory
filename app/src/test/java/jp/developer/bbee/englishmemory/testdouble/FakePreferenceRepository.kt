package jp.developer.bbee.englishmemory.testdouble

import jp.developer.bbee.englishmemory.domain.model.BookmarkPreferences
import jp.developer.bbee.englishmemory.domain.model.SettingPreferences
import jp.developer.bbee.englishmemory.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

class FakePreferenceRepository : PreferenceRepository {
    override fun getSavedLastDateFlow(): Flow<LocalDateTime?> = flow {
//        emit(null)
        emit(LocalDateTime.now().minusDays(1))
    }

    override suspend fun setSavedLastDate(date: LocalDateTime) {

    }

    override fun getPreferencesFlow(): Flow<Map<SettingPreferences, Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun setPreferences(pref: SettingPreferences, flag: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getBookmarkPreferencesFlow(): Flow<BookmarkPreferences> {
        TODO("Not yet implemented")
    }

    override suspend fun setBookmarkPreferences(prefs: BookmarkPreferences) {
        TODO("Not yet implemented")
    }
}