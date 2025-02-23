package com.chandana.newstrack.ui.languagenews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.chandana.newstrack.data.model.Code
import com.chandana.newstrack.ui.base.ShowError
import com.chandana.newstrack.ui.base.ShowLoading
import com.chandana.newstrack.ui.base.UiState
import com.chandana.newstrack.ui.theme.NewsAppTheme
import com.chandana.newstrack.utils.extensions.capitalizeWords
import com.google.accompanist.flowlayout.FlowRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreenRoute(
    onLanguage: (language: String) -> Unit,
    viewModel: LanguageNewsViewModel = hiltViewModel(),
    onNavigate: () -> Boolean
) {
    LaunchedEffect(Unit) {
        viewModel.getLanguagesList()
    }
    val uiState by viewModel.uiStateLanguage.collectAsStateWithLifecycle()
    NewsAppTheme {
        Scaffold(topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ), title = {
                Text(
                    text = stringResource(R.string.language_selection_text),
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
                LanguageScreen(uiState, onLanguage)
            }
        })
    }
}

@Composable
fun LanguageScreen(uiState: UiState<List<Code>>, onLanguage: (language: String) -> Unit) {
    when (uiState) {
        is UiState.Success -> {
            LanguageList(uiState.data, onLanguage)
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
fun LanguageList(languages: List<Code>, onLanguage: (language: String) -> Unit) {
    FlowRow(
        modifier = Modifier
            .padding(top = 6.dp, bottom = 6.dp, start = 6.dp, end = 6.dp)
            .verticalScroll(rememberScrollState()),
        mainAxisSpacing = 4.dp,
        crossAxisSpacing = 0.5.dp
    ) {
        languages.forEach { language ->
            LanguageChip(language = language, onLanguage)
        }
    }
}

@Composable
fun LanguageChip(language: Code, onLanguage: (language: String) -> Unit) {
    SuggestionChip(modifier = Modifier.padding(1.dp),
        onClick = { onLanguage(language.id) },
        label = {
            Text(
                text = language.value.capitalizeWords(),
                textAlign = TextAlign.Right,
                modifier = Modifier.padding(1.dp),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    )
}