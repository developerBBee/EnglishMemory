package jp.developer.bbee.englishmemory.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember

@Composable
fun AppUtils(
    dimensions: Dimensions,
    content: @Composable () -> Unit,
) {
    val dimSet = remember { dimensions }
    CompositionLocalProvider(LocalAppDimens provides dimSet, content = content)
}

val LocalAppDimens = compositionLocalOf { phoneDimensions }
