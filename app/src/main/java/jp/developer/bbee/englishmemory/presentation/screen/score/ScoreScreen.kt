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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import jp.developer.bbee.englishmemory.R
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

    val title = stringResource(id = R.string.score_title)
    CustomScaffold(
        title = title,
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
    val titles = listOf(
        stringResource(id = R.string.score_tab_whole),
        stringResource(id = R.string.score_tab_week),
    )
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
                ScoreDetail(
                    wordType = stringResource(id = R.string.score_all_word_type),
                    score = scoreTotal
                )

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
        Text(text = stringResource(id = R.string.score_no_data))
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
                text = stringResource(id = R.string.score_number_of_questions),
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
                    text = stringResource(id = R.string.score_correct_answer_rate),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(id = R.string.score_percent, correctRate),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}