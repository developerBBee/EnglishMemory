package jp.developer.bbee.englishmemory.presentation.screen.top

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.developer.bbee.englishmemory.common.response.Response
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.domain.usecase.GetTranslateDataFromDbUseCase
import jp.developer.bbee.englishmemory.domain.usecase.GetTranslateDataUseCase
import jp.developer.bbee.englishmemory.domain.usecase.SaveTranslateDataUseCase
import jp.developer.bbee.englishmemory.service.AccountService
import kotlinx.coroutines.Dispatchers
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
    private var _state = mutableStateOf(TopState())
    val state = _state

    init {
        viewModelScope.launch {
            if (!accountService.hasUser) {
                accountService.createAnonymousAccount()
            }
            accountService.useTokenCallApi { token ->
                getTranslateDataUseCase(token).onEach { response ->
                    processTranslateDataResponse(response)
                }.launchIn(viewModelScope)
            }
        }
    }

    fun save() {
        viewModelScope.launch {
            saveTranslateDataUseCase(state.value.translateData ?: emptyList())
        }
    }

    fun load() {
        getTranslateDataFromDbUseCase().onEach { response ->
            processTranslateDataResponse(response)
        }.launchIn(viewModelScope)
    }

    private fun processTranslateDataResponse(
        response: Response<List<TranslateData>>
    ) {
        when (response) {
            is Response.Loading -> {
                _state.value = TopState(isLoading = true)
            }
            is Response.Success -> {
                _state.value = TopState(translateData = response.data)
            }
            is Response.Failure -> {
                _state.value = TopState(error = response.error)
            }
        }
    }
}