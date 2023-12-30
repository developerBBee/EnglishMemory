package jp.developer.bbee.englishmemory.presentation.screen.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.developer.bbee.englishmemory.common.response.Async
import jp.developer.bbee.englishmemory.domain.model.BookmarkPreferences
import jp.developer.bbee.englishmemory.domain.model.BookmarkType
import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.domain.usecase.GetBookmarkPreferenceUseCase
import jp.developer.bbee.englishmemory.domain.usecase.GetStudyDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class BookmarkState(
    val studyData: List<StudyData>? = null,
    val listParams: ListParams = ListParams(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class ListParams(
    val filter: Filters = Filters(),
    val search: String = "",
)

data class Filters(
    val importance: Set<String>? = null,
    val wordType: Set<String>? = null,
    val isFavorite: Boolean = true,
)

enum class SortType(var order: Order = Order.ASC) {
    ENGLISH,
    NUMBER_OF_QUESTION,
    SCORE_RATE,
    COUNT_MISS,
    COUNT_CORRECT,
}

enum class Order {
    NONE,
    ASC,
    DESC,
}

val SORT_TYPE_PRIORITIES = listOf(
    SortType.ENGLISH,
    SortType.NUMBER_OF_QUESTION,
    SortType.SCORE_RATE,
    SortType.COUNT_MISS,
    SortType.COUNT_CORRECT,
)

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    getStudyDataUseCase: GetStudyDataUseCase,
    getBookmarkPreferenceUseCase: GetBookmarkPreferenceUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BookmarkState())
    val state: StateFlow<BookmarkState> get() = _state

    init {
        combine(
            getStudyDataUseCase(),
            getBookmarkPreferenceUseCase()
        ) { studyData, prefs ->
            applyPreferences(prefs)
            studyData
        }.onEach {
                when(it) {
                    is Async.Loading -> {
                        _state.value = BookmarkState(isLoading = true)
                    }
                    is Async.Success -> {
                        _state.value = BookmarkState(
                            studyData = it.data?.filterAndSort(_state.value.listParams)
                        )
                    }
                    is Async.Failure -> {
                        _state.value = BookmarkState(error = it.error)
                    }
                }
            }.launchIn(viewModelScope)

        getBookmarkPreferenceUseCase()
            .onEach {

            }.launchIn(viewModelScope)
    }

    fun setOrder(sortType: SortType, order: Order) {
        /* TODO 降順昇順の追加 */
        sortType.order = order
    }

    private fun applyPreferences(prefs: BookmarkPreferences) {
        val word = prefs.searchWord
        val importance = prefs.bookmarkBooleans.filter { it.key.type == BookmarkType.IMPORTANCE }
            .filter { it.value }.map { it.key.displayName }.toSet()
        val wordType = prefs.bookmarkBooleans.filter { it.key.type == BookmarkType.WORD_TYPE }
            .filter { it.value }.map { it.key.displayName }.toSet()

        _state.value = state.value.copy(
            listParams = ListParams(
                filter = Filters(
                    importance = importance,
                    wordType = wordType,
                ),
                search = word,
            )
        )
    }
}

internal fun List<StudyData>.filterAndSort(listParams: ListParams): List<StudyData> {
    return this.filterByParams(listParams).sort()
}

internal fun List<StudyData>.filterByParams(listParams: ListParams): List<StudyData> {
    return this.filter {
        it.english.contains(listParams.search)
                && listParams.filter.importance?.contains(it.importance) ?: true
                && listParams.filter.wordType?.contains(it.wordType) ?: true
                && listParams.filter.isFavorite == it.isFavorite
    }
}

internal fun List<StudyData>.sort(): List<StudyData> {
    var newStudyDataList = this
    SORT_TYPE_PRIORITIES.forEach {
        newStudyDataList = newStudyDataList.sort(it)
    }
    return newStudyDataList
}

internal fun List<StudyData>.sort(sortType: SortType): List<StudyData> {
    val order = sortType.order

    return when(sortType) {
        SortType.ENGLISH -> {
            this.orderBy(order = order) { it.english }
        }
        SortType.NUMBER_OF_QUESTION -> {
            this.orderBy(order = order) { it.numberOfQuestion }
        }
        SortType.SCORE_RATE -> {
            this.orderBy(order = order) { it.scoreRate }
        }
        SortType.COUNT_MISS -> {
            this.orderBy(order = order) { it.countMiss }
        }
        SortType.COUNT_CORRECT -> {
            this.orderBy(order = order) { it.countCorrect }
        }
    }
}

internal fun <T : Comparable<T>> List<StudyData>.orderBy(
    order: Order,
    selector: (StudyData) -> T?
): List<StudyData> {
    return when (order) {
        Order.NONE ->
            this
        Order.ASC ->
            this.sortedBy(selector)
        Order.DESC ->
            this.sortedByDescending(selector)
    }
}