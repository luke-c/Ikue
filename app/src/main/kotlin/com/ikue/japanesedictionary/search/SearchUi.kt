package com.ikue.japanesedictionary.search

internal interface SearchUi {
    data class State(
        val query: String = "",
        val isExpanded: Boolean = false,
    )
}