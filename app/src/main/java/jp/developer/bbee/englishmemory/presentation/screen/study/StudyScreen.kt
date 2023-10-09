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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import jp.developer.bbee.englishmemory.presentation.ScreenRoute.TopScreen
import jp.developer.bbee.englishmemory.presentation.ui.theme.AppTheme

@Composable
fun StudyScreen(
    navController: NavController,
    viewModel: StudyViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val error = state.error
    val question = state.questionData

    val isShowCorrect = viewModel.isShowCorrect

    Box(Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center))
        } else if (error != null || question == null) {
            AlertDialog(
                text = { Text(text = error?: "出題する問題がありません") },
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
                        .weight(1f),
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
                            text = "[${question.wordType}]",
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                }

                /* 回答表示 */
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isShowCorrect) {
                        Text(
                            text = lineBreakAdjustment(question.translateToJapanese),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (!isShowCorrect) {
                        Button(onClick = { viewModel.isShowCorrect = true }) {
                            Text(
                                text = "正解を表示する",
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                    } else {
                        Text(
                            text = "自己採点",
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Spacer(modifier = Modifier.padding(AppTheme.dimens.small))
                        Row {
                            Button(onClick = { viewModel.updateStudyStatus(true) }) {
                                Text(
                                    text = "正解",
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            }
                            Spacer(modifier = Modifier.padding(AppTheme.dimens.small))
                            Button(onClick = { viewModel.updateStudyStatus(false) }) {
                                Text(
                                    text = "不正解",
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
    var lines = 1
    splitStr.forEach {
        if (retStr.isNotEmpty()) {
            retStr += "、"
            if (retStr.length > 10 * lines) {
                retStr += "\n"
                lines++
            }
        }
        retStr += it
    }
    return retStr
}