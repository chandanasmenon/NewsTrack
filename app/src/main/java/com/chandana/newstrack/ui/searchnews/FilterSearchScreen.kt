package com.chandana.newstrack.ui.searchnews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.chandana.newstrack.utils.extensions.getFilterValue
import com.chandana.newstrack.utils.extensions.getFilterValuesList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSearchRoute(
    query: String,
    filters: String,
    onNavigate: (String, String) -> Unit,
    applyFilters: (String, String) -> Unit
) {
    val context = LocalContext.current
    var resetFilter by rememberSaveable {
        mutableStateOf(false)
    }
    NewsAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ), title = {
                    Text(
                        text = stringResource(R.string.filter_text),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Start
                    )
                }, navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(2.dp)
                            .size(32.dp)
                            .clickable {
                                if (filters.isNotEmpty()) {
                                    onNavigate(query, filters)
                                } else {
                                    onNavigate(query, context.getString(R.string.default_text))
                                }
                            },
                        painter = painterResource(id = R.drawable.back_button),
                        contentDescription = stringResource(R.string.back_button_text),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                    actions = {
                        Text(text = stringResource(R.string.reset_filter_text),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .clickable {
                                    resetFilter = true
                                }
                        )
                    })
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(padding)
                        .padding(3.dp)
                ) {
                    FilterScreen(query, filters, resetFilter, applyFilters)
                    if (resetFilter) {
                        resetFilter = false
                    }
                }
            })
    }
}

@Composable
fun FilterScreen(
    query: String,
    filters: String,
    resetFilters: Boolean,
    applyFilters: (String, String) -> Unit,
    viewModel: SearchNewsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var selectedFilter by rememberSaveable { mutableStateOf(context.getString(R.string.languages_text)) }
    var previousSelectedLanguage by rememberSaveable { mutableStateOf("") }
    var previousSelectedSortBy by rememberSaveable { mutableStateOf("") }
    var selectedLanguage by rememberSaveable { mutableStateOf("") }
    var selectedSortBy by rememberSaveable { mutableStateOf("") }
    val languages by viewModel.uiStateLanguage.collectAsStateWithLifecycle()
    val sortByOptions by viewModel.uiStateSortBy.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        if (filters.isNotEmpty()) {
            val filterList = filters.getFilterValuesList(filters)
            selectedLanguage =
                filterList.getFilterValue(filterList, context.getString(R.string.languages_text))
            previousSelectedLanguage =
                filterList.getFilterValue(filterList, context.getString(R.string.languages_text))
            selectedSortBy =
                filterList.getFilterValue(filterList, context.getString(R.string.sortby_text))
            previousSelectedSortBy =
                filterList.getFilterValue(filterList, context.getString(R.string.sortby_text))
        }
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.getFilterData()
    }
    val availableFilters by viewModel.uiStateFilterData.collectAsStateWithLifecycle()
    LaunchedEffect(resetFilters) {
        if (resetFilters) {
            selectedLanguage = ""
            selectedSortBy = ""
        }
    }
    Column {
        Box(modifier = Modifier.weight(0.9f)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            )
            {
                Column(
                    modifier = Modifier
                        .weight(0.35f)
                        .padding(2.dp)
                        .background(MaterialTheme.colorScheme.surface),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    ShowSearchFilters(
                        filters = availableFilters,
                        selectedFilter = selectedFilter,
                        onClicked = {
                            selectedFilter = it
                        })
                }

                Column(
                    modifier = Modifier
                        .weight(0.65f)
                        .padding(2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    when (selectedFilter) {
                        context.getString(R.string.languages_text) -> {
                            LaunchedEffect(key1 = Unit) {
                                viewModel.getLanguagesList()
                            }
                            ShowSelectedFilterValues(
                                list = languages,
                                selectedLanguage,
                                onUpdateFilter = {
                                    selectedLanguage = it
                                })
                        }

                        context.getString(R.string.sortby_text) -> {
                            LaunchedEffect(key1 = Unit) {
                                viewModel.getSortByOptions()
                            }
                            ShowSelectedFilterValues(
                                list = sortByOptions,
                                selectedSortBy,
                                onUpdateFilter = {
                                    selectedSortBy = it
                                })
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                if (selectedLanguage.isNotEmpty() || selectedSortBy.isNotEmpty()) {
                    val updatedFilters =
                        "${context.getString(R.string.languages_text)}:$selectedLanguage|${
                            context.getString(
                                R.string.sortby_text
                            )
                        }:$selectedSortBy"
                    applyFilters(query, updatedFilters)
                } else {
                    applyFilters(query, context.getString(R.string.default_text))
                }
            },
            enabled = (previousSelectedLanguage != selectedLanguage || previousSelectedSortBy != selectedSortBy),
            modifier = Modifier
                .padding(12.dp)
                .weight(0.1f)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.apply_filters_text),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun FilterItem(value: String, isSelectedFilter: Boolean, onClicked: (String) -> Unit) {
    Text(
        text = value,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onClicked(value) },
        style = MaterialTheme.typography.bodyMedium.copy(
            background = if (isSelectedFilter) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.background,
            fontWeight = if (isSelectedFilter) FontWeight.ExtraBold
            else FontWeight.SemiBold
        )
    )
}

@Composable
fun RadioGroup(
    code: Code,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            modifier = Modifier.testTag(code.id),
            selected = selectedOption == code.id,
            onClick = { onOptionSelected(code.id) }
        )
        Spacer(modifier = Modifier.width(0.5.dp))
        Text(
            text = code.value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 13.dp)
        )
    }
}

@Composable
fun ShowSearchFilters(
    filters: UiState<List<String>>,
    selectedFilter: String,
    onClicked: (String) -> Unit
) {
    when (filters) {
        is UiState.Loading -> {
            ShowLoading()
        }

        is UiState.Error -> {
            ShowError(filters.message)
        }

        is UiState.Success -> {
            val filterList = filters.data
            LazyColumn(
                modifier = Modifier.padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                items(filterList, key = { filter -> filter }) { filter ->
                    FilterItem(filter, selectedFilter == filter, onClicked = {
                        onClicked(it)
                    })
                }
            }
        }
    }
}

@Composable
fun ShowSelectedFilterValues(
    list: UiState<List<Code>>,
    selectedFilterValue: String,
    onUpdateFilter: (String) -> Unit
) {
    when (list) {
        is UiState.Loading ->
            ShowLoading()

        is UiState.Error ->
            ShowError(text = list.message)

        is UiState.Success -> {
            val data = list.data
            LazyColumn(
                modifier = Modifier.padding(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.Start
            ) {
                items(data, key = { filterItem -> filterItem.id }) { item ->
                    RadioGroup(
                        code = item,
                        selectedOption = selectedFilterValue,
                        onOptionSelected = { onUpdateFilter(it) }
                    )
                }
            }
        }
    }
}