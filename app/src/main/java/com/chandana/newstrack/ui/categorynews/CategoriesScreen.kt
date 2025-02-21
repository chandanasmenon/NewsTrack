package com.chandana.newstrack.ui.categorynews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chandana.newstrack.R
import com.chandana.newstrack.ui.base.ShowError
import com.chandana.newstrack.ui.base.ShowLoading
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.theme.NewsAppTheme
import com.chandana.newstrack.utils.extensions.capitalizeWords
import com.google.accompanist.flowlayout.FlowRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreenRoute(
    onCategory: (category: String) -> Unit,
    viewModel: CategoryNewsViewModel = hiltViewModel(),
    onNavigate: () -> Boolean
) {
    LaunchedEffect(Unit) {
        viewModel.getCategories()
    }
    val uiState by viewModel.uiStateCategory.collectAsStateWithLifecycle()
    NewsAppTheme {
        Scaffold(topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ), title = {
                Text(
                    text = stringResource(R.string.news_categories),
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
                        contentDescription = "Back Button",
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
                CategoryScreen(uiState, onCategory)
            }
        })
    }
}

@Composable
fun CategoryScreen(uiState: UiState<List<String>>, onCategory: (category: String) -> Unit) {
    when (uiState) {
        is UiState.Success -> {
            CategoryList(uiState.data, onCategory)
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
fun CategoryList(categories: List<String>, onCategory: (category: String) -> Unit) {
    FlowRow(
        modifier = Modifier.padding(8.dp),
        mainAxisSpacing = 4.dp,
        crossAxisSpacing = 0.5.dp
    ) {
        categories.forEach { chipText ->
            CategoryChip(category = chipText, onCategory)
        }
    }
}

@Composable
fun CategoryChip(category: String, onCategory: (category: String) -> Unit) {
    SuggestionChip(modifier = Modifier.padding(1.dp),
        onClick = { onCategory(category) },
        label = {
            Text(
                text = category.capitalizeWords(),
                textAlign = TextAlign.Right,
                modifier = Modifier.padding(1.dp),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    )
}