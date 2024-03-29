package jp.developer.bbee.englishmemory.presentation.screen.bookmark

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import jp.developer.bbee.englishmemory.R
import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.presentation.ScreenRoute
import jp.developer.bbee.englishmemory.presentation.components.dialog.StudyDataDialog
import jp.developer.bbee.englishmemory.presentation.components.modal.CustomScaffold
import jp.developer.bbee.englishmemory.presentation.extentiions.noRippleClickable
import jp.developer.bbee.englishmemory.presentation.ui.theme.AppTheme

@Composable
fun BookmarkScreen(
    navController: NavController,
    viewModel: BookmarkViewModel = hiltViewModel()
) {
    val bookmarkState by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = bookmarkState.isLoading
    val error = bookmarkState.error
    /*TODO エラー表示*/

    val drawerOpenEnabled = !isLoading && error.isNullOrBlank()

    val title = stringResource(id = R.string.bookmark_title)
    CustomScaffold(
        title = title,
        navController = navController,
        drawerOpenEnabled = drawerOpenEnabled
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        BookmarkContent(
            navController = navController,
            bookmarkState = bookmarkState,
            onItemClick = { studyData -> viewModel.selectStudyData(studyData) },
        )
    }

    // Show translate data on dialog for selected history
    bookmarkState.selectedStudyData?.let { selectedData ->
        StudyDataDialog(
            studyData = selectedData,
            onDismiss = { viewModel.unSelectStudyData() },
            onClickBookmark = { bookmark ->
                viewModel.updateStudyStatus(selectedData.copy(isFavorite = bookmark))
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookmarkContent(
    navController: NavController,
    bookmarkState: BookmarkState,
    onItemClick: (StudyData) -> Unit,
) {
    val studyDataList = bookmarkState.studyData ?: emptyList()

    Box(modifier = Modifier.fillMaxWidth()) {
        LazyColumn {
            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(vertical = AppTheme.dimens.small)
                        .noRippleClickable {},
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        navController.navigate(ScreenRoute.BookmarkSettingScreen.route)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(id = R.string.bookmark_search),
                        )
                    }
                }
            }

            items(studyDataList) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.dimens.small)
                        .clickable { onItemClick(it) },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(2f),
                        style = MaterialTheme.typography.titleLarge,
                        text = stringResource(id = R.string.bookmark_word, it.wordType, it.english),
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
                            text = stringResource(id = R.string.bookmark_rate)
                        )
                        Text(
                            style = MaterialTheme.typography.titleLarge,
                            text = stringResource(id = R.string.bookmark_percent, it.scoreRate * 100)
                        )
                    }
                }
            }
        }
    }
}
