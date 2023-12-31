package jp.developer.bbee.englishmemory.presentation.screen.history

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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import jp.developer.bbee.englishmemory.common.constant.DATE_JP_FORMATTER
import jp.developer.bbee.englishmemory.common.constant.TIME_JP_FORMATTER
import jp.developer.bbee.englishmemory.domain.model.History
import jp.developer.bbee.englishmemory.presentation.ScreenRoute
import jp.developer.bbee.englishmemory.presentation.components.dialog.StudyDataDialog
import jp.developer.bbee.englishmemory.presentation.components.modal.CustomScaffold

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val historyState by viewModel.state.collectAsStateWithLifecycle()

    val drawerOpenEnabled = !historyState.isLoading && historyState.error.isNullOrBlank()
    val title = "History"
    CustomScaffold(
        title = title,
        navController = navController,
        drawerOpenEnabled = drawerOpenEnabled
    ) {
        HistoryContent(
            navController = navController,
            historyState = historyState,
            onItemClick = { history -> viewModel.selectHistory(history) },
        )
    }

    // Show translate data on dialog for selected history
    historyState.selectedStudyData?.let { selectedData ->
        StudyDataDialog(
            studyData = selectedData,
            onDismiss = { viewModel.unSelectHistory() },
            onClickBookmark = { bookmark ->
                viewModel.updateStudyStatus(selectedData.copy(isFavorite = bookmark))
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryContent(
    navController: NavController,
    historyState: HistoryState,
    onItemClick: (History) -> Unit,
) {
    val historyData = historyState.historyOneWeek ?: emptyList()

    Box(modifier = Modifier.fillMaxWidth()) {
        // Learning history list with date-based sticky headers
        LazyColumn {
            val groupedData = historyData
                .sortedByDescending { it.studyDate }
                .groupBy { it.studyDate.toLocalDate() }

            groupedData.forEach { (date, histories) ->
                stickyHeader {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.surface)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = date.format(DATE_JP_FORMATTER),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                }

                items(histories) { history ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable(true) { onItemClick(history) },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = history.studyDate.format(TIME_JP_FORMATTER),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            text = "${history.english}  [${history.wordType}]",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        CorrectOrNotIcon(isCorrect = history.correct)
                    }
                }
            }
        }

        if (!historyState.error.isNullOrBlank()) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    TextButton(
                        onClick = { navController.navigate(ScreenRoute.TopScreen.route) }) {
                        Text(text = "トップに戻る")
                    }
                },
                text = { Text(text = historyState.error) }
            )
        } else if (historyState.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun CorrectOrNotIcon(
    isCorrect: Boolean
) {
    if (isCorrect) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = "正解",
            tint = Color.Green
        )
    } else {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "不正解",
            tint = Color.Red
        )
    }
}
