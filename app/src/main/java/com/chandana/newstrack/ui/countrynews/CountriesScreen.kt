package com.chandana.newstrack.ui.countrynews

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
fun CountryScreenRoute(
    onCountry: (country: String) -> Unit,
    viewModel: CountryNewsViewModel = hiltViewModel(),
    onNavigate: () -> Boolean
) {
    LaunchedEffect(Unit) {
        viewModel.getCountriesList()
    }
    val uiState by viewModel.uiStateCountry.collectAsStateWithLifecycle()
    NewsAppTheme {
        Scaffold(topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ), title = {
                Text(
                    text = stringResource(R.string.country_selection_text),
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
                CountryScreen(uiState, onCountry)
            }
        })
    }
}

@Composable
fun CountryScreen(uiState: UiState<List<Code>>, onCountry: (country: String) -> Unit) {
    when (uiState) {
        is UiState.Success -> {
            CountryList(uiState.data, onCountry)
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
fun CountryList(countries: List<Code>, onCountry: (country: String) -> Unit) {
    FlowRow(
        modifier = Modifier
            .padding(top = 6.dp, bottom = 6.dp, start = 6.dp, end = 6.dp)
            .verticalScroll(rememberScrollState()),
        mainAxisSpacing = 4.dp,
        crossAxisSpacing = 0.5.dp
    ) {
        countries.forEach { country ->
            CountryChip(country = country, onCountry)
        }
    }
}

@Composable
fun CountryChip(country: Code, onCountry: (country: String) -> Unit) {
    SuggestionChip(modifier = Modifier.padding(1.dp),
        onClick = { onCountry(country.id) },
        label = {
            Text(
                text = country.value.capitalizeWords(),
                textAlign = TextAlign.Right,
                modifier = Modifier.padding(1.dp),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    )
}