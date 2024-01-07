package jp.developer.bbee.englishmemory.presentation.screen.bookmark.setting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import jp.developer.bbee.englishmemory.R
import jp.developer.bbee.englishmemory.domain.model.FilterKey
import jp.developer.bbee.englishmemory.domain.model.Order
import jp.developer.bbee.englishmemory.domain.model.SearchKey
import jp.developer.bbee.englishmemory.domain.model.SortKey
import jp.developer.bbee.englishmemory.presentation.components.modal.CustomScaffold
import jp.developer.bbee.englishmemory.presentation.ui.theme.AppTheme

@Composable
fun BookmarkSettingScreen(
    navController: NavController,
    viewModel: BookmarkSettingViewModel = hiltViewModel()
) {
    val bookmarkSettingState by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = bookmarkSettingState.isLoading
    val isSaved = bookmarkSettingState.isSaved
    /*TODO エラー表示*/

    LaunchedEffect(isSaved) {
        if (isSaved) {
            navController.popBackStack()
        }
    }
    BackHandler(enabled = !isLoading) { viewModel.saveBookmarkPreferences() }

    val title = stringResource(id = R.string.bookmark_setting_title)
    CustomScaffold(
        title = title,
        navController = navController,
        onClose = { if (!isLoading) viewModel.saveBookmarkPreferences() },
        drawerOpenEnabled = false
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        BookmarkSettingContent(
            bookmarkSettingState = bookmarkSettingState,
            onSearchChanged = { w -> viewModel.updateSearchPreferences(word = w) },
            onCheckedChange = { k, v -> viewModel.updateFilterPreferences(key = k, value = v) },
            onToggleChange = { k, v -> viewModel.updateSortPreferences(key = k, value = v) },
        )
    }
}

@Composable
fun BookmarkSettingContent(
    bookmarkSettingState: BookmarkSettingState,
    onSearchChanged: (String) -> Unit,
    onCheckedChange: (FilterKey, Boolean) -> Unit,
    onToggleChange: (SortKey, Order) -> Unit,
) {
    val prefs = bookmarkSettingState.preferences
    val searchWord = prefs?.searchWord ?: ""
    val checkedMap = prefs?.bookmarkBooleans ?: emptyMap()
    val sortPrefs = prefs?.sortPrefs ?: emptyMap()

    if (prefs == null) {
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.small),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.bookmark_search),
                    )
                    Text(
                        text = SearchKey.SEARCH_WORD.displayName,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    OutlinedTextField(
                        value = searchWord,
                        onValueChange = { onSearchChanged(it) },
                        singleLine = true,
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.small),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_outline_filter_alt_24),
                        contentDescription = stringResource(id = R.string.bookmark_filter)
                    )
                    Text(
                        text = stringResource(id = R.string.bookmark_importance),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    FilterKey.IMPORTANCE_RANK_A.PreferenceCheckBox(checkedMap, onCheckedChange)
                    FilterKey.IMPORTANCE_RANK_B.PreferenceCheckBox(checkedMap, onCheckedChange)
                    FilterKey.IMPORTANCE_RANK_C.PreferenceCheckBox(checkedMap, onCheckedChange)
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.small),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_outline_filter_alt_24),
                        contentDescription = stringResource(id = R.string.bookmark_filter)
                    )
                    Text(
                        text = stringResource(id = R.string.bookmark_word_type),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    FilterKey.WORD_TYPE_NOUN.PreferenceCheckBox(checkedMap, onCheckedChange)
                    FilterKey.WORD_TYPE_PRONOUN.PreferenceCheckBox(checkedMap, onCheckedChange)
                    FilterKey.WORD_TYPE_VERB.PreferenceCheckBox(checkedMap, onCheckedChange)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    FilterKey.WORD_TYPE_ADJECTIVE.PreferenceCheckBox(checkedMap, onCheckedChange)
                    FilterKey.WORD_TYPE_ADVERB.PreferenceCheckBox(checkedMap, onCheckedChange)
                    FilterKey.WORD_TYPE_AUXILIARY_VERB.PreferenceCheckBox(checkedMap, onCheckedChange)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    FilterKey.WORD_TYPE_PREPOSITION.PreferenceCheckBox(checkedMap, onCheckedChange)
                    FilterKey.WORD_TYPE_INTERJECTION.PreferenceCheckBox(checkedMap, onCheckedChange)
                    FilterKey.WORD_TYPE_CONJUNCTION.PreferenceCheckBox(checkedMap, onCheckedChange)
                }
            }
        }

        item { Divider() }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.small),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_sort_24),
                        contentDescription = stringResource(id = R.string.bookmark_sort)
                    )
                    Text(
                        text = stringResource(id = R.string.bookmark_order),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SortKey.ENGLISH.PreferenceSort(sortPrefs, onToggleChange)
                    SortKey.NUMBER_OF_QUESTION.PreferenceSort(sortPrefs, onToggleChange)
                    SortKey.SCORE_RATE.PreferenceSort(sortPrefs, onToggleChange)
                    SortKey.COUNT_MISS.PreferenceSort(sortPrefs, onToggleChange)
                    SortKey.COUNT_CORRECT.PreferenceSort(sortPrefs, onToggleChange)
                }
            }
        }
    }
}

@Composable
fun TextAndCheckbox(
    key: FilterKey,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(AppTheme.dimens.small))
            .clickable { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = key.displayName,
            style = MaterialTheme.typography.titleLarge,
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
internal fun FilterKey.PreferenceCheckBox(
    checkedMap: Map<FilterKey, Boolean>,
    onCheckedChange: (FilterKey, Boolean) -> Unit,
) {
    TextAndCheckbox(
        key = this,
        checked = checkedMap.getOrDefault(this, true),
    ) { newChecked ->
        onCheckedChange(this, newChecked)
    }
}

@Composable
fun TextAndToggleIcon(
    sortKey: SortKey,
    order: Order,
    onOrderChange: (Order) -> Unit,
) {
    val newOrder = order.next()

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(AppTheme.dimens.small))
            .clickable { onOrderChange(newOrder) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = sortKey.displayName,
            style = MaterialTheme.typography.titleLarge,
        )
        IconButton(onClick = { onOrderChange(newOrder) }) {
            when (order) {
                Order.NONE -> Icon(
                    painter = painterResource(R.drawable.ic_horizontal_rule_24),
                    contentDescription = stringResource(id = R.string.bookmark_no_order)
                )
                Order.ASC -> Icon(
                    painter = painterResource(R.drawable.ic_arrow_upward_24),
                    contentDescription = stringResource(id = R.string.bookmark_ascending_order)
                )
                Order.DESC -> Icon(
                    painter = painterResource(R.drawable.ic_arrow_downward_24),
                    contentDescription = stringResource(id = R.string.bookmark_descending_order)
                )
            }
        }
    }
}

@Composable
internal fun SortKey.PreferenceSort(
    sortPrefs: Map<SortKey, Order>,
    onToggleChange: (SortKey, Order) -> Unit,
) {
    TextAndToggleIcon(
        sortKey = this,
        order = checkNotNull(sortPrefs[this])
    ) { newOrder ->
        onToggleChange(this, newOrder)
    }
}

internal fun Order.next(): Order = when (this) {
    Order.NONE -> Order.ASC
    Order.ASC -> Order.DESC
    Order.DESC -> Order.NONE
}
