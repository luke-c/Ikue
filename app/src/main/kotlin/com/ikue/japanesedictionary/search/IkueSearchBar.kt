package com.ikue.japanesedictionary.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ikue.japanesedictionary.application.theme.IkueTheme

@Composable
internal fun createSearchBarUiModel(
    showTopAndBottomBars: Boolean,
    viewModel: SearchViewModel,
): SearchBarUiModel? {
    if (!showTopAndBottomBars) return null
    val viewState = viewModel.viewState.collectAsStateWithLifecycle()
    return SearchBarUiModel(
        query = viewState.value.query,
        onQueryChange = viewModel::onSearchQueryChange,
        expanded = viewState.value.isExpanded,
        onExpandedChange = viewModel::onSearchExpandedChange,
        onSubmit = viewModel::onSearchSubmitted,
        onLeadingIconClick = viewModel::onLeadingIconClick,
        onTrailingIconClick = viewModel::onTrailingIconClick,
    )
}

@Immutable
internal data class SearchBarUiModel(
    val query: String,
    val onQueryChange: (String) -> Unit,
    val expanded: Boolean,
    val onExpandedChange: (Boolean) -> Unit,
    val onSubmit: () -> Unit,
    val onLeadingIconClick: () -> Unit,
    val onTrailingIconClick: () -> Unit,
) {
    val leadingIcon = if (expanded) Icons.AutoMirrored.Filled.ArrowBack else Icons.Filled.Search
    val trailingIcon = if (expanded) Icons.Filled.Close else Icons.Filled.MoreVert
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun IkueSearchBar(
    modifier: Modifier = Modifier,
    uiModel: SearchBarUiModel,
) {
    SearchBar(
        modifier = modifier,
        inputField = { SearchBarInputField(uiModel = uiModel) },
        expanded = uiModel.expanded,
        onExpandedChange = uiModel.onExpandedChange,
        content = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBarInputField(
    modifier: Modifier = Modifier,
    uiModel: SearchBarUiModel,
) {
    SearchBarDefaults.InputField(
        modifier = modifier,
        query = uiModel.query,
        onQueryChange = uiModel.onQueryChange,
        onSearch = { uiModel.onSubmit() },
        expanded = uiModel.expanded,
        onExpandedChange = uiModel.onExpandedChange,
        placeholder = {
            Text("Search...")
        },
        leadingIcon = {
            SearchBarLeadingIcon(
                icon = uiModel.leadingIcon,
                expanded = uiModel.expanded,
                onClick = uiModel.onLeadingIconClick,
            )
        },
        trailingIcon = {
            SearchBarIconButton(
                icon = uiModel.trailingIcon,
                onClick = uiModel.onTrailingIconClick,
            )
        }
    )
}

@Composable
private fun SearchBarLeadingIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    if (expanded) {
        SearchBarIconButton(
            modifier = modifier,
            icon = icon,
            onClick = onClick
        )
    } else {
        Icon(
            modifier = modifier,
            imageVector = icon,
            contentDescription = null,
        )
    }
}

@Composable
private fun SearchBarIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun IkueSearchBarCollapsedPreview() {
    IkueTheme {
        IkueSearchBar(
            uiModel = SearchBarUiModel(
                query = "",
                onQueryChange = {},
                expanded = false,
                onExpandedChange = {},
                onSubmit = {},
                onLeadingIconClick = {},
                onTrailingIconClick = {},
            )
        )
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun IkueSearchBarExpandedPreview() {
    IkueTheme {
        IkueSearchBar(
            uiModel = SearchBarUiModel(
                query = "Ohayou",
                onQueryChange = {},
                expanded = true,
                onExpandedChange = {},
                onSubmit = {},
                onLeadingIconClick = {},
                onTrailingIconClick = {},
            )
        )
    }
}