package jp.developer.bbee.englishmemory

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import jp.developer.bbee.englishmemory.presentation.screen.top.TopContent
import jp.developer.bbee.englishmemory.presentation.ui.theme.EnglishMemoryTheme

class PreviewScreenshotsTest {
    @Preview(showBackground = true)
    @Composable
    private fun TopContentPreview() {
        EnglishMemoryTheme {
            TopContent(
                isLoading = false,
                errorMessage = null,
                isReady = true,
                isRestart = false,
                restart = {},
                navigate = {}
            )
        }
    }
}