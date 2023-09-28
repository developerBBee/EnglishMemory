package jp.developer.bbee.englishmemory.presentation.screen.top

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.developer.bbee.englishmemory.common.response.NetworkResponse
import jp.developer.bbee.englishmemory.data.source.remote.dto.TranslateDataDto
import jp.developer.bbee.englishmemory.domain.usecase.GetTranslateDataUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class TopState(
    val translateData: TranslateDataDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

class TopViewModel(
    private val getTranslateDataUseCase: GetTranslateDataUseCase,
) : ViewModel() {
    private var _state = mutableStateOf(TopState())
    val state = _state

    init {
        getTranslateDataUseCase().onEach { response ->
            when (response) {
                is NetworkResponse.Loading -> {
                    _state.value = TopState(isLoading = true)
                }
                is NetworkResponse.Success -> {
                    _state.value = TopState(translateData = response.data)
                }
                is NetworkResponse.Failure -> {
                    _state.value = TopState(error = response.error)
                }
            }
        }.launchIn(viewModelScope)
    }
}