package com.ikue.japanesedictionary.search

internal interface SearchUi {
    data class State(
        val query: String = "",
        val isSearchBarExpanded: Boolean = false,
        val isSearchBarMenuExpanded: Boolean = false,
    )
}