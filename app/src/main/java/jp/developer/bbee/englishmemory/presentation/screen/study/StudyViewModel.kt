package jp.developer.bbee.englishmemory.presentation.screen.study

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.developer.bbee.englishmemory.domain.model.History
import jp.developer.bbee.englishmemory.domain.model.Recent
import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.domain.usecase.AddHistoryUseCase
import jp.developer.bbee.englishmemory.domain.usecase.GetRecentUseCase
import jp.developer.bbee.englishmemory.domain.usecase.GetStudyDataUseCase
import jp.developer.bbee.englishmemory.domain.usecase.UpdateRecentUseCase
import jp.developer.bbee.englishmemory.domain.usecase.UpdateStudyStatusUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class StudyState(
    val studyData: List<StudyData>? = null,
    val questionData: StudyData? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    fun updateData(old: StudyData, new: StudyData): List<StudyData>? {
        if (this.studyData == null || old.english != new.english || old.wordType != new.wordType) {
            return this.studyData
        }

        val newStudyData = mutableListOf<StudyData>()
        newStudyData.addAll(this.studyData)
        newStudyData.remove(old)
        newStudyData.add(new)
        return newStudyData
    }
}

@HiltViewModel
class StudyViewModel @Inject constructor(
    getStudyDataUseCase: GetStudyDataUseCase,
    getRecentUseCase: GetRecentUseCase,
    private val updateRecentUseCase: UpdateRecentUseCase,
    private val updateStudyStatusUseCase: UpdateStudyStatusUseCase,
    private val addHistoryUseCase: AddHistoryUseCase,
) : ViewModel() {
    private var _state = MutableStateFlow(StudyState(isLoading = true))
    val state = _state.asStateFlow()

    private val _favoriteFlow = MutableSharedFlow<Boolean>()

    var isShowCorrect by mutableStateOf(false)

    val recent = getRecentUseCase().distinctUntilChanged()
        .onEach {
            recentData = it
        }

    private var recentData: List<Recent> = emptyList()

    init {
        _state.value = StudyState(isLoading = true)

        getStudyDataUseCase()
            .take(1)
            .onEach { studyDataList ->
                studyDataList.let {
                    val question = getQuestion(it)
                    if (question != null) {
                        _state.value = StudyState(
                            studyData = it,
                            questionData = question,
                        )
                    } else {
                        _state.value = StudyState(error = "出題できる問題がありません")
                    }
                }
            }.launchIn(viewModelScope)

        _favoriteFlow
            .onEach { isFavorite ->
                _state.value.questionData?.let {
                    _state.value = _state.value.copy(
                        questionData = it.copy(isFavorite = isFavorite)
                    )
                }
            }.launchIn(viewModelScope)
    }

    // 出題
    private fun getQuestion(data: List<StudyData>?): StudyData? {
        return data?.random()
    }

    fun updateStudyStatus(correct: Boolean) {
        state.value.questionData?.let {
            updateRecent(it, correct)

            val updateNumberOfQuestion = it.numberOfQuestion + 1
            val updateCountCorrect = it.countCorrect + (if (correct) 1 else 0)
            val updateCountMiss = it.countMiss + (if (!correct) 1 else 0)
            val updateScoreRate = if (updateNumberOfQuestion > 0)
                updateCountCorrect.toDouble() / updateNumberOfQuestion else 0.0

            val updated = it.copy(
                numberOfQuestion = updateNumberOfQuestion,
                countCorrect = updateCountCorrect,
                countMiss = updateCountMiss,
                scoreRate = updateScoreRate,
                isLatestAnswerCorrect = correct,
            )
            val newStudyData = _state.value.updateData(it, updated)

            // 正解表示をOFF、今回の問題の結果を更新し、次の問題を表示
            isShowCorrect = false
            _state.value = StudyState(
                studyData = newStudyData,
                questionData = getQuestion(newStudyData)
            )

            // 前回の問題の結果をDBに保存
            viewModelScope.launch {
                updateStudyStatusUseCase(updated.toStudyStatus())
            }
        }
    }

    private fun updateRecent(studyData: StudyData, correct: Boolean) {
        val history = History(
            studyDate = LocalDateTime.now(),
            english = studyData.english,
            wordType = studyData.wordType,
            correct = correct,
        )
        viewModelScope.launch {
            addHistoryUseCase(history)
        }

        // TODO RecentはHistoryを最新10件を取得すれば不要になる
        val addRecent = Recent(studyData, correct)
        addRecent.updateRecentList(recentData).let {
            viewModelScope.launch {
                updateRecentUseCase(it)
            }
        }
    }

    fun updateBookmarkStudyStatus(isFavorite: Boolean) {
        state.value.questionData?.let {
            viewModelScope.launch {
                val studyStatus = it.toStudyStatus().copy(isFavorite = isFavorite)
                updateStudyStatusUseCase(studyStatus)
                _favoriteFlow.emit(studyStatus.isFavorite)
            }
        }
    }
}