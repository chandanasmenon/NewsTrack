package com.chandana.newstrack.utils

import android.content.Context
import android.net.ConnectivityManager

interface NetworkHelper {

    fun isNetworkConnected(): Boolean
}

class DefaultNetworkHelper(private val context: Context) : NetworkHelper {

    override fun isNetworkConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }

}