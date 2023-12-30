package jp.developer.bbee.englishmemory.presentation

sealed class ScreenRoute(val route: String) {
    data object StartApp : ScreenRoute("start")
    data object TopScreen : ScreenRoute("top")
    data object StudyScreen : ScreenRoute("study")
    data object HistoryScreen : ScreenRoute("history")
    data object BookmarkScreen : ScreenRoute("bookmark")
    data object BookmarkSettingScreen : ScreenRoute("bookmark_setting")
    data object SettingScreen : ScreenRoute("setting")
}
