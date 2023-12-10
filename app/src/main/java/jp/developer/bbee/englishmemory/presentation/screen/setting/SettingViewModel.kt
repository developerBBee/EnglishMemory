package jp.developer.bbee.englishmemory.presentation.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.developer.bbee.englishmemory.common.response.Async
import jp.developer.bbee.englishmemory.domain.model.SettingPreferences
import jp.developer.bbee.englishmemory.domain.usecase.GetPreferenceUseCase
import jp.developer.bbee.englishmemory.domain.usecase.SavePreferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class SettingState(
    val prefs: Map<SettingPreferences, Boolean>? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    getPreferenceUseCase: GetPreferenceUseCase,
    private val savePreferenceUseCase: SavePreferenceUseCase,
) : ViewModel() {

    private var _state = MutableStateFlow(SettingState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        getPreferenceUseCase().onEach {
            _state.emit(state.prefs(prefs = it))
        }.launchIn(viewModelScope)
    }

    fun setPreferences(pref: SettingPreferences, flag: Boolean) {
        runFlowAsync(
            flow = savePreferenceUseCase(pref, flag),
            loading = { _state.emit(state.loading(isLoading = true)) },
            failure = { _state.emit(state.error(error = it ?: "Error")) },
        )
    }
}

fun <T> ViewModel.runFlowAsync(
    flow: Flow<Async<T>>,
    loading: suspend () -> Unit = {},
    failure: suspend (String?) -> Unit = {},
    success: suspend (T?) -> Unit = {},
) = flow.runFlowAsync(
    loading = loading,
    failure = failure,
    success = success,
).launchIn(this.viewModelScope)

fun <T> Flow<Async<T>>.runFlowAsync(
    loading: suspend () -> Unit = {},
    failure: suspend (String?) -> Unit = {},
    success: suspend (T?) -> Unit = {},
) = onEach {
    when (it) {
        is Async.Loading -> loading()
        is Async.Failure -> failure(it.error)
        is Async.Success -> success(it.data)
    }
}

fun StateFlow<SettingState>.prefs(prefs: Map<SettingPreferences, Boolean>) =
    value.copy(prefs = prefs, isLoading = false)

fun StateFlow<SettingState>.loading(isLoading: Boolean) =
    value.copy(isLoading = isLoading)

fun StateFlow<SettingState>.error(error: String) =
    value.copy(error = error)
