package com.chandana.newstrack.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chandana.newstrack.R
import com.chandana.newstrack.ui.categorynews.CategoryNewsRoute
import com.chandana.newstrack.ui.categorynews.CategoryScreenRoute
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
    object NewsCategories : Route("newscategories")
    object CategoryBasedNews : Route("categorybasednews/{category}")
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
                    "newscategories" -> navController.navigate(Route.NewsCategories.name)
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

        composable(route = Route.NewsCategories.name) {
            CategoryScreenRoute(onCategory = { category ->
                navController.navigate("categorybasednews/$category")
            }, onNavigate = {
                navController.popBackStack()
            })
        }

        composable(
            route = Route.CategoryBasedNews.name, arguments = listOf(
                navArgument(context.getString(R.string.category_text)) {
                    type = NavType.StringType
                })
        ) { backStackEntry ->
            val category =
                backStackEntry.arguments?.getString(stringResource(R.string.category_text))
            if (category != null) {
                CategoryNewsRoute(category, onNewsClick = {
                    context.launchCustomTab(it)
                }, onNavigate = {
                    navController.popBackStack()
                })
            }
        }

    }

}




