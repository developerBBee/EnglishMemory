package jp.developer.bbee.englishmemory.presentation.screen.top

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.developer.bbee.englishmemory.common.response.Async
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.usecase.GetTranslateDataFromDbUseCase
import jp.developer.bbee.englishmemory.domain.usecase.GetTranslateDataUseCase
import jp.developer.bbee.englishmemory.domain.usecase.SaveTranslateDataUseCase
import jp.developer.bbee.englishmemory.service.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TopState(
    val translateData: List<TranslateData>? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class TopViewModel @Inject constructor(
    private val getTranslateDataUseCase: GetTranslateDataUseCase,
    private val saveTranslateDataUseCase: SaveTranslateDataUseCase,
    private val getTranslateDataFromDbUseCase: GetTranslateDataFromDbUseCase,
    private val accountService: AccountService,
) : ViewModel() {
    private var _state = MutableStateFlow(TopState(isLoading = true))
    val state = _state.asStateFlow()

    var isReady by mutableStateOf(false)
    var isRestart by mutableStateOf(false)

    init {
        viewModelScope.launch {
            if (!accountService.hasUser) {
                    accountService.createAnonymousAccount()
            }
            accountService.useTokenCallApi { token ->
                if (token.isEmpty()) {
                    _state.value = TopState(error = "認証できませんでした。\nネットワーク接続を確認してください。")
                } else {
                    getTranslateDataUseCase(token).onEach { response ->
                        processTranslateDataResponse(response) {
                            save(it)
                            isReady = true
                        }
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun save(translateData: List<TranslateData>? = _state.value.translateData) {
        viewModelScope.launch {
            saveTranslateDataUseCase(translateData ?: emptyList())
        }
    }

    fun load() {
        getTranslateDataFromDbUseCase().onEach { response ->
            processTranslateDataResponse(response)
        }.launchIn(viewModelScope)
    }

    private fun processTranslateDataResponse(
        async: Async<List<TranslateData>>,
        saveSuccessData: (List<TranslateData>?) -> Unit = {},
    ) {
        when (async) {
            is Async.Loading -> {
                _state.value = TopState(isLoading = true)
            }
            is Async.Success -> {
                saveSuccessData(async.data)
                _state.value = TopState(translateData = async.data)
            }
            is Async.Failure -> {
                _state.value = TopState(error = async.error)
            }
        }
    }

    fun restart() {
        isReady = false
        isRestart = true
        _state.value = TopState(isLoading = true)
    }
}