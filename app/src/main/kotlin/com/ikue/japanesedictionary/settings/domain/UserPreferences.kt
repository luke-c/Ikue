package com.ikue.japanesedictionary.settings.domain

data class UserPreferences(
    val shouldUseCaseSensitiveRomajiSearch: Boolean,
    val defaultSearchType: SearchType,
    val searchResultsLimit: ResultsLimit,
    val historyLimit: ResultsLimit,
    val favouritesLimit: ResultsLimit,
    val startupPage: StartupPage
) {
    enum class SearchType {
        EXACT_MATCH,
        ENDING_WITH,
        STARTING_WITH,
        CONTAINING,
    }

    enum class ResultsLimit {
        TEN,
        TWENTY_FIVE,
        FIFTY,
        ONE_HUNDRED,
        TWO_HUNDRED,
        NO_LIMIT,
    }

    enum class StartupPage {
        HISTORY,
        HOME,
        FAVOURITES
    }
}