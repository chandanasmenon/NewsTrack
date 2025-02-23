package com.chandana.newstrack.searchnews

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import com.chandana.newstrack.R
import com.chandana.newstrack.data.model.Article
import com.chandana.newstrack.data.model.Entity
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.searchnews.SearchData
import org.junit.Rule
import org.junit.Test

class SearchNewsTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loading_whenUiStateIsLoading_isShown() {
        composeTestRule.setContent {
            SearchData(
                uiState = UiState.Loading,
                onNewsClick = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.resources.getString(R.string.loading))
            .assertExists()
    }

    @Test
    fun searchData_whenUiStateIsSuccess_isShown() {
        composeTestRule.setContent {
            SearchData(
                uiState = UiState.Success(testArticles),
                onNewsClick = {}
            )
        }

        testArticles[0].title?.let {
            composeTestRule
                .onNodeWithText(
                    it,
                    substring = true
                )
                .assertExists()
                .assertHasClickAction()
        }

        testArticles[5].title?.let {
            hasText(
                it,
                substring = true
            )
        }?.let {
            composeTestRule.onNode(hasScrollToNodeAction())
                .performScrollToNode(
                    it
                )
        }

        testArticles[5].title?.let {
            composeTestRule
                .onNodeWithText(
                    it,
                    substring = true
                )
                .assertExists()
                .assertHasClickAction()
        }
    }

    @Test
    fun error_whenUiStateIsError_isShown() {
        val errorMessage = "Error Message For You"

        composeTestRule.setContent {
            SearchData(
                uiState = UiState.Error(errorMessage),
                onNewsClick = {}
            )
        }

        composeTestRule
            .onNodeWithText(errorMessage)
            .assertExists()
    }

}

private val testArticles = listOf(
    Article(
        author = "Author1",
        title = "Title1",
        description = "Description1",
        url = "Url1",
        urlToImage = "UrlToImage1",
        publishedAt = "PublishedAt1",
        content = "Content1",
        source = Entity(
            id = "Id1",
            name = "Name1"
        )
    ),
    Article(
        author = "Author2",
        title = "Title2",
        description = "Description2",
        url = "Url2",
        urlToImage = "UrlToImage2",
        publishedAt = "PublishedAt2",
        content = "Content2",
        source = Entity(
            id = "Id2",
            name = "Name2"
        )
    ),
    Article(
        author = "Author3",
        title = "Title3",
        description = "Description3",
        url = "Url3",
        urlToImage = "UrlToImage3",
        publishedAt = "PublishedAt3",
        content = "Content3",
        source = Entity(
            id = "Id3",
            name = "Name3"
        )
    ),
    Article(
        author = "Author4",
        title = "Title4",
        description = "Description4",
        url = "Url4",
        urlToImage = "UrlToImage4",
        publishedAt = "PublishedAt4",
        content = "Content4",
        source = Entity(
            id = "Id4",
            name = "Name4"
        )
    ),
    Article(
        author = "Author5",
        title = "Title5",
        description = "Description5",
        url = "Url5",
        urlToImage = "UrlToImage5",
        publishedAt = "PublishedAt5",
        content = "Content5",
        source = Entity(
            id = "Id5",
            name = "Name5"
        )
    ),
    Article(
        author = "Author6",
        title = "Title6",
        description = "Description6",
        url = "Url6",
        urlToImage = "UrlToImage6",
        publishedAt = "PublishedAt6",
        content = "Content6",
        source = Entity(
            id = "Id6",
            name = "Name6"
        )
    ),
    Article(
        author = "Author7",
        title = "Title7",
        description = "Description7",
        url = "Url7",
        urlToImage = "UrlToImage7",
        publishedAt = "PublishedAt7",
        content = "Content7",
        source = Entity(
            id = "Id7",
            name = "Name7"
        )
    )
)