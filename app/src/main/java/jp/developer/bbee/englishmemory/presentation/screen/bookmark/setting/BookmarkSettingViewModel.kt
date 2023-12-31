package jp.developer.bbee.englishmemory.presentation.screen.bookmark.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.developer.bbee.englishmemory.common.response.Async
import jp.developer.bbee.englishmemory.domain.model.FilterKey
import jp.developer.bbee.englishmemory.domain.model.BookmarkPreferences
import jp.developer.bbee.englishmemory.domain.model.Order
import jp.developer.bbee.englishmemory.domain.model.SortKey
import jp.developer.bbee.englishmemory.domain.usecase.GetBookmarkPreferenceUseCase
import jp.developer.bbee.englishmemory.domain.usecase.SaveBookmarkPreferenceUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookmarkSettingState(
    val isLoading: Boolean = false,
    val preferences: BookmarkPreferences? = null,
    val error: String? = null,
    val isSaved: Boolean = false,
)

@HiltViewModel
class BookmarkSettingViewModel @Inject constructor(
    getBookmarkPreferenceUseCase: GetBookmarkPreferenceUseCase,
    private val saveBookmarkPreferenceUseCase: SaveBookmarkPreferenceUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BookmarkSettingState())
    val state: StateFlow<BookmarkSettingState> get() = _state

    private val _preferencesFlow = MutableSharedFlow<BookmarkPreferences>(replay = 1)

    init {
        _state.value = BookmarkSettingState(isLoading = true)

        _preferencesFlow
            .onEach {
                // 画面クローズ後はemitしない
                if (saveJob == null) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        preferences = it,
                        error = null,
                    )
                }
            }.launchIn(viewModelScope)

        getBookmarkPreferenceUseCase()
            .onEach {
                _preferencesFlow.emit(it)
            }.catch {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = it.message ?: "error",
                )
            }.launchIn(viewModelScope)
    }

    private var saveJob: Job? = null

    fun saveBookmarkPreferences() {
        saveJob?.cancel()
        viewModelScope.launch {
            val prefs = _preferencesFlow.first()
            saveJob = saveBookmarkPreferenceUseCase(prefs)
                .onEach {
                    when (it) {
                        is Async.Loading -> _state.value = _state.value.copy(
                            isLoading = true,
                            error = null,
                        )
                        is Async.Success -> _state.value = _state.value.copy(
                            isLoading = true,
                            error = null,
                            isSaved = true
                        )
                        is Async.Failure -> _state.value = _state.value.copy(
                            isLoading = false,
                            error = it.error,
                        )
                    }
                }.catch {
                    when (it) {
                        is CancellationException -> {
                            Log.d("BookmarkSetting", "saveJob is canceled")
                        }
                        else -> {
                            Log.d("BookmarkSetting", "saveJob is failed ${it.message}")
                        }
                    }
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = it.message ?: "error",
                    )
                }.launchIn(viewModelScope)
        }
    }

    fun updateSearchPreferences(word: String) = viewModelScope.launch {
        val prefs = _preferencesFlow.first()
        _preferencesFlow.emit(prefs.copy(searchWord = word))
    }

    fun updateFilterPreferences(key: FilterKey, value: Boolean) = viewModelScope.launch {
        val prefs = _preferencesFlow.first()
        val map = prefs.bookmarkBooleans.toMutableMap()
        map[key] = value
        _preferencesFlow.emit(prefs.copy(bookmarkBooleans = map))
    }

    fun updateSortPreferences(key: SortKey, value: Order) = viewModelScope.launch {
        val prefs = _preferencesFlow.first()
        val sortPrefs = prefs.sortPrefs.toMutableMap()
        sortPrefs[key] = value
        _preferencesFlow.emit(prefs.copy(sortPrefs = sortPrefs))
    }
}