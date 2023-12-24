package jp.developer.bbee.englishmemory.presentation.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.developer.bbee.englishmemory.common.constant.DATETIME_FORMATTER
import jp.developer.bbee.englishmemory.domain.model.History
import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.domain.usecase.GetHistoryUseCase
import jp.developer.bbee.englishmemory.domain.usecase.GetStudyDataByWordUseCase
import jp.developer.bbee.englishmemory.domain.usecase.UpdateStudyStatusUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class HistoryState(
    val historyOneWeek: List<History>? = null,
    val selectedStudyData: StudyData? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HistoryViewModel @Inject constructor(
    getHistoryUseCase: GetHistoryUseCase,
    getStudyDataByWordUseCase: GetStudyDataByWordUseCase,
    private val updateStudyStatusUseCase: UpdateStudyStatusUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(HistoryState(isLoading = true))
    val state: StateFlow<HistoryState> get() = _state

    private val _selectedHistoryFlow = MutableSharedFlow<History?>(1)
    private val _favoriteFlow = MutableStateFlow<Boolean?>(null)

    init {
        // History from the last week
        getHistoryUseCase(LocalDateTime.now().minusDays(8).format(DATETIME_FORMATTER))
            .onEach {
                _state.value = _state.value.copy(
                    historyOneWeek = it,
                    isLoading = false,
                )
            }
            .catch { e ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message,
                )
            }.launchIn(viewModelScope)

        // TranslateData for selected history
        combine(_selectedHistoryFlow, _favoriteFlow) { history, _ ->
            history
        }
            .onEach {
                _state.value = _state.value.copy(isLoading = true)
            }
            .flatMapLatest { history ->
                if (history != null) {
                    getStudyDataByWordUseCase(history.english, history.wordType)
                } else {
                    flow<StudyData?> { emit(null) }
                }
            }
            .catch { e ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message,
                )
            }
            .onEach {
                _state.value = _state.value.copy(
                    selectedStudyData = it,
                    isLoading = false
                )
            }.launchIn(viewModelScope)
    }

    fun selectHistory(history: History) {
        viewModelScope.launch {
            _selectedHistoryFlow.emit(history)
        }
    }

    fun unSelectHistory() {
        viewModelScope.launch {
            _selectedHistoryFlow.emit(null)
        }
    }

    fun updateStudyStatus(studyData: StudyData) {
        val studyStatus = studyData.toStudyStatus()
        viewModelScope.launch {
            updateStudyStatusUseCase(studyStatus)
            _favoriteFlow.value = studyStatus.isFavorite
        }
    }
}