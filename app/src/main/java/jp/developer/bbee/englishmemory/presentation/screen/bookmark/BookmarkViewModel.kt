package jp.developer.bbee.englishmemory.presentation.screen.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.developer.bbee.englishmemory.domain.model.BookmarkPreferences
import jp.developer.bbee.englishmemory.domain.model.FilterKeyType
import jp.developer.bbee.englishmemory.domain.model.Order
import jp.developer.bbee.englishmemory.domain.model.SortKey
import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.domain.usecase.GetBookmarkPreferenceUseCase
import jp.developer.bbee.englishmemory.domain.usecase.GetStudyDataUseCase
import jp.developer.bbee.englishmemory.domain.usecase.UpdateStudyStatusUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookmarkState(
    val studyData: List<StudyData>? = null,
    val selectedStudyData: StudyData? = null,
    val filterPrefs: FilterPrefs = FilterPrefs(),
    val sortPrefs: Map<SortKey, Order> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class FilterPrefs(
    val search: String = "",
    val filter: Filters = Filters(),
)

data class Filters(
    val importance: Set<String>? = null,
    val wordType: Set<String>? = null,
    val isFavorite: Boolean = true,
)

val SORT_TYPE_PRIORITIES = listOf(
    SortKey.COUNT_CORRECT,
    SortKey.COUNT_MISS,
    SortKey.SCORE_RATE,
    SortKey.NUMBER_OF_QUESTION,
    SortKey.ENGLISH,
)

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    getStudyDataUseCase: GetStudyDataUseCase,
    getBookmarkPreferenceUseCase: GetBookmarkPreferenceUseCase,
    private val updateStudyStatusUseCase: UpdateStudyStatusUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(BookmarkState())
    val state: StateFlow<BookmarkState> get() = _state

    private val _selectedDataFlow = MutableSharedFlow<StudyData?>(1)
    private val _favoriteFlow = MutableStateFlow<Boolean?>(null)

    init {
        _state.value = BookmarkState(isLoading = true)
        unSelectStudyData()

        combine(
            getStudyDataUseCase(),
            getBookmarkPreferenceUseCase(),
            _selectedDataFlow
        ) { dataList, prefs, selectedData ->
            applyPreferences(prefs)
            dataList to selectedData
        }.onEach { (dataList, selectedData) ->
            val selectedStudyData = selectedData?.let { selected ->
                dataList.firstOrNull { studyData ->
                    studyData.english == selected.english
                            && studyData.wordType == selected.wordType
                }
            }

            _state.value = _state.value.copy(
                isLoading = false,
                studyData = dataList.filterAndSort(
                    filterPrefs = _state.value.filterPrefs,
                    sortPrefs = _state.value.sortPrefs
                ),
                selectedStudyData = selectedStudyData,
            )
        }.launchIn(viewModelScope)
    }

    private fun applyPreferences(prefs: BookmarkPreferences) {
        val word = prefs.searchWord
        val importance = prefs.bookmarkBooleans.filter { it.key.type == FilterKeyType.IMPORTANCE }
            .filter { it.value }.map { it.key.displayName }.toSet()
        val wordType = prefs.bookmarkBooleans.filter { it.key.type == FilterKeyType.WORD_TYPE }
            .filter { it.value }.map { it.key.displayName }.toSet()

        _state.value = state.value.copy(
            filterPrefs = FilterPrefs(
                filter = Filters(
                    importance = importance,
                    wordType = wordType,
                ),
                search = word,
            ),
            sortPrefs = prefs.sortPrefs
        )
    }

    fun updateStudyStatus(studyData: StudyData) {
        val studyStatus = studyData.toStudyStatus()
        viewModelScope.launch {
            updateStudyStatusUseCase(studyStatus)
            _favoriteFlow.value = studyStatus.isFavorite
        }
    }

    fun selectStudyData(studyData: StudyData) {
        viewModelScope.launch {
            _selectedDataFlow.emit(studyData)
        }
    }

    fun unSelectStudyData() {
        viewModelScope.launch {
            _selectedDataFlow.emit(null)
        }
    }
}

internal fun List<StudyData>.filterAndSort(
    filterPrefs: FilterPrefs,
    sortPrefs: Map<SortKey, Order>,
): List<StudyData> {
    return this.filterByParams(filterPrefs).sort(sortPrefs)
}

internal fun List<StudyData>.filterByParams(filterPrefs: FilterPrefs): List<StudyData> {
    return this.filter {
        it.english.contains(filterPrefs.search)
                && filterPrefs.filter.importance?.contains(it.importance) ?: true
                && filterPrefs.filter.wordType?.contains(it.wordType) ?: true
                && filterPrefs.filter.isFavorite == it.isFavorite
    }
}

internal fun List<StudyData>.sort(sortPrefs: Map<SortKey, Order>): List<StudyData> {
    var newStudyDataList = this
    SORT_TYPE_PRIORITIES.forEach {
        newStudyDataList = newStudyDataList.sort(
            sortKey = it,
            order = checkNotNull(sortPrefs[it])
        )
    }
    return newStudyDataList
}

internal fun List<StudyData>.sort(
    sortKey: SortKey,
    order: Order
): List<StudyData> = when(sortKey) {
    SortKey.ENGLISH -> {
        this.orderBy(order = order) { it.english }
    }
    SortKey.NUMBER_OF_QUESTION -> {
        this.orderBy(order = order) { it.numberOfQuestion }
    }
    SortKey.SCORE_RATE -> {
        this.orderBy(order = order) { it.scoreRate }
    }
    SortKey.COUNT_MISS -> {
        this.orderBy(order = order) { it.countMiss }
    }
    SortKey.COUNT_CORRECT -> {
        this.orderBy(order = order) { it.countCorrect }
    }
}

internal fun <T : Comparable<T>> List<StudyData>.orderBy(
    order: Order,
    selector: (StudyData) -> T?
): List<StudyData> {
    return when (order) {
        Order.NONE -> this
        Order.ASC -> this.sortedBy(selector)
        Order.DESC -> this.sortedByDescending(selector)
    }
}