package jp.developer.bbee.englishmemory.presentation.screen.bookmark.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.developer.bbee.englishmemory.common.response.Async
import jp.developer.bbee.englishmemory.domain.model.BookmarkKey
import jp.developer.bbee.englishmemory.domain.model.BookmarkPreferences
import jp.developer.bbee.englishmemory.domain.usecase.GetBookmarkPreferenceUseCase
import jp.developer.bbee.englishmemory.domain.usecase.SaveBookmarkPreferenceUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class BookmarkSettingState(
    val isLoading: Boolean = false,
    val preferences: BookmarkPreferences? = null,
    val error: String? = null,
)

@HiltViewModel
class BookmarkSettingViewModel @Inject constructor(
    getBookmarkPreferenceUseCase: GetBookmarkPreferenceUseCase,
    private val saveBookmarkPreferenceUseCase: SaveBookmarkPreferenceUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BookmarkSettingState())
    val state: StateFlow<BookmarkSettingState> get() = _state

    private val _preferencesStateFlow = MutableStateFlow(BookmarkPreferences.from())

    init {
        _state.value = BookmarkSettingState(isLoading = true)

        _preferencesStateFlow
            .onEach {
                _state.value = _state.value.copy(
                    isLoading = false,
                    preferences = it,
                    error = null,
                )
            }
            .launchIn(viewModelScope)

        getBookmarkPreferenceUseCase()
            .onEach {
                _preferencesStateFlow.value = it
            }.catch {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = it.message ?: "error",
                )
            }.launchIn(viewModelScope)
    }

    private var saveJob: Job? = null

    fun saveBookmarkPreferences() {
        val prefs = _preferencesStateFlow.value

        saveJob?.cancel()
        saveJob = saveBookmarkPreferenceUseCase(prefs)
            .onEach {
                when (it) {
                    is Async.Loading -> _state.value = _state.value.copy(
                        isLoading = true,
                        error = null,
                    )
                    is Async.Success -> {}
                    is Async.Failure -> _state.value = _state.value.copy(
                        isLoading = false,
                        error = it.error,
                    )
                }
            }.catch {
                when (it) {
                    is CancellationException -> {
                        Log.d("BookmarkSettingViewModel", "saveJob is canceled")
                    }
                    else -> _state.value = _state.value.copy(
                        isLoading = false,
                        error = it.message ?: "error",
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun updatePreferences(key: BookmarkKey, value: Boolean) {
        val prefs = _preferencesStateFlow.value
        val map = prefs.bookmarkBooleans.toMutableMap()
        map[key] = value
        _preferencesStateFlow.value = prefs.copy(bookmarkBooleans = map)
    }

    fun updatePreferences(word: String) {
        val prefs = _preferencesStateFlow.value
        _preferencesStateFlow.value = prefs.copy(searchWord = word)
    }
}