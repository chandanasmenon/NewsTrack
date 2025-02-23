package com.chandana.newstrack.ui.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chandana.newstrack.R
import com.chandana.newstrack.ui.base.ButtonText
import com.chandana.newstrack.ui.theme.NewsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenRoute(onNavigate: (String) -> Unit) {
    NewsAppTheme {
        Scaffold(topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
            ), title = {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
            })
        }, content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding)
                    .padding(top = 10.dp, bottom = 4.dp, start = 2.dp, end = 2.dp)
            ) {
                HomeScreen(onNavigate)
            }
        })
    }
}

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopHeadlines(onNavigate)
        PaginationTopHeadlines(onNavigate)
        OfflineTopHeadlines(onNavigate)
        NewsCategories(onNavigate)
        CountrySelection(onNavigate)
        LanguageSelection(onNavigate)
        SearchNews()
    }
}

@Composable
fun TopHeadlines(onNavigate: (String) -> Unit) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        onClick = {
            onNavigate("topheadline")
        }
    ) {
        ButtonText(text = stringResource(R.string.topheadline_sources))
    }
}

@Composable
fun PaginationTopHeadlines(onNavigate: (String) -> Unit) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        onClick = {
            onNavigate("paginationtopheadline")
        }
    ) {
        ButtonText(text = stringResource(R.string.pagination_topheadline_sources))
    }
}

@Composable
fun OfflineTopHeadlines(onNavigate: (String) -> Unit) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        onClick = {
            onNavigate("offlinetopheadline")
        }
    ) {
        ButtonText(text = stringResource(R.string.offline_topheadlines_sources_text))

    }
}

@Composable
fun NewsCategories(onNavigate: (String) -> Unit) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        onClick = {
            onNavigate("newscategories")
        }
    ) {
        ButtonText(text = stringResource(R.string.news_categories))
    }
}

@Composable
fun CountrySelection(onNavigate: (String) -> Unit) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        onClick = {
            onNavigate("countryselection")
        }
    ) {
        ButtonText(text = stringResource(R.string.country_selection_text))
    }
}

@Composable
fun LanguageSelection(onNavigate: (String) -> Unit) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        onClick = {
            onNavigate("languageselection")
        }
    ) {
        ButtonText(text = stringResource(R.string.language_selection_text))
    }
}

@Composable
fun SearchNews() {
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        onClick = {

        }
    ) {
        ButtonText(text = stringResource(R.string.search_text))
    }
}