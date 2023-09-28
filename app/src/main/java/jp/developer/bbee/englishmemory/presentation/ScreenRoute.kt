package jp.developer.bbee.englishmemory.presentation

sealed class ScreenRoute(val route: String) {
    object TopScreen : ScreenRoute("top")
}
