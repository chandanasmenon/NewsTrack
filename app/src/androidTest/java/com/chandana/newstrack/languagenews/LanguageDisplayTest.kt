package com.chandana.newstrack.languagenews

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.chandana.newstrack.R
import com.chandana.newstrack.data.model.Code
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.languagenews.LanguageScreen
import org.junit.Rule
import org.junit.Test

class LanguageDisplayTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loading_whenUiStateIsLoading_isShown() {
        composeTestRule.setContent {
            LanguageScreen(
                uiState = UiState.Loading,
                onLanguage = {}
            )
        }
        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.resources.getString(R.string.loading))
            .assertExists()
    }

    @Test
    fun languages_whenUiStateIsSuccess_isShown() {
        composeTestRule.setContent {
            LanguageScreen(
                uiState = UiState.Success(languageList),
                onLanguage = {}
            )
        }
        composeTestRule
            .onNodeWithText(
                languageList[0].value,
                substring = true
            )
            .assertExists()
            .assertHasClickAction()
        composeTestRule
            .onNodeWithText(
                languageList[2].value,
                substring = true
            )
            .assertExists()
            .assertHasClickAction()
        composeTestRule
            .onNodeWithText(
                languageList[4].value,
                substring = true
            )
            .assertExists()
            .assertHasClickAction()
    }

    @Test
    fun error_whenUiStateIsError_isShown() {
        val errorMessage = "Error Message For You"

        composeTestRule.setContent {
            LanguageScreen(
                uiState = UiState.Error(errorMessage),
                onLanguage = {}
            )
        }
        composeTestRule
            .onNodeWithText(errorMessage)
            .assertExists()
    }
}

private val languageList = listOf(
    Code("ar", "Arabic"),
    Code("de", "German"),
    Code("en", "English"),
    Code("es", "Spanish"),
    Code("fr", "French"),
    Code("he", "Hebrew"),
    Code("it", "Italian"),
    Code("nl", "Dutch"),
    Code("no", "Norwegian"),
    Code("pt", "Portuguese"),
    Code("ru", "Russian"),
    Code("sv", "Swedish"),
    Code("ud", "Urdu"),
    Code("zh", "Chinese")
)