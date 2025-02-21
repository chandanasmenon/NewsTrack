package com.chandana.newstrack.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chandana.newstrack.ui.homescreen.HomeScreenRoute
import com.chandana.newstrack.ui.offlinetopheadlines.OfflineTopHeadlineRoute
import com.chandana.newstrack.ui.pagingtopheadlinesources.PaginationTopHeadlineRoute
import com.chandana.newstrack.ui.topheadlinesources.TopHeadlineSourcesRoute
import com.chandana.newstrack.utils.extensions.launchCustomTab

sealed class Route(val name: String) {
    object HomeScreen : Route("homescreen")
    object TopHeadlineSources : Route("topheadlinesources")
    object PaginationTopHeadlineSources : Route("paginationtopheadlinesources")
    object OfflineTopHeadlineSources : Route("offlinetopheadlinesources")
}

@Composable
fun NewsNavHost() {

    val context = LocalContext.current
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.HomeScreen.name
    ) {
        composable(route = Route.HomeScreen.name) {
            HomeScreenRoute(onNavigate = { route ->
                when (route) {
                    "topheadline" -> navController.navigate(Route.TopHeadlineSources.name)
                    "paginationtopheadline" -> navController.navigate(Route.PaginationTopHeadlineSources.name)
                    "offlinetopheadline" -> navController.navigate(Route.OfflineTopHeadlineSources.name)
                }
            }
            )
        }

        composable(route = Route.TopHeadlineSources.name) {
            TopHeadlineSourcesRoute(onNavigate = {
                navController.popBackStack()
            }, onNewsClick = {
                context.launchCustomTab(it)
            })
        }

        composable(route = Route.PaginationTopHeadlineSources.name) {
            PaginationTopHeadlineRoute(onNavigate = {
                navController.popBackStack()
            }, onNewsClick = {
                context.launchCustomTab(it)
            })
        }

        composable(route = Route.OfflineTopHeadlineSources.name) {
            OfflineTopHeadlineRoute(onNavigate = {
                navController.popBackStack()
            }, onNewsClick = {
                context.launchCustomTab(it)
            })
        }
    }

}




