package jp.developer.bbee.englishmemory.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jp.developer.bbee.englishmemory.presentation.ScreenRoute.StartApp
import jp.developer.bbee.englishmemory.presentation.ScreenRoute.StudyScreen
import jp.developer.bbee.englishmemory.presentation.ScreenRoute.TopScreen
import jp.developer.bbee.englishmemory.presentation.screen.study.StudyScreen
import jp.developer.bbee.englishmemory.presentation.screen.top.TopScreen

@Composable
fun StartApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = TopScreen.route,
    ) {
        composable(StartApp.route) {
            StartApp()
        }
        composable(TopScreen.route) {
            TopScreen(navController = navController)
        }
        composable(StudyScreen.route) {
            StudyScreen(navController = navController)
        }
    }
}