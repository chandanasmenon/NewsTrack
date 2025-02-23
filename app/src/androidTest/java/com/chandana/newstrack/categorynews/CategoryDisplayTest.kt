package com.chandana.newstrack.categorynews

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.chandana.newstrack.R
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.categorynews.CategoryScreen
import com.chandana.newstrack.utils.extensions.capitalizeWords
import org.junit.Rule
import org.junit.Test

class CategoryDisplayTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loading_whenUiStateIsLoading_isShown() {
        composeTestRule.setContent {
            CategoryScreen(
                uiState = UiState.Loading,
                onCategory = {}
            )
        }
        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.resources.getString(R.string.loading))
            .assertExists()
    }

    @Test
    fun categories_whenUiStateIsSuccess_isShown() {
        composeTestRule.setContent {
            CategoryScreen(
                uiState = UiState.Success(categoryList),
                onCategory = {}
            )
        }
        composeTestRule
            .onNodeWithText(
                categoryList[0].capitalizeWords(),
                substring = true
            )
            .assertExists()
            .assertHasClickAction()
        composeTestRule
            .onNodeWithText(
                categoryList[2].capitalizeWords(),
                substring = true
            )
            .assertExists()
            .assertHasClickAction()
        composeTestRule
            .onNodeWithText(
                categoryList[4].capitalizeWords(),
                substring = true
            )
            .assertExists()
            .assertHasClickAction()
    }

    @Test
    fun error_whenUiStateIsError_isShown() {
        val errorMessage = "Error Message For You"

        composeTestRule.setContent {
            CategoryScreen(
                uiState = UiState.Error(errorMessage),
                onCategory = {}
            )
        }
        composeTestRule
            .onNodeWithText(errorMessage)
            .assertExists()
    }
}

private val categoryList =
    listOf("business", "entertainment", "general", "health", "science", "sports", "technology")