package com.ikue.japanesedictionary.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor() : ViewModel() {
    private val _viewState: MutableStateFlow<SearchUi.State> =
        MutableStateFlow(value = SearchUi.State())
    internal val viewState: StateFlow<SearchUi.State> = _viewState

    fun onSearchQueryChange(newQuery: String) {
        _viewState.update {
            it.copy(query = newQuery)
        }
    }

    fun onSearchExpandedChange(isExpanded: Boolean) {
        _viewState.update { it.copy(isSearchBarExpanded = isExpanded) }
    }

    fun onSearchSubmitted() {
        // open search results
    }

    fun onLeadingIconClick() {
        val expanded = _viewState.value.isSearchBarExpanded
        onSearchExpandedChange(isExpanded = !expanded)

        if (expanded) {
            _viewState.update { it.copy(query = "") }
        }
    }

    fun onTrailingIconClick() {
        val expanded = _viewState.value.isSearchBarExpanded

        if (expanded) {
            _viewState.update { it.copy(query = "") }
        } else {
            _viewState.update { it.copy(isSearchBarMenuExpanded = true) }
        }
    }

    fun onSearchBarMenuDismissed() {
        _viewState.update { it.copy(isSearchBarMenuExpanded = false) }
    }

    fun onSettingsMenuItemClick() {
        // open settings
    }
}