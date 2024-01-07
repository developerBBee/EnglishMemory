package jp.developer.bbee.englishmemory.presentation.components.modal

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import jp.developer.bbee.englishmemory.R
import jp.developer.bbee.englishmemory.presentation.ScreenRoute
import jp.developer.bbee.englishmemory.presentation.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffold(
    title: String,
    navController: NavController,
    drawerOpenEnabled: Boolean,
    onClose: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val systemUiController = rememberSystemUiController()
    val isDark = isSystemInDarkTheme()

    // ステータスバーを透過させる
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = !isDark,
        )
    }

    val topAppBarState = rememberTopAppBarState()
    // pinnedScrollBehaviorは、TopAppBarのスクロール状態によって、サイズは固定し、色を変化させる
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerOpenEnabled,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = stringResource(id = R.string.modal_drawer_title),
                    modifier = Modifier.padding(AppTheme.dimens.medium)
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = stringResource(id = R.string.top_title)) },
                    selected = false,
                    onClick = {
                        navController.navigate(ScreenRoute.TopScreen.route) {
                            // launchSingleTopをtrueにすることで、同じ画面に遷移しなくする
                            launchSingleTop = true
                            // drawerを閉じる
                            scope.launch { drawerState.close() }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = stringResource(id = R.string.study_title)) },
                    selected = false,
                    onClick = {
                        navController.navigate(ScreenRoute.StudyScreen.route) {
                            launchSingleTop = true
                            scope.launch { drawerState.close() }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = stringResource(id = R.string.history_title)) },
                    selected = false,
                    onClick = {
                        navController.navigate(ScreenRoute.HistoryScreen.route) {
                            launchSingleTop = true
                            scope.launch { drawerState.close() }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = stringResource(id = R.string.score_title)) },
                    selected = false,
                    onClick = {
                        navController.navigate(ScreenRoute.ScoreScreen.route) {
                            launchSingleTop = true
                            scope.launch { drawerState.close() }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = stringResource(id = R.string.bookmark_title)) },
                    selected = false,
                    onClick = {
                        navController.navigate(ScreenRoute.BookmarkScreen.route) {
                            launchSingleTop = true
                            scope.launch { drawerState.close() }
                        }
                    }
                )
                // TODO SettingScreen完成後に有効にする
//                NavigationDrawerItem(
//                    label = { Text(text = "Settings") },
//                    selected = false,
//                    onClick = {
//                        navController.navigate(ScreenRoute.SettingScreen.route) {
//                            launchSingleTop = true
//                            scope.launch { drawerState.close() }
//                        }
//                    }
//                )
            }
        }
    ) {
        Scaffold(
            // nestedScrollConnectionで、コンテンツのスクロール状態とTopAppBarの状態を同期させる
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = title) },
                    actions = {
                        if (onClose != null) {
                            // 右上閉じるボタン
                            IconButton(onClick = onClose) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = stringResource(id = R.string.common_close)
                                )
                            }
                        } else {
                            // 右上メニューボタン
                            IconButton(onClick = {
                                if (drawerOpenEnabled) {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = stringResource(id = R.string.modal_drawer)
                                )
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                content()
            }
        }
    }
}
