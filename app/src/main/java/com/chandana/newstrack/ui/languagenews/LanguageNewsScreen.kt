package com.chandana.newstrack.ui.languagenews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chandana.newstrack.R
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.ui.base.CategoryDisplay
import com.chandana.newstrack.ui.base.CountryDisplay
import com.chandana.newstrack.ui.base.DescriptionText
import com.chandana.newstrack.ui.base.LanguageDisplay
import com.chandana.newstrack.ui.base.NoLanguageNewsFoundMsg
import com.chandana.newstrack.ui.base.ShowError
import com.chandana.newstrack.ui.base.ShowLoading
import com.chandana.newstrack.ui.base.TitleText
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.theme.NewsAppTheme
import com.chandana.newstrack.utils.extensions.getLanguageName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageNewsRoute(
    language: String,
    onNewsClick: (url: String) -> Unit,
    viewModel: LanguageNewsViewModel = hiltViewModel(),
    onNavigate: () -> Boolean
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getLanguageBasedNews(language)

    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    NewsAppTheme {
        Scaffold(topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ), title = {
                Text(
                    text = stringResource(
                        R.string.news_languages_headline,
                        language.getLanguageName(language)
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
            }, navigationIcon = {
                IconButton(onClick = { onNavigate() }) {
                    Icon(
                        modifier = Modifier
                            .padding(2.dp)
                            .size(32.dp),
                        painter = painterResource(id = R.drawable.back_button),
                        contentDescription = stringResource(id = R.string.back_button_text),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            })
        }, content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding)
            ) {
                LanguageNews(uiState, onNewsClick, language)
            }
        })
    }
}

@Composable
fun LanguageNews(
    uiState: UiState<List<ApiSource>>,
    onNewsClick: (url: String) -> Unit,
    language: String
) {
    when (uiState) {
        is UiState.Success -> {
            LanguageNewsList(uiState.data, onNewsClick, language)
        }

        is UiState.Loading -> {
            ShowLoading()
        }

        is UiState.Error -> {
            ShowError(uiState.message)
        }
    }
}

@Composable
fun LanguageNewsList(
    newsList: List<ApiSource>,
    onNewsClick: (url: String) -> Unit,
    language: String
) {
    if (newsList.isEmpty()) {
        NoLanguageNewsFoundMsg(language = language.getLanguageName(language))
    } else {
        LazyColumn(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(newsList, key = { news -> news.id }) { news ->
                LanguageNewsItem(newsItem = news, onNewsClick = onNewsClick)
            }

        }
    }
}

@Composable
fun LanguageNewsItem(newsItem: ApiSource, onNewsClick: (url: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(start = 2.dp, end = 2.dp, top = 3.dp, bottom = 3.dp)
            .shadow(1.dp, RectangleShape, true, MaterialTheme.colorScheme.surfaceTint)
            .clickable {
                if (newsItem.url.isNotEmpty()) {
                    onNewsClick(newsItem.url)
                }
            },
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.Start
    ) {
        TitleText(newsItem.name)
        DescriptionText(newsItem.description)
        CategoryDisplay(newsItem.category)
        LanguageDisplay(newsItem.language)
        CountryDisplay(newsItem.country)
    }
}