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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import jp.developer.bbee.englishmemory.presentation.ScreenRoute
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
                Text("Drawer title", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Top") },
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
                    label = { Text(text = "Study Mode") },
                    selected = false,
                    onClick = {
                        navController.navigate(ScreenRoute.StudyScreen.route) {
                            launchSingleTop = true
                            scope.launch { drawerState.close() }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Settings") },
                    selected = false,
                    onClick = {
                        navController.navigate(ScreenRoute.SettingScreen.route) {
                            launchSingleTop = true
                            scope.launch { drawerState.close() }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Drawer Item") },
                    selected = false,
                    onClick = { /*TODO*/ }
                )
                // ...other drawer items
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
                                    contentDescription = "閉じる"
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
                                    contentDescription = "メニュードロワー"
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
