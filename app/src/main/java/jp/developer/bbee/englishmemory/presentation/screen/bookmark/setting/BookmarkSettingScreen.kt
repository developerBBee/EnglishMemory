package jp.developer.bbee.englishmemory.presentation.screen.bookmark.setting

import android.util.Log
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import jp.developer.bbee.englishmemory.R
import jp.developer.bbee.englishmemory.domain.model.BookmarkKey
import jp.developer.bbee.englishmemory.presentation.components.modal.CustomScaffold

@Composable
fun BookmarkSettingScreen(
    navController: NavController,
    viewModel: BookmarkSettingViewModel = hiltViewModel()
) {
    val bookmarkSettingState by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = bookmarkSettingState.isLoading
    /*TODO エラー表示*/

    BackHandler(enabled = !isLoading) { back(navController, viewModel) }

    val title = "Bookmark Setting"
    CustomScaffold(
        title = title,
        navController = navController,
        onClose = { if (!isLoading) back(navController, viewModel) },
        drawerOpenEnabled = false
    ) {
        BookmarkSettingContent(
            bookmarkSettingState = bookmarkSettingState,
            onSearchChanged = { w -> viewModel.updatePreferences(word = w) },
            onCheckedChange = { k, v -> viewModel.updatePreferences(key = k, value = v) },
        )
    }
}

@Composable
fun BookmarkSettingContent(
    bookmarkSettingState: BookmarkSettingState,
    onSearchChanged: (String) -> Unit,
    onCheckedChange: (BookmarkKey, Boolean) -> Unit,
) {
    val isLoading = bookmarkSettingState.isLoading
    val prefs = bookmarkSettingState.preferences
    val searchWord = prefs?.searchWord ?: ""
    val checkedMap = prefs?.bookmarkBooleans ?: emptyMap()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    if (prefs == null) {
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "検索"
                    )
                    Text(
                        text = "英単語検索",
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
                    .padding(8.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_outline_filter_alt_24),
                        contentDescription = "フィルター"
                    )
                    Text(
                        text = "重要単語ランク",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    BookmarkKey.IMPORTANCE_RANK_A.PreferenceByKey(checkedMap, onCheckedChange)
                    BookmarkKey.IMPORTANCE_RANK_B.PreferenceByKey(checkedMap, onCheckedChange)
                    BookmarkKey.IMPORTANCE_RANK_C.PreferenceByKey(checkedMap, onCheckedChange)
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_outline_filter_alt_24),
                        contentDescription = "フィルター"
                    )
                    Text(
                        text = "品詞",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    BookmarkKey.WORD_TYPE_NOUN.PreferenceByKey(checkedMap, onCheckedChange)
                    BookmarkKey.WORD_TYPE_PRONOUN.PreferenceByKey(checkedMap, onCheckedChange)
                    BookmarkKey.WORD_TYPE_VERB.PreferenceByKey(checkedMap, onCheckedChange)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    BookmarkKey.WORD_TYPE_ADJECTIVE.PreferenceByKey(checkedMap, onCheckedChange)
                    BookmarkKey.WORD_TYPE_ADVERB.PreferenceByKey(checkedMap, onCheckedChange)
                    BookmarkKey.WORD_TYPE_AUXILIARY_VERB.PreferenceByKey(checkedMap, onCheckedChange)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    BookmarkKey.WORD_TYPE_PREPOSITION.PreferenceByKey(checkedMap, onCheckedChange)
                    BookmarkKey.WORD_TYPE_INTERJECTION.PreferenceByKey(checkedMap, onCheckedChange)
                    BookmarkKey.WORD_TYPE_CONJUNCTION.PreferenceByKey(checkedMap, onCheckedChange)
                }
            }
        }
    }
}

@Composable
fun TextAndCheckbox(
    key: BookmarkKey,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
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
internal fun BookmarkKey.PreferenceByKey(
    checkedMap: Map<BookmarkKey, Boolean>,
    onCheckedChange: (BookmarkKey, Boolean) -> Unit,
) {
    TextAndCheckbox(
        key = this,
        checked = checkedMap.getOrDefault(this, true),
    ) { newChecked ->
        onCheckedChange(this, newChecked)
    }
}

private fun back(
    navController: NavController,
    viewModel: BookmarkSettingViewModel,
) {
    Log.d("BookmarkSettingScreen", "back")
    viewModel.saveBookmarkPreferences()
    navController.popBackStack()
}