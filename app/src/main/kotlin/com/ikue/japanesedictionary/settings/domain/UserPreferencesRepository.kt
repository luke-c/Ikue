package com.ikue.japanesedictionary.settings.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val CASE_SENSITIVE_ROMAJI_SEARCH = booleanPreferencesKey("case_sensitive_romaji_search")
        val DEFAULT_SEARCH_TYPE = intPreferencesKey("default_search_type")
        val LIMIT_SEARCH_RESULTS = intPreferencesKey("limit_search_results")
        val LIMIT_HISTORY_RESULTS = intPreferencesKey("limit_history_results")
        val LIMIT_FAVOURITE_RESULTS = intPreferencesKey("limit_favourite_results")
        val STARTUP_PAGE = intPreferencesKey("startup_page")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserPreferences(
                shouldUseCaseSensitiveRomajiSearch = preferences[PreferencesKeys.CASE_SENSITIVE_ROMAJI_SEARCH] ?: true,
                defaultSearchType = preferences[PreferencesKeys.DEFAULT_SEARCH_TYPE].toSearchType(),
                searchResultsLimit = preferences[PreferencesKeys.LIMIT_SEARCH_RESULTS].toResultsLimit(),
                historyLimit = preferences[PreferencesKeys.LIMIT_HISTORY_RESULTS].toResultsLimit(),
                favouritesLimit = preferences[PreferencesKeys.LIMIT_FAVOURITE_RESULTS].toResultsLimit(),
                startupPage = preferences[PreferencesKeys.STARTUP_PAGE].toStartupPage()
            )
        }

    suspend fun updateCaseSensitiveRomajiSearch(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CASE_SENSITIVE_ROMAJI_SEARCH] = enabled
        }
    }

    suspend fun updateDefaultSearchType(searchType: UserPreferences.SearchType) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DEFAULT_SEARCH_TYPE] = searchType.toInt()
        }
    }

    suspend fun updateLimitSearchResults(limit: UserPreferences.ResultsLimit) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LIMIT_SEARCH_RESULTS] = limit.toInt()
        }
    }

    suspend fun updateLimitHistoryResults(limit: UserPreferences.ResultsLimit) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LIMIT_HISTORY_RESULTS] = limit.toInt()
        }
    }

    suspend fun updateLimitFavouriteResults(limit: UserPreferences.ResultsLimit) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LIMIT_FAVOURITE_RESULTS] = limit.toInt()
        }
    }

    suspend fun updateStartupPage(page: UserPreferences.StartupPage) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.STARTUP_PAGE] = page.toInt()
        }
    }

    private fun Int?.toSearchType() = when (this) {
        0 -> UserPreferences.SearchType.EXACT_MATCH
        1 -> UserPreferences.SearchType.ENDING_WITH
        2 -> UserPreferences.SearchType.STARTING_WITH
        3 -> UserPreferences.SearchType.CONTAINING
        else -> UserPreferences.SearchType.EXACT_MATCH
    }

    private fun UserPreferences.SearchType?.toInt() = when (this) {
        UserPreferences.SearchType.EXACT_MATCH -> 0
        UserPreferences.SearchType.ENDING_WITH -> 1
        UserPreferences.SearchType.STARTING_WITH -> 2
        UserPreferences.SearchType.CONTAINING -> 3
        null -> 0
    }

    private fun UserPreferences.ResultsLimit?.toInt() = when (this) {
        UserPreferences.ResultsLimit.TEN -> 10
        UserPreferences.ResultsLimit.TWENTY_FIVE -> 25
        UserPreferences.ResultsLimit.FIFTY -> 50
        UserPreferences.ResultsLimit.ONE_HUNDRED -> 100
        UserPreferences.ResultsLimit.TWO_HUNDRED -> 200
        UserPreferences.ResultsLimit.NO_LIMIT -> 0
        null -> 0
    }

    private fun Int?.toResultsLimit() = when(this) {
        10 -> UserPreferences.ResultsLimit.TEN
        25 -> UserPreferences.ResultsLimit.TWENTY_FIVE
        50 -> UserPreferences.ResultsLimit.FIFTY
        100 -> UserPreferences.ResultsLimit.ONE_HUNDRED
        200 -> UserPreferences.ResultsLimit.TWO_HUNDRED
        else -> UserPreferences.ResultsLimit.NO_LIMIT
    }

    private fun Int?.toStartupPage() = when(this) {
        0 -> UserPreferences.StartupPage.HISTORY
        1 -> UserPreferences.StartupPage.HOME
        2 -> UserPreferences.StartupPage.FAVOURITES
        else -> UserPreferences.StartupPage.HOME
    }

    private fun UserPreferences.StartupPage?.toInt() = when(this) {
        UserPreferences.StartupPage.HISTORY -> 0
        UserPreferences.StartupPage.HOME -> 1
        UserPreferences.StartupPage.FAVOURITES -> 2
        null -> 1
    }
}
