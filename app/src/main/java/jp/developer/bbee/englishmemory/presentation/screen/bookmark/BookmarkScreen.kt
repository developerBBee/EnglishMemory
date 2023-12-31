package jp.developer.bbee.englishmemory.presentation.screen.bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import jp.developer.bbee.englishmemory.presentation.ScreenRoute
import jp.developer.bbee.englishmemory.presentation.components.modal.CustomScaffold
import kotlin.math.round

@Composable
fun BookmarkScreen(
    navController: NavController,
    viewModel: BookmarkViewModel = hiltViewModel()
) {
    val bookmarkState by viewModel.state.collectAsStateWithLifecycle()
    /*TODO エラー表示*/

    val drawerOpenEnabled = !bookmarkState.isLoading && bookmarkState.error.isNullOrBlank()

    val title = "Bookmark"
    CustomScaffold(
        title = title,
        navController = navController,
        drawerOpenEnabled = drawerOpenEnabled
    ) {
        BookmarkContent(
            navController = navController,
            bookmarkState = bookmarkState
        )
    }
}

@Composable
fun BookmarkContent(
    navController: NavController,
    bookmarkState: BookmarkState,
) {
    val studyDataList = bookmarkState.studyData ?: emptyList()

    Box(modifier = Modifier.fillMaxWidth()) {
        LazyColumn {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        navController.navigate(ScreenRoute.BookmarkSettingScreen.route)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "検索、フィルター、ソート",
                        )
                    }
                }
            }

            items(studyDataList) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(2f),
                        style = MaterialTheme.typography.titleLarge,
                        text = "[${it.wordType}] ${it.english}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            style = MaterialTheme.typography.titleMedium,
                            text = "正答率"
                        )
                        Text(
                            style = MaterialTheme.typography.titleLarge,
                            text = "${round(it.scoreRate * 1000) / 10}%"
                        )
                    }
                }
            }
        }
    }
}
