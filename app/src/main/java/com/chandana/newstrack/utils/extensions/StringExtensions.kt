package com.chandana.newstrack.utils.extensions

import com.chandana.newstrack.utils.AppConstant

/* Extension function to capitalize the first letter of each word */
fun String.capitalizeWords(): String {
    return this.lowercase().split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

/* Function to get the language name based on the code */
fun getLanguageName(code: String): String {
    return AppConstant.LANGUAGES.find { it.id == code }?.value ?: "Unknown"
}

/* Function to get the country name based on the code */
fun getCountryName(code: String): String {
    return AppConstant.COUNTRIES.find { it.id == code }?.value ?: "Unknown"
}
