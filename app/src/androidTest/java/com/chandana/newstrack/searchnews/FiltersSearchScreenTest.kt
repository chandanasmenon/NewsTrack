package com.chandana.newstrack.searchnews

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import com.chandana.newstrack.R
import com.chandana.newstrack.data.model.Code
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.searchnews.ShowSearchFilters
import com.chandana.newstrack.ui.searchnews.ShowSelectedFilterValues
import org.junit.Rule
import org.junit.Test

class FiltersSearchScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val selectedFilter = "Languages"
    private val selectedFilterValue = "en"

    @Test
    fun loadingFilters_whenUiStateIsLoading_isShown() {
        composeTestRule.setContent {
            ShowSearchFilters(
                filters = UiState.Loading,
                onClicked = {},
                selectedFilter = selectedFilter
            )
        }

        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.resources.getString(R.string.loading))
            .assertExists()
    }

    @Test
    fun showFilters_whenUiStateIsSuccess_isShown() {
        composeTestRule.setContent {
            ShowSearchFilters(
                filters = UiState.Success(filters),
                onClicked = {},
                selectedFilter = selectedFilter
            )
        }

        composeTestRule
            .onNodeWithText(
                filters[0],
                substring = true
            )
            .assertExists()
            .assertHasClickAction()

        composeTestRule.onNode(hasScrollToNodeAction())
            .performScrollToNode(
                hasText(
                    filters[1],
                    substring = true
                )
            )

        composeTestRule
            .onNodeWithText(
                filters[1],
                substring = true
            )
            .assertExists()
            .assertHasClickAction()
    }

    @Test
    fun errorFilters_whenUiStateIsError_isShown() {
        val errorMessage = "Error Message For You"

        composeTestRule.setContent {
            ShowSearchFilters(
                filters = UiState.Error(errorMessage),
                onClicked = {},
                selectedFilter = selectedFilter
            )
        }

        composeTestRule
            .onNodeWithText(errorMessage)
            .assertExists()
    }

    @Test
    fun loadingFilterValues_whenUiStateIsLoading_isShown() {
        composeTestRule.setContent {
            ShowSelectedFilterValues(
                list = UiState.Loading,
                selectedFilterValue = selectedFilterValue,
                onUpdateFilter = {}

            )
        }

        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.resources.getString(R.string.loading))
            .assertExists()
    }

    @Test
    fun showFilterValues_whenUiStateIsSuccess_isShown() {
        composeTestRule.setContent {
            ShowSelectedFilterValues(
                list = UiState.Success(languages),
                selectedFilterValue = selectedFilterValue,
                onUpdateFilter = {}

            )
        }

        composeTestRule
            .onNodeWithText(
                languages[0].value,
                substring = true
            )
            .assertExists()
        composeTestRule
            .onNodeWithTag(
                languages[0].id
            )
            .assertExists()
            .assertHasClickAction()

        composeTestRule.onNode(hasScrollToNodeAction())
            .performScrollToNode(
                hasText(
                    languages[5].value,
                    substring = true
                )
            )

        composeTestRule
            .onNodeWithText(
                languages[5].value,
                substring = true
            )
            .assertExists()
        composeTestRule
            .onNodeWithTag(
                languages[5].id
            )
            .assertExists()
            .assertHasClickAction()

    }

    @Test
    fun errorFilterValues_whenUiStateIsError_isShown() {
        val errorMessage = "Error Message For You"

        composeTestRule.setContent {
            ShowSelectedFilterValues(
                list = UiState.Error(errorMessage),
                selectedFilterValue = selectedFilterValue,
                onUpdateFilter = {}

            )
        }

        composeTestRule
            .onNodeWithText(errorMessage)
            .assertExists()
    }

}

private val filters = listOf("Languages", "SortBy")
private val languages = listOf(
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