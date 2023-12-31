package jp.developer.bbee.englishmemory.data.source.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import jp.developer.bbee.englishmemory.domain.model.FilterKey
import jp.developer.bbee.englishmemory.domain.model.BookmarkPreferences
import jp.developer.bbee.englishmemory.domain.model.Order
import jp.developer.bbee.englishmemory.domain.model.SearchKey
import jp.developer.bbee.englishmemory.domain.model.SettingPreferences
import jp.developer.bbee.englishmemory.domain.model.SortKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceStore @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>
) {

    fun getPreferencesFlow(): Flow<Map<SettingPreferences, Boolean>> = preferencesDataStore.data
        .map { preferences ->
            SettingPreferences.entries.associateWith { key ->
                preferences[booleanPreferencesKey(key.name)] ?: false
            }
        }

    suspend fun setPreferences(pref: SettingPreferences, flag: Boolean) {
        preferencesDataStore.edit { preferences ->
            preferences[booleanPreferencesKey(pref.name)] = flag
        }
    }

    fun getBookmarkPreferencesFlow(): Flow<BookmarkPreferences> = preferencesDataStore.data
        .map { preferences ->
            BookmarkPreferences.from(
                searchWord = preferences[stringPreferencesKey(SearchKey.SEARCH_WORD.name)] ?: "",
                bookmarkBooleans = FilterKey.entries.associateWith { key ->
                    preferences[booleanPreferencesKey(key.name)] ?: true
                },
                sortPrefs = SortKey.entries.associateWith { key ->
                    val order = preferences[stringPreferencesKey(key.name)] ?: Order.NONE.name
                    Order.valueOf(order)
                },
            )
        }

    suspend fun setBookmarkPreferences(prefs: BookmarkPreferences) {
        preferencesDataStore.edit { preferences ->
            preferences[stringPreferencesKey(SearchKey.SEARCH_WORD.name)] = prefs.searchWord
            prefs.bookmarkBooleans.forEach { (key, value) ->
                preferences[booleanPreferencesKey(key.name)] = value
            }
            prefs.sortPrefs.forEach { (key, value) ->
                val order = value.name
                preferences[stringPreferencesKey(key.name)] = order
            }
        }
    }
}