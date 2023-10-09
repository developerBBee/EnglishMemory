package jp.developer.bbee.englishmemory.presentation

sealed class ScreenRoute(val route: String) {
    object StartApp : ScreenRoute("start")
    object TopScreen : ScreenRoute("top")
    object StudyScreen : ScreenRoute("study")
}
