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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import jp.developer.bbee.englishmemory.R
import jp.developer.bbee.englishmemory.domain.model.TranslateData
import jp.developer.bbee.englishmemory.presentation.ScreenRoute
import jp.developer.bbee.englishmemory.presentation.components.modal.CustomScaffold
import jp.developer.bbee.englishmemory.presentation.ui.theme.AppTheme

const val DEBUG = false

@Composable
fun TopScreen(
    navController: NavController,
    viewModel: TopViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val drawerOpenEnabled = !state.isLoading && state.error.isNullOrBlank()

    CustomScaffold(
        title = "Memorizing English Words",
        navController = navController,
        drawerOpenEnabled = drawerOpenEnabled
    ) {
        TopContent(
            state = state,
            navController = navController,
            viewModel = viewModel
        )
    }
}
@Composable
fun TopContent(
    state: TopState,
    navController: NavController,
    viewModel: TopViewModel,
) {
    val dataList = state.translateData
    val errorMessage = state.error

    Box(modifier = Modifier.fillMaxSize()) {
        if (DEBUG) {
            DebugView(state = state, dataList = dataList, viewModel = viewModel)
        } else {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(3f),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    val mainLogo = painterResource(id = R.drawable.main_logo)
                    Image(
                        painter = mainLogo,
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = "アプリイメージロゴ",
                        contentScale = ContentScale.Fit
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    MenuTextButton(text = "Start", enabled = viewModel.isReady) {
                        navController.navigate(ScreenRoute.StudyScreen.route)
                    }
                    MenuTextButton(text = "History", enabled = viewModel.isReady) {
                        navController.navigate(ScreenRoute.HistoryScreen.route)
                    }
                    MenuTextButton(text = "Score", enabled = viewModel.isReady) {
                        navController.navigate(ScreenRoute.ScoreScreen.route)
                    }
                    MenuTextButton(text = "Bookmark", enabled = viewModel.isReady) {
                        navController.navigate(ScreenRoute.BookmarkScreen.route)
                    }
                    // TODO SettingScreen完成後に有効にする
//                    MenuTextButton(text = "Setting", enabled = viewModel.isReady) {
//                        navController.navigate(ScreenRoute.SettingScreen.route)
//                    }
                }
            }
            if (viewModel.isRestart) {
                navController.navigate(ScreenRoute.StartApp.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            } else if (!errorMessage.isNullOrBlank()) {
                AlertDialog(
                    onDismissRequest = {},
                    confirmButton = {
                        TextButton(
                            onClick = { viewModel.restart() }) {
                            Text(text = "再起動")
                        }
                    },
                    text = { Text(text = errorMessage) }
                )
            } else if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun MenuTextButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

@Composable
fun DebugView(
    state: TopState,
    dataList: List<TranslateData>?,
    viewModel: TopViewModel,
) {
    when {
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        !state.error.isNullOrBlank() -> {
            TextField(
                value = state.error,
                onValueChange = {},
                textStyle = MaterialTheme.typography.bodyLarge,
            )
        }
        (dataList != null) -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.dimens.small),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(onClick = { viewModel.save() }) {
                        Text(text = "Save")
                    }
                    Spacer(modifier = Modifier.padding(AppTheme.dimens.medium))
                    Button(onClick = { viewModel.load() }) {
                        Text(text = "Load")
                    }
                }
                LazyColumn(modifier = Modifier.padding(AppTheme.dimens.medium)) {
                    items(dataList) { data ->
                        Text(
                            text = "${data.english} : ${data.translateToJapanese}",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}