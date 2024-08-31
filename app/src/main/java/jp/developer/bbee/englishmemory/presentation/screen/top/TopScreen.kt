package jp.developer.bbee.englishmemory.presentation.screen.top

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import jp.developer.bbee.englishmemory.R
import jp.developer.bbee.englishmemory.presentation.ScreenRoute
import jp.developer.bbee.englishmemory.presentation.components.modal.CustomScaffold
import jp.developer.bbee.englishmemory.presentation.ui.theme.AppTheme

@Composable
fun TopScreen(
    navController: NavController,
    viewModel: TopViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val drawerOpenEnabled = !state.isLoading && state.error.isNullOrBlank()

    val title = stringResource(id = R.string.top_title)
    CustomScaffold(
        title = title,
        navController = navController,
        drawerOpenEnabled = drawerOpenEnabled
    ) {
        TopContent(
            isLoading = state.isLoading,
            errorMessage = state.error,
            isReady = viewModel.isReady,
            isRestart = viewModel.isRestart,
            restart = viewModel::restart,
            navigate = navController::navigate,
        )
    }

    LaunchedEffect(key1 = viewModel.isRestart) {
        if (viewModel.isRestart) {
            navController.navigate(ScreenRoute.StartApp.route) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }
    }
}
@Composable
fun TopContent(
    isLoading: Boolean,
    errorMessage: String?,
    isReady: Boolean,
    isRestart: Boolean,
    restart: () -> Unit,
    navigate: (String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.Center,
            ) {
                val mainLogo = painterResource(id = R.drawable.main_logo)
                Image(
                    painter = mainLogo,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(AppTheme.dimens.large)),
                    contentDescription = stringResource(id = R.string.top_main_logo),
                    contentScale = ContentScale.FillWidth
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                val study = stringResource(id = R.string.top_study)
                val history = stringResource(id = R.string.top_history)
                val score = stringResource(id = R.string.top_score)
                val bookmark = stringResource(id = R.string.top_bookmark)

                MenuTextButton(
                    text = study,
                    style = MaterialTheme.typography.headlineLarge,
                    enabled = isReady
                ) {
                    navigate(ScreenRoute.StudyScreen.route)
                }
                Spacer(modifier = Modifier.padding(AppTheme.dimens.small))
                MenuTextButton(text = history, enabled = isReady) {
                    navigate(ScreenRoute.HistoryScreen.route)
                }
                MenuTextButton(text = score, enabled = isReady) {
                    navigate(ScreenRoute.ScoreScreen.route)
                }
                MenuTextButton(text = bookmark, enabled = isReady) {
                    navigate(ScreenRoute.BookmarkScreen.route)
                }
                // TODO SettingScreen完成後に有効にする
//                    MenuTextButton(text = "Setting", enabled = isReady) {
//                        navigate(ScreenRoute.SettingScreen.route)
//                    }
            }
        }
        if (isRestart) {
            Box(modifier = Modifier.fillMaxSize())
        } else if (!errorMessage.isNullOrBlank()) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    TextButton(onClick = restart) {
                        Text(text = stringResource(id = R.string.top_restart))
                    }
                },
                text = { Text(text = errorMessage) }
            )
        } else if (isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun MenuTextButton(
    text: String,
    style: TextStyle = MaterialTheme.typography.headlineSmall,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
    ) {
        Text(
            text = text,
            style = style,
        )
    }
}
