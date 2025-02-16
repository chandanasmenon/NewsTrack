package com.chandana.newstrack.utils

import com.chandana.newstrack.data.model.Code

object AppConstant {
    const val API_KEY = "34f03cf8a221405aa17278a04e8e98a4"
    const val USER_AGENT = "abc"
    const val INITIAL_PAGE = 1
    const val PAGE_SIZE = 10
    const val CATEGORY = "CATEGORY"
    val COUNTRIES = listOf(
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
    val LANGUAGES = listOf(
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
    val CATEGORIES =
        listOf("business", "entertainment", "general", "health", "science", "sports", "technology")
}