package jp.developer.bbee.englishmemory.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jp.developer.bbee.englishmemory.presentation.screen.bookmark.BookmarkScreen
import jp.developer.bbee.englishmemory.presentation.screen.bookmark.setting.BookmarkSettingScreen
import jp.developer.bbee.englishmemory.presentation.screen.history.HistoryScreen
import jp.developer.bbee.englishmemory.presentation.screen.setting.SettingScreen
import jp.developer.bbee.englishmemory.presentation.screen.study.StudyScreen
import jp.developer.bbee.englishmemory.presentation.screen.top.TopScreen

@Composable
fun StartApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.TopScreen.route,
    ) {
        composable(ScreenRoute.StartApp.route) {
            StartApp()
        }
        composable(ScreenRoute.TopScreen.route) {
            TopScreen(navController = navController)
        }
        composable(ScreenRoute.StudyScreen.route) {
            StudyScreen(navController = navController)
        }
        composable(ScreenRoute.HistoryScreen.route) {
            HistoryScreen(navController = navController)
        }
        composable(ScreenRoute.BookmarkScreen.route) {
            BookmarkScreen(navController = navController)
        }
        composable(ScreenRoute.BookmarkSettingScreen.route) {
            BookmarkSettingScreen(navController = navController)
        }
        composable(ScreenRoute.SettingScreen.route) {
            SettingScreen(navController = navController)
        }
    }
}