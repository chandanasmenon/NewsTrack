package com.chandana.newstrack.offlinetopheadlinesources

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import com.chandana.newstrack.R
import com.chandana.newstrack.data.local.entity.Source
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.offlinetopheadlines.OfflineTopHeadlineScreen
import org.junit.Rule
import org.junit.Test

class OfflineTopHeadlineSourcesTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loading_whenUiStateIsLoading_isShown() {
        composeTestRule.setContent {
            OfflineTopHeadlineScreen(
                uiState = UiState.Loading,
                onNewsClick = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.resources.getString(R.string.loading))
            .assertExists()
    }

    @Test
    fun sources_whenUiStateIsSuccess_isShown() {
        composeTestRule.setContent {
            OfflineTopHeadlineScreen(
                uiState = UiState.Success(testSources),
                onNewsClick = {}
            )
        }

        composeTestRule
            .onNodeWithText(
                testSources[0].name,
                substring = true
            )
            .assertExists()
            .assertHasClickAction()

        composeTestRule.onNode(hasScrollToNodeAction())
            .performScrollToNode(
                hasText(
                    testSources[5].name,
                    substring = true
                )
            )

        composeTestRule
            .onNodeWithText(
                testSources[5].name,
                substring = true
            )
            .assertExists()
            .assertHasClickAction()
    }

    @Test
    fun error_whenUiStateIsError_isShown() {
        val errorMessage = "Error Message For You"

        composeTestRule.setContent {
            OfflineTopHeadlineScreen(
                uiState = UiState.Error(errorMessage),
                onNewsClick = {}
            )
        }

        composeTestRule
            .onNodeWithText(errorMessage)
            .assertExists()
    }

}

private val testSources = listOf(
    Source(
        sourceId = 1,
        category = "Category1",
        country = "Country1",
        language = "Language1",
        name = "Name1",
        description = "Description1",
        url = "Url1",
        id = "Id1"
    ),
    Source(
        sourceId = 2,
        category = "Category2",
        country = "Country2",
        language = "Language2",
        name = "Name2",
        description = "Description2",
        url = "Url2",
        id = "Id2"
    ),
    Source(
        sourceId = 3,
        category = "Category3",
        country = "Country3",
        language = "Language3",
        name = "Name3",
        description = "Description3",
        url = "Url3",
        id = "Id3"
    ),
    Source(
        sourceId = 4,
        category = "Category4",
        country = "Country4",
        language = "Language4",
        name = "Name4",
        description = "Description4",
        url = "Url4",
        id = "Id4"
    ),
    Source(
        sourceId = 5,
        category = "Category5",
        country = "Country5",
        language = "Language5",
        name = "Name5",
        description = "Description5",
        url = "Url5",
        id = "Id5"
    ),
    Source(
        sourceId = 6,
        category = "Category6",
        country = "Country6",
        language = "Language6",
        name = "Name6",
        description = "Description6",
        url = "Url6",
        id = "Id6"
    )
)