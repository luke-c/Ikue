package com.ikue.japanesedictionary.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor() : ViewModel() {
    internal val viewState: StateFlow<SearchUi.State>
        field = MutableStateFlow(value = SearchUi.State())

    fun onSearchQueryChange(newQuery: String) {
        viewState.update {
            it.copy(query = newQuery)
        }
    }

    fun onSearchExpandedChange(isExpanded: Boolean) {
        viewState.update { it.copy(isSearchBarExpanded = isExpanded) }
    }

    fun onSearchSubmitted() {
        // open search results
    }

    fun onLeadingIconClick() {
        val expanded = viewState.value.isSearchBarExpanded
        onSearchExpandedChange(isExpanded = !expanded)

        if (expanded) {
            viewState.update { it.copy(query = "") }
        }
    }

    fun onTrailingIconClick() {
        val expanded = viewState.value.isSearchBarExpanded

        if (expanded) {
            viewState.update { it.copy(query = "") }
        } else {
            viewState.update { it.copy(isSearchBarMenuExpanded = true) }
        }
    }

    fun onSearchBarMenuDismissed() {
        viewState.update { it.copy(isSearchBarMenuExpanded = false) }
    }

    fun onSettingsMenuItemClick() {
        viewState.update {
            it.copy(
                navigateToSettings = true,
                isSearchBarMenuExpanded = false
            )
        }
    }

    fun onNavigationSuccess() {
        viewState.update { it.copy(navigateToSettings = false) }
    }
}