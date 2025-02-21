package com.chandana.newstrack.ui.base

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chandana.newstrack.ui.homescreen.HomeScreenRoute

sealed class Route(val name: String) {
    object HomeScreen : Route("homescreen")
}

@Composable
fun NewsNavHost() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.HomeScreen.name
    ) {
        composable(route = Route.HomeScreen.name) {
            HomeScreenRoute(
            )
        }
    }

}




