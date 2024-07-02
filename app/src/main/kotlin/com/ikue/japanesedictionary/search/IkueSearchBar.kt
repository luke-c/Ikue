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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ikue.japanesedictionary.application.theme.IkueTheme

@Composable
fun rememberSearchBarUiModel(
    showTopAndBottomBars: Boolean,
    searchViewModel: SearchViewModel,
): SearchBarUiModel? {
    return remember(showTopAndBottomBars) {
        if (!showTopAndBottomBars) {
            null
        } else {
            SearchBarUiModel(query = "", onQueryChange = {})
        }
    }
}

@Immutable
data class SearchBarUiModel(
    val query: String,
    val onQueryChange: (String) -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IkueSearchBar(
    modifier: Modifier = Modifier,
    uiModel: SearchBarUiModel,
) {
    var active by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        SearchBar(
            query = uiModel.query,
            onQueryChange = uiModel.onQueryChange,
            onSearch = { },
            active = active,
            onActiveChange = { value -> active = value },
            placeholder = {
                Text("Search...")
            },
            leadingIcon = {
                val icon = if (active) Icons.AutoMirrored.Filled.ArrowBack else Icons.Filled.Search
                Icon(icon, contentDescription = null)
            },
            trailingIcon = {
                val icon = if (active) Icons.Filled.Close else Icons.Filled.MoreVert
                Icon(icon, contentDescription = null)
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
            uiModel = SearchBarUiModel(query = "", onQueryChange = {})
        )
    }
}