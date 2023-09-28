package jp.developer.bbee.englishmemory.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jp.developer.bbee.englishmemory.presentation.ScreenRoute.TopScreen
import jp.developer.bbee.englishmemory.presentation.screen.top.TopScreen

@Composable
fun StartApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = TopScreen.route,
    ) {
        composable(TopScreen.route) {
            TopScreen()
        }
    }
}