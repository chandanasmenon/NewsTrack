package com.chandana.newstrack

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.topheadlinesources.TopHeadlineSourcesActivity
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TopHeadlineSourcesTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(TopHeadlineSourcesActivity::class.java)

    @Test
    fun loading_whenUiStateIsLoading_showProgressBar() {
        activityRule.scenario.onActivity { activity ->
            activity.setUiState(UiState.Loading)
        }
        onView(withId(R.id.progressBarTopHeadlineSources))
            .check(matches(isDisplayed()))
    }

    @Test
    fun success_whenUiStateIsSuccess_showList() {
        runTest {
            val sourceList = mutableListOf<ApiSource>()
            val sourceOne = ApiSource(
                "general",
                "us",
                "Your trusted source for breaking news...",
                "abc-news",
                "en",
                "ABC News",
                "https://abcnews.go.com"
            )
            val sourceTwo = ApiSource(
                "science",
                "au",
                "Australia's most trusted source of local...",
                "abc-news-au",
                "ar",
                "ABC News (AU)",
                "https://www.abc.net.au/news"
            )
            sourceList.add(sourceOne)
            sourceList.add(sourceTwo)
            activityRule.scenario.onActivity { activity ->
                activity.setUiState(UiState.Success(sourceList))
            }
            onView(withId(R.id.recyclerViewTopHeadlineSources))
                .check(matches(isDisplayed()))

            onView(withId(R.id.recyclerViewTopHeadlineSources))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))

            onView(allOf(withId(R.id.sourceNameTV), withText("ABC News")))
                .check(matches(isDisplayed()))
            onView(
                allOf(
                    withId(R.id.sourceDescriptionTV),
                    withText("Your trusted source for breaking news...")
                )
            )
                .check(matches(isDisplayed()))
            onView(allOf(withId(R.id.categoryTV), withText("general")))
                .check(matches(isDisplayed()))
            onView(allOf(withId(R.id.countryTV), withText("us")))
                .check(matches(isDisplayed()))
            onView(allOf(withId(R.id.languageTV), withText("en")))
                .check(matches(isDisplayed()))

            onView(withId(R.id.recyclerViewTopHeadlineSources))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            onView(allOf(withId(R.id.sourceNameTV), withText("ABC News (AU)")))
                .check(matches(isDisplayed()))
            onView(
                allOf(
                    withId(R.id.sourceDescriptionTV),
                    withText("Australia's most trusted source of local...")
                )
            )
                .check(matches(isDisplayed()))
            onView(allOf(withId(R.id.categoryTV), withText("science")))
                .check(matches(isDisplayed()))
            onView(allOf(withId(R.id.countryTV), withText("au")))
                .check(matches(isDisplayed()))
            onView(allOf(withId(R.id.languageTV), withText("ar")))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun error_whenUiStateIsError_showToast() = runTest {
        val errorMessage = "An error occurred"
        activityRule.scenario.onActivity { activity ->
            activity.setUiState(UiState.Error(errorMessage))
        }
        onView(allOf(withId(R.id.errorMsgTopHeadlineTV), withText("An error occurred")))
            .check(matches(isDisplayed()))
    }

}