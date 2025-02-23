package com.chandana.newstrack.utils.extensions

import com.chandana.newstrack.utils.AppConstant

/* Extension function to capitalize the first letter of each word */
fun String.capitalizeWords(): String {
    return this.lowercase().split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

/* Function to get the language name based on the code */
fun String.getLanguageName(code: String): String {
    return AppConstant.LANGUAGES.find { it.id == code }?.value ?: "Unknown"
}

/* Function to get the country name based on the code */
fun String.getCountryName(code: String): String {
    return AppConstant.COUNTRIES.find { it.id == code }?.value ?: "Unknown"
}

/* Parses a filter string and converts it into a list of key-value pairs.*/
fun String.getFilterValuesList(filters: String): List<Pair<String, String>> {
    return filters.split("|").map { filter ->
        val (key, values) = filter.split(":")
        key to values
    }
}

/* Retrieves the value of a specific filter from a list of key-value filter pairs */
fun List<Pair<String, String>>.getFilterValue(
    filterList: List<Pair<String, String>>,
    filter: String
): String {
    val pair = filterList.find { it.first == filter }
    return pair?.second ?: ""
}