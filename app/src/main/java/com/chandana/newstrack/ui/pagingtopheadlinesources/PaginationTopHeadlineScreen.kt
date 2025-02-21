package com.chandana.newstrack.ui.pagingtopheadlinesources

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.chandana.newstrack.R
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.ui.base.CategoryDisplay
import com.chandana.newstrack.ui.base.CountryDisplay
import com.chandana.newstrack.ui.base.DescriptionText
import com.chandana.newstrack.ui.base.LanguageDisplay
import com.chandana.newstrack.ui.base.ShowError
import com.chandana.newstrack.ui.base.ShowLoading
import com.chandana.newstrack.ui.base.TitleText
import com.chandana.newstrack.ui.theme.NewsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaginationTopHeadlineRoute(
    onNewsClick: (url: String) -> Unit,
    viewModel: TopHeadlinesPagingViewModel = hiltViewModel(),
    onNavigate: () -> Boolean
) {
    val lazyPagingItems = viewModel.uiState.collectAsLazyPagingItems()

    NewsAppTheme {
        Scaffold(topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ), title = {
                Text(
                    text = stringResource(R.string.pagination_topheadline_sources),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
            },
                navigationIcon = {
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
                PaginationTopHeadlineScreen(lazyPagingItems, onNewsClick)
            }
        })
    }
}


@Composable
fun PaginationTopHeadlineScreen(
    sources: LazyPagingItems<ApiSource>,
    onNewsClick: (url: String) -> Unit
) {
    SourceList(sources, onNewsClick)

    sources.apply {
        when {
            loadState.refresh is LoadState.Loading -> {
                ShowLoading()
            }

            loadState.refresh is LoadState.Error -> {
                val error = sources.loadState.refresh as LoadState.Error
                ShowError(error.error.localizedMessage!!)
            }

            loadState.append is LoadState.Loading -> {
                ShowLoading()
            }

            loadState.append is LoadState.Error -> {
                val error = sources.loadState.append as LoadState.Error
                ShowError(error.error.localizedMessage!!)
            }
        }
    }
}

@Composable
fun SourceList(sources: LazyPagingItems<ApiSource>, onNewsClick: (url: String) -> Unit) {
    LazyColumn(
        modifier = Modifier.padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(sources.itemCount, key = { index -> sources[index]!!.id }) { index ->
            Source(sources[index]!!, onNewsClick)
        }
    }
}

@Composable
fun Source(source: ApiSource, onNewsClick: (url: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(start = 2.dp, end = 2.dp, top = 3.dp, bottom = 3.dp)
            .shadow(1.dp, RectangleShape, true, MaterialTheme.colorScheme.surfaceTint)
            .clickable {
                if (source.url.isNotEmpty()) {
                    onNewsClick(source.url)
                }
            }, verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.Start
    ) {
        TitleText(source.name)
        DescriptionText(source.description)
        CategoryDisplay(source.category)
        LanguageDisplay(source.language)
        CountryDisplay(source.country)
    }
}
