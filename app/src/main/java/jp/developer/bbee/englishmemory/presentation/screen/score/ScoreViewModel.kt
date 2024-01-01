package jp.developer.bbee.englishmemory.presentation.screen.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.developer.bbee.englishmemory.common.constant.DATETIME_FORMATTER
import jp.developer.bbee.englishmemory.domain.model.FilterKey
import jp.developer.bbee.englishmemory.domain.usecase.GetHistoryUseCase
import jp.developer.bbee.englishmemory.domain.usecase.GetStudyDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import javax.inject.Inject

data class ScoreState(
    val isLoading: Boolean = false,
    val scores: Map<FilterKey, Score> = emptyMap(),
    val scoresOneWeek: Map<FilterKey, Score> = emptyMap(),
    val error: String? = null,
)

data class Score(
    val questionSum: Int,
    val collectSum: Int,
    val missSum: Int,
) {
    operator fun plus(other: Score): Score = Score(
        questionSum = questionSum + other.questionSum,
        collectSum = collectSum + other.collectSum,
        missSum = missSum + other.missSum,
    )
}

@HiltViewModel
class ScoreViewModel @Inject constructor(
    getStudyDataUseCase: GetStudyDataUseCase,
    getHistoryUseCase: GetHistoryUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ScoreState())
    val state get() = _state

    init {
        _state.value = ScoreState(isLoading = true)

        combine(
            getStudyDataUseCase(),
            getHistoryUseCase(LocalDateTime.now().minusDays(8).format(DATETIME_FORMATTER)),
        ) { studyDataList, histories ->
            val scores = studyDataList.groupBy { studyData ->
                checkNotNull(FilterKey.fromDisplayName(studyData.wordType))
            }.filter { (_, groupedList) ->
                groupedList.sumOf { data -> data.numberOfQuestion } > 0
            }.mapValues { (_, groupedList) ->
                Score(
                    questionSum = groupedList.sumOf { data -> data.numberOfQuestion },
                    collectSum = groupedList.sumOf { data -> data.countCorrect },
                    missSum = groupedList.sumOf { data -> data.countMiss },
                )
            }.toSortedMap(compareBy { key -> key.ordinal })

            val scoresOneWeek = histories.groupBy { history ->
                checkNotNull(FilterKey.fromDisplayName(history.wordType))
            }.mapValues { (_, groupedList) ->
                val questionSum = groupedList.count()
                val collectSum = groupedList.count { data -> data.correct }
                Score(
                    questionSum = questionSum,
                    collectSum = collectSum,
                    missSum = questionSum - collectSum,
                )
            }.toSortedMap(compareBy { key -> key.ordinal })

            scores to scoresOneWeek
        }
        .onEach { (scores, scoresOneWeek) ->
            _state.value = _state.value.copy(
                isLoading = false,
                scores = scores,
                scoresOneWeek = scoresOneWeek,
                error = null,
            )
        }
        .catch {
            _state.value = _state.value.copy(
                isLoading = false,
                error = it.message,
            )
        }
        .launchIn(viewModelScope)
    }
}