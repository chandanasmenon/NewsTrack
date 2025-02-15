package com.chandana.newstrack.utils.extensions

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent

fun Context.launchCustomTab(url: String) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
}

fun Context.displayErrorMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

