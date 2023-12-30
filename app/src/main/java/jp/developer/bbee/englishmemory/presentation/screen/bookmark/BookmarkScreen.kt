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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import jp.developer.bbee.englishmemory.presentation.ScreenRoute
import jp.developer.bbee.englishmemory.presentation.components.modal.CustomScaffold

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
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        text = "検索",
                        style = MaterialTheme.typography.titleLarge,
                    )
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
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(2f),
                        style = MaterialTheme.typography.titleLarge,
                        text = "${it.english} [${it.wordType}]"
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.titleLarge,
                        text = "正答率 ${it.scoreRate}"
                    )
                    IconButton(onClick = { /*TODO ブックマーク解除処理*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "ブックマーク",
                            tint = Color.Yellow,
                        )
                    }
                }
            }
        }
    }
}
