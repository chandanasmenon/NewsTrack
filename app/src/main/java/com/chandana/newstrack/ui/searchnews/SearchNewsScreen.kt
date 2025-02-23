package com.chandana.newstrack.ui.searchnews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.chandana.newstrack.R
import com.chandana.newstrack.data.model.Article
import com.chandana.newstrack.ui.base.DescriptionText
import com.chandana.newstrack.ui.base.ShowError
import com.chandana.newstrack.ui.base.ShowLoading
import com.chandana.newstrack.ui.base.TitleText
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.theme.NewsAppTheme
import com.chandana.newstrack.utils.AppConstant
import com.chandana.newstrack.utils.extensions.capitalizeWords
import com.chandana.newstrack.utils.extensions.getFilterValue
import com.chandana.newstrack.utils.extensions.getFilterValuesList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenRoute(
    searchQuery: String,
    filters: String,
    onNewsClick: (url: String) -> Unit,
    onNavigateBack: () -> Unit,
    addFilters: (String, String) -> Unit,
    viewModel: SearchNewsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiStateSearchNews by viewModel.uiStateSearchNews.collectAsStateWithLifecycle()
    val uiStateFilterSearchNews by viewModel.uiStateFilterSearchNews.collectAsStateWithLifecycle()
    var language by rememberSaveable {
        mutableStateOf("")
    }
    var sortBy by rememberSaveable {
        mutableStateOf("")
    }
    var query by rememberSaveable {
        mutableStateOf("")
    }
    query = searchQuery
    LaunchedEffect(key1 = Unit) {
        if (searchQuery.isNotEmpty() && filters.isEmpty()) {
            viewModel.setSearchQuery(searchQuery)
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (filters.isNotEmpty()) {
            val filterList = filters.getFilterValuesList(filters)
            language = filterList.getFilterValue(
                filterList,
                context.getString(R.string.languages_text)
            )
            sortBy = filterList.getFilterValue(
                filterList,
                context.getString(R.string.sortby_text)
            )
            viewModel.getFilterSearchNewsList(
                query,
                language, sortBy
            )
        }
    }
    NewsAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ), title = {
                    Text(
                        text = stringResource(R.string.search_news_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }, navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(2.dp)
                            .size(32.dp)
                            .clickable {
                                onNavigateBack()
                            },
                        painter = painterResource(id = R.drawable.back_button),
                        contentDescription = stringResource(R.string.back_button_text),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )

                })
            }, content = { padding ->
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(padding)
                            .padding(3.dp)
                    ) {
                        OutlinedTextField(
                            value = query,
                            onValueChange = { newQuery ->
                                query = newQuery
                                viewModel.setSearchQuery(newQuery)
                                language = ""
                                sortBy = ""
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(1.dp),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.search_news_hint_text),
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = stringResource(R.string.search_icon_text)
                                )
                            },
                            trailingIcon = {
                                if (query.isNotEmpty()) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = stringResource(R.string.clear_icon_text),
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable {
                                                query = ""
                                                language = ""
                                                sortBy = ""
                                                viewModel.setSearchQuery(query)
                                            }
                                    )
                                }
                            },
                            shape = TextFieldDefaults.shape,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search)
                        )

                        Box(modifier = Modifier.weight(4f)) {
                            if (language.isNotEmpty() || sortBy.isNotEmpty()) {
                                SearchData(uiStateFilterSearchNews, onNewsClick)
                            } else if (query.isNotEmpty() && query.length >= AppConstant.MIN_SEARCH_CHAR) {
                                SearchData(uiStateSearchNews, onNewsClick)
                            }
                        }
                        Button(
                            onClick = {
                                if (language.isNotEmpty() || sortBy.isNotEmpty()) {
                                    addFilters(query, filters)
                                } else {
                                    addFilters(query, context.getString(R.string.default_text))
                                }

                            },
                            enabled = query.isNotEmpty() && query.length >= AppConstant.MIN_SEARCH_CHAR,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 20.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.show_filters_text),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            })
    }
}

@Composable
fun SearchData(uiState: UiState<List<Article>>, onNewsClick: (url: String) -> Unit) {
    when (uiState) {
        is UiState.Success -> {
            SearchNewsList(uiState.data, onNewsClick)
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
fun SearchNewsList(newsList: List<Article>, onNewsClick: (url: String) -> Unit) {
    if (newsList.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_results_found_text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(newsList, key = { news -> news.url.toString() }) { news ->
                NewsItem(news = news, onNewsClick = onNewsClick)
            }
        }
    }
}

@Composable
fun NewsItem(news: Article, onNewsClick: (url: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(start = 2.dp, end = 2.dp, top = 3.dp, bottom = 3.dp)
            .shadow(1.dp, RectangleShape, true, MaterialTheme.colorScheme.surfaceTint)
            .clickable {
                if (news.url?.isNotEmpty() == true) {
                    onNewsClick(news.url)
                }
            }, verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        news.title?.let { TitleText(it) }
        BannerImage(news)
        news.publishedAt?.let { PublishedDisplay(publishedAt = it) }
        news.author?.let { AuthorDisplay(it) }
        news.content?.let { ContentDisplay(content = it) }
        news.description?.let { DescriptionText(it) }
    }
}

@Composable
fun AuthorDisplay(author: String) {
    Row(
        modifier = Modifier.padding(2.5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.author_text),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(3.dp)
        )
        Text(
            text = author.capitalizeWords(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(3.dp)
        )
    }
}

@Composable
fun ContentDisplay(content: String) {
    Row(
        modifier = Modifier.padding(2.5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        letterSpacing = 0.4.sp
                    )
                ) {
                    append(stringResource(R.string.content_text))
                }
                withStyle(
                    style = SpanStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        letterSpacing = 0.5.sp
                    )
                ) {
                    append(content.capitalizeWords())
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(3.dp)
        )
    }
}

@Composable
fun PublishedDisplay(publishedAt: String) {
    Row(
        modifier = Modifier.padding(2.5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.published_at_text),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(3.dp)
        )
        Text(
            text = publishedAt.capitalizeWords(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(3.dp)
        )
    }
}

@Composable
fun BannerImage(article: Article) {
    AsyncImage(
        model = article.urlToImage,
        contentDescription = article.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
    )
}