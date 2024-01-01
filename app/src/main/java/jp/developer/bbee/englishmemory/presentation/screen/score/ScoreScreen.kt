package jp.developer.bbee.englishmemory.presentation.screen.score

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import jp.developer.bbee.englishmemory.presentation.components.modal.CustomScaffold
import jp.developer.bbee.englishmemory.presentation.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun ScoreScreen(
    navController: NavController,
    viewModel: ScoreViewModel = hiltViewModel(),
) {
    val scoreState by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = scoreState.isLoading
    val error = scoreState.error

    val drawerOpenEnabled = !isLoading && error.isNullOrBlank()

    CustomScaffold(
        title = "Your score",
        navController = navController,
        drawerOpenEnabled = drawerOpenEnabled
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            return@CustomScaffold
        }

        ScoreContent(
            scoreState = scoreState,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScoreContent(
    scoreState: ScoreState,
) {
    val scoresList = listOf(scoreState.scores, scoreState.scoresOneWeek)

    // pager params
    val titles = listOf("全期間", "直近１週間")
    val pagerState = rememberPagerState { titles.size }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }

        HorizontalPager(state = pagerState) { page ->
            val scores = scoresList[page]
            if (scores.isEmpty()) {
                NoDataPage()
                return@HorizontalPager
            }

            Column(modifier = Modifier.fillMaxSize()) {
                val scoreTotal = scores.values.reduce { acc, score -> acc + score }
                ScoreDetail(wordType = "全体", score = scoreTotal)

                Divider()

                LazyColumn {
                    items(scores.entries.toList()) { (key, score) ->
                        ScoreDetail(wordType = key.displayName, score = score)
                    }
                }
            }
        }
    }
}

@Composable
fun NoDataPage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "No data ...")
    }
}

@Composable
fun ScoreDetail(
    wordType: String,
    score: Score
) {
    val questionSum = score.questionSum
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppTheme.dimens.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = wordType,
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "出題回数：",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "$questionSum",
                style = MaterialTheme.typography.titleMedium
            )
        }
        if (questionSum > 0) {
            val correctRate = 100.0 * score.collectSum / questionSum
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "正解率：",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "%3.1f".format(correctRate) + "%",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}