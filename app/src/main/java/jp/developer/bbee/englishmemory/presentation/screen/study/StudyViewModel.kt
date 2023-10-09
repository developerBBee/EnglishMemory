package jp.developer.bbee.englishmemory.presentation.screen.study

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.developer.bbee.englishmemory.common.response.Async
import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.domain.usecase.GetStudyDataUseCase
import jp.developer.bbee.englishmemory.domain.usecase.UpdateStudyStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
    private val updateStudyStatusUseCase: UpdateStudyStatusUseCase,
) : ViewModel() {
    private var _state = MutableStateFlow(StudyState(isLoading = true))
    val state = _state.asStateFlow()

    var isShowCorrect by mutableStateOf(false)

    init {
        getStudyDataUseCase().onEach { response ->
            when (response) {
                is Async.Success -> {
                    response.data?.let {
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
                }

                is Async.Failure -> {
                    _state.value = StudyState(error = response.error)
                }

                is Async.Loading -> {
                    _state.value = StudyState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    // 出題
    private fun getQuestion(data: List<StudyData>?): StudyData? {
        return data?.random()
    }

    fun updateStudyStatus(correct: Boolean) {
        state.value.questionData?.let {
            val updateNumberOfQuestion = it.numberOfQuestion + 1
            val updateCountCorrect = it.countCorrect + (if (correct) 1 else 0)
            val updateCountMiss = it.countMiss + (if (!correct) 1 else 0)
            val updateScoreRate = if (updateNumberOfQuestion > 0)
                updateCountCorrect.toDouble() / updateNumberOfQuestion else 0.0

            val updated = it.updateStudyData(
                numberOfQuestion = updateNumberOfQuestion,
                countCorrect = updateCountCorrect,
                countMiss = updateCountMiss,
                scoreRate = updateScoreRate,
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
                updateStudyStatusUseCase(updated.getStudyStatus())
            }
        }
    }
}