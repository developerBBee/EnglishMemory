package jp.developer.bbee.englishmemory.presentation.screen.top

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import jp.developer.bbee.englishmemory.R
import org.junit.Rule
import org.junit.Test

class TopScreenContentTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loadingTest() {
        composeTestRule.setContent {
            TopContent(
                isLoading = true,
                errorMessage = null,
                isReady = false,
                isRestart = false,
                restart = {},
                navigate = {}
            )
        }

        composeTestRule.onNodeWithContentDescription(TopProgressDescription).assertIsDisplayed()
    }

    @Test
    fun errorTest() {
        val errorMessage = "test error message"

        composeTestRule.setContent {
            TopContent(
                isLoading = false,
                errorMessage = errorMessage,
                isReady = false,
                isRestart = false,
                restart = {},
                navigate = {}
            )
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun readyTest() {
        val activity = composeTestRule.activity
        val study = activity.getString(R.string.top_study)
        val history = activity.getString(R.string.top_history)
        val score = activity.getString(R.string.top_score)
        val bookmark = activity.getString(R.string.top_bookmark)

        composeTestRule.setContent {
            TopContent(
                isLoading = false,
                errorMessage = null,
                isReady = true,
                isRestart = false,
                restart = {},
                navigate = {}
            )
        }

        composeTestRule.onNodeWithText(study).assertIsEnabled()
        composeTestRule.onNodeWithText(history).assertIsEnabled()
        composeTestRule.onNodeWithText(score).assertIsEnabled()
        composeTestRule.onNodeWithText(bookmark).assertIsEnabled()
    }

    @Test
    fun notReadyTest() {
        val activity = composeTestRule.activity
        val study = activity.getString(R.string.top_study)
        val history = activity.getString(R.string.top_history)
        val score = activity.getString(R.string.top_score)
        val bookmark = activity.getString(R.string.top_bookmark)

        composeTestRule.setContent {
            TopContent(
                isLoading = true,
                errorMessage = null,
                isReady = false,
                isRestart = false,
                restart = {},
                navigate = {}
            )
        }

        composeTestRule.onNodeWithText(study).assertIsNotEnabled()
        composeTestRule.onNodeWithText(history).assertIsNotEnabled()
        composeTestRule.onNodeWithText(score).assertIsNotEnabled()
        composeTestRule.onNodeWithText(bookmark).assertIsNotEnabled()
    }
}