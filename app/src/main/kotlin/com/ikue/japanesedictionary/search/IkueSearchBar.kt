package com.ikue.japanesedictionary.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ikue.japanesedictionary.application.theme.IkueTheme

@Composable
internal fun createSearchBarUiModel(
    showTopAndBottomBars: Boolean,
    searchViewModel: SearchViewModel,
): SearchBarUiModel? {
    if (!showTopAndBottomBars) return null
    val viewState = searchViewModel.viewState.collectAsStateWithLifecycle()
    return SearchBarUiModel(
        query = viewState.value.query,
        onQueryChange = searchViewModel::onSearchQueryChange,
        active = viewState.value.isExpanded,
        onActiveChange = searchViewModel::onSearchExpandedChange,
    )
}

@Immutable
data class SearchBarUiModel(
    val query: String,
    val onQueryChange: (String) -> Unit,
    val active: Boolean,
    val onActiveChange: (Boolean) -> Unit
) {
    val leadingIcon = if (active) Icons.AutoMirrored.Filled.ArrowBack else Icons.Filled.Search
    val trailingIcon = if (active) Icons.Filled.Close else Icons.Filled.MoreVert
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IkueSearchBar(
    modifier: Modifier = Modifier,
    uiModel: SearchBarUiModel,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        SearchBar(
            query = uiModel.query,
            onQueryChange = uiModel.onQueryChange,
            onSearch = { },
            active = uiModel.active,
            onActiveChange = uiModel.onActiveChange,
            placeholder = {
                Text("Search...")
            },
            leadingIcon = {
                Icon(uiModel.leadingIcon, contentDescription = null)
            },
            trailingIcon = {
                Icon(uiModel.trailingIcon, contentDescription = null)
            }
        ) {

        }
    }
}

@Preview
@Composable
private fun IkueSearchBarPreview() {
    IkueTheme {
        IkueSearchBar(
            uiModel = SearchBarUiModel(
                query = "",
                onQueryChange = {},
                active = false,
                onActiveChange = {},
            )
        )
    }
}