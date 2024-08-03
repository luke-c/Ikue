package com.ikue.japanesedictionary.search

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
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
        isSearchBarExpanded = viewState.value.isSearchBarExpanded,
        onExpandedChange = viewModel::onSearchExpandedChange,
        onSubmit = viewModel::onSearchSubmitted,
        onLeadingIconClick = viewModel::onLeadingIconClick,
        onTrailingIconClick = viewModel::onTrailingIconClick,
        isSearchBarMenuExpanded = viewState.value.isSearchBarMenuExpanded,
        onSearchBarMenuDismissed = viewModel::onSearchBarMenuDismissed,
        onSettingsMenuItemClick = viewModel::onSettingsMenuItemClick,
    )
}

@Immutable
internal data class SearchBarUiModel(
    val query: String,
    val onQueryChange: (String) -> Unit,
    val isSearchBarExpanded: Boolean,
    val onExpandedChange: (Boolean) -> Unit,
    val onSubmit: () -> Unit,
    val onLeadingIconClick: () -> Unit,
    val onTrailingIconClick: () -> Unit,
    val isSearchBarMenuExpanded: Boolean,
    val onSearchBarMenuDismissed: () -> Unit,
    val onSettingsMenuItemClick: () -> Unit,
) {
    val leadingIcon =
        if (isSearchBarExpanded) Icons.AutoMirrored.Filled.ArrowBack else Icons.Filled.Search
    val trailingIcon = if (isSearchBarExpanded) Icons.Filled.Close else Icons.Filled.MoreVert
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun IkueSearchBar(
    modifier: Modifier = Modifier,
    uiModel: SearchBarUiModel,
) {
    // It's not possible to add padding to only the unexpanded SearchBar, so we
    // need to dynamically set and remove the padding ourself when the SearchBar
    // is unexpanded and expanded respectively.
    val horizontalPadding by animateDpAsState(
        targetValue = if (uiModel.isSearchBarExpanded) 0.dp else 16.dp,
        label = "IkueSearchBar padding"
    )

    SearchBar(
        modifier = modifier.padding(horizontal = horizontalPadding),
        inputField = { SearchBarInputField(uiModel = uiModel) },
        expanded = uiModel.isSearchBarExpanded,
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
        expanded = uiModel.isSearchBarExpanded,
        onExpandedChange = uiModel.onExpandedChange,
        placeholder = {
            Text("Search...")
        },
        leadingIcon = {
            SearchBarLeadingIcon(
                icon = uiModel.leadingIcon,
                expanded = uiModel.isSearchBarExpanded,
                onClick = uiModel.onLeadingIconClick,
            )
        },
        trailingIcon = {
            SearchBarTrailingIcon(
                icon = uiModel.trailingIcon,
                isSearchBarMenuExpanded = uiModel.isSearchBarMenuExpanded,
                onIconClick = uiModel.onTrailingIconClick,
                onSearchBarMenuDismissed = uiModel.onSearchBarMenuDismissed,
                onSettingsMenuItemClick = uiModel.onSettingsMenuItemClick,
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
private fun SearchBarTrailingIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    isSearchBarMenuExpanded: Boolean,
    onIconClick: () -> Unit,
    onSearchBarMenuDismissed: () -> Unit,
    onSettingsMenuItemClick: () -> Unit,
) {
    Box(modifier = modifier) {
        SearchBarIconButton(
            icon = icon,
            onClick = onIconClick,
        )
        SearchBarMenu(
            expanded = isSearchBarMenuExpanded,
            onDismiss = onSearchBarMenuDismissed,
            onSettingsClick = onSettingsMenuItemClick,
        )
    }
}

@Composable
private fun SearchBarMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = { Text("Settings") },
            onClick = onSettingsClick,
            leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) }
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
                isSearchBarExpanded = false,
                onExpandedChange = {},
                onSubmit = {},
                onLeadingIconClick = {},
                onTrailingIconClick = {},
                isSearchBarMenuExpanded = false,
                onSearchBarMenuDismissed = {},
                onSettingsMenuItemClick = {},
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
                isSearchBarExpanded = true,
                onExpandedChange = {},
                onSubmit = {},
                onLeadingIconClick = {},
                onTrailingIconClick = {},
                isSearchBarMenuExpanded = false,
                onSearchBarMenuDismissed = {},
                onSettingsMenuItemClick = {},
            )
        )
    }
}