package com.chandana.newstrack.countrynews

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.chandana.newstrack.R
import com.chandana.newstrack.data.model.Code
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.countrynews.CountryScreen
import org.junit.Rule
import org.junit.Test

class CountryDisplayTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loading_whenUiStateIsLoading_isShown() {
        composeTestRule.setContent {
            CountryScreen(
                uiState = UiState.Loading,
                onCountry = {}
            )
        }
        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.resources.getString(R.string.loading))
            .assertExists()
    }

    @Test
    fun countries_whenUiStateIsSuccess_isShown() {
        composeTestRule.setContent {
            CountryScreen(
                uiState = UiState.Success(countryList),
                onCountry = {}
            )
        }
        composeTestRule
            .onNodeWithText(
                countryList[0].value,
                substring = true
            )
            .assertExists()
            .assertHasClickAction()
        composeTestRule
            .onNodeWithText(
                countryList[2].value,
                substring = true
            )
            .assertExists()
            .assertHasClickAction()
        composeTestRule
            .onNodeWithText(
                countryList[4].value,
                substring = true
            )
            .assertExists()
            .assertHasClickAction()
    }

    @Test
    fun error_whenUiStateIsError_isShown() {
        val errorMessage = "Error Message For You"

        composeTestRule.setContent {
            CountryScreen(
                uiState = UiState.Error(errorMessage),
                onCountry = {}
            )
        }
        composeTestRule
            .onNodeWithText(errorMessage)
            .assertExists()
    }
}

private val countryList = listOf(
    Code("ae", "United Arab Emirates"),
    Code("ar", "Argentina"),
    Code("at", "Austria"),
    Code("au", "Australia"),
    Code("be", "Belgium"),
    Code("bg", "Bulgaria"),
    Code("br", "Brazil"),
    Code("ca", "Canada"),
    Code("ch", "Switzerland"),
    Code("cn", "China"),
    Code("co", "Colombia"),
    Code("cu", "Cuba"),
    Code("cz", "Czech Republic"),
    Code("de", "Germany"),
    Code("eg", "Egypt"),
    Code("fr", "France"),
    Code("gb", "United Kingdom"),
    Code("gr", "Greece"),
    Code("hk", "Hong Kong"),
    Code("hu", "Hungary"),
    Code("id", "Indonesia"),
    Code("ie", "Ireland"),
    Code("il", "Israel"),
    Code("in", "India"),
    Code("it", "Italy"),
    Code("jp", "Japan"),
    Code("kr", "South Korea"),
    Code("lt", "Lithuania"),
    Code("lv", "Latvia"),
    Code("ma", "Morocco"),
    Code("mx", "Mexico"),
    Code("my", "Malaysia"),
    Code("ng", "Nigeria"),
    Code("nl", "Netherlands"),
    Code("no", "Norway"),
    Code("nz", "New Zealand"),
    Code("ph", "Philippines"),
    Code("pl", "Poland"),
    Code("pt", "Portugal"),
    Code("ro", "Romania"),
    Code("rs", "Serbia"),
    Code("ru", "Russia"),
    Code("sa", "Saudi Arabia"),
    Code("se", "Sweden"),
    Code("sg", "Singapore"),
    Code("si", "Slovenia"),
    Code("sk", "Slovakia"),
    Code("th", "Thailand"),
    Code("tr", "Turkey"),
    Code("tw", "Taiwan"),
    Code("ua", "Ukraine"),
    Code("us", "United States"),
    Code("ve", "Venezuela"),
    Code("za", "South Africa")
)