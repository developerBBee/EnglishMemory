package jp.developer.bbee.englishmemory.presentation.screen.study

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import jp.developer.bbee.englishmemory.R
import jp.developer.bbee.englishmemory.presentation.ScreenRoute.TopScreen
import jp.developer.bbee.englishmemory.presentation.components.icon.BookmarkOrNotIcon
import jp.developer.bbee.englishmemory.presentation.components.modal.CustomScaffold
import jp.developer.bbee.englishmemory.presentation.ui.theme.AppTheme

@Composable
fun StudyScreen(
    navController: NavController,
    viewModel: StudyViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val drawerOpenEnabled = !state.isLoading && state.error.isNullOrBlank()

    val title = stringResource(id = R.string.study_title)
    CustomScaffold(
        title = title,
        navController = navController,
        drawerOpenEnabled = drawerOpenEnabled
    ) {
        StudyContent(
            state = state,
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
fun StudyContent(
    state: StudyState,
    navController: NavController,
    viewModel: StudyViewModel,
) {
    val error = state.error
    val question = state.questionData

    val isShowCorrect = viewModel.isShowCorrect
    val uriHandler = LocalUriHandler.current

    Box(Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center))
        } else if (error != null || question == null) {
            AlertDialog(
                text = { Text(text = error?: stringResource(id = R.string.study_no_question)) },
                onDismissRequest = { navController.navigate(TopScreen.route) },
                confirmButton = { navController.navigate(TopScreen.route) }
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                /* 出題英単語 */
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(3f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = question.english,
                            style = MaterialTheme.typography.displayLarge
                        )
                        Spacer(modifier = Modifier.padding(AppTheme.dimens.small))
                        Text(
                            text = stringResource(id = R.string.study_word_type, question.wordType),
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                }

                /* 回答表示 */
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(5f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isShowCorrect) {
                        Text(
                            text = lineBreakAdjustment(question.translateToJapanese),
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Spacer(modifier = Modifier.padding(AppTheme.dimens.small))
                        val url = stringResource(id = R.string.study_weblio_url, question.english)
                        TextButton(onClick = { uriHandler.openUri(uri = url) }) {
                            Text(
                                text = stringResource(id = R.string.study_weblio),
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                        Spacer(modifier = Modifier.padding(AppTheme.dimens.small))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = stringResource(id = R.string.study_bookmark),
                                style = MaterialTheme.typography.titleMedium,
                            )
                            BookmarkOrNotIcon(
                                isBookmarked = question.isFavorite,
                                onClickBookmark = { viewModel.updateBookmarkStudyStatus(it) }
                            )
                        }
                    }
                }

                // ボタン
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (!isShowCorrect) {
                        Button(onClick = { viewModel.isShowCorrect = true }) {
                            Text(
                                text = stringResource(id = R.string.study_show_answer),
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(id = R.string.study_self_grading),
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Spacer(modifier = Modifier.padding(AppTheme.dimens.small))
                        Row {
                            Button(onClick = { viewModel.updateStudyStatus(true) }) {
                                Text(
                                    text = stringResource(id = R.string.study_correct),
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            }
                            Spacer(modifier = Modifier.padding(AppTheme.dimens.small))
                            Button(onClick = { viewModel.updateStudyStatus(false) }) {
                                Text(
                                    text = stringResource(id = R.string.study_incorrect),
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun lineBreakAdjustment(str: String): String {
    val splitStr = str.split("、")
    var retStr = ""
    var lineWords = 0
    splitStr.forEach {
        if (retStr.isNotEmpty()) {
            retStr += "、"
            lineWords += 1
        }

        // ワードを追加した場合に1行で15文字以上の場合は改行する
        lineWords += it.length
        if (lineWords >= 15) {
            retStr += "\n"
            lineWords = it.length
        }
        retStr += it
    }
    return retStr
}