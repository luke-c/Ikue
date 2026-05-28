package com.ikue.japanesedictionary.settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ikue.japanesedictionary.R
import com.ikue.japanesedictionary.application.theme.IkueTheme
import com.ikue.japanesedictionary.settings.domain.UserPreferences

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val userPreferences by viewModel.userPreferences.collectAsStateWithLifecycle()

    SettingsScreenContent(
        userPreferences = userPreferences,
        onCaseSensitiveRomajiSearchChanged = viewModel::onCaseSensitiveRomajiSearchChanged,
        onDefaultSearchTypeChanged = viewModel::onDefaultSearchTypeChanged,
        onLimitSearchResultsChanged = viewModel::onLimitSearchResultsChanged,
        onLimitHistoryResultsChanged = viewModel::onLimitHistoryResultsChanged,
        onLimitFavouriteResultsChanged = viewModel::onLimitFavouriteResultsChanged,
        onStartupPageChanged = viewModel::onStartupPageChanged,
        modifier = modifier
    )
}

@Composable
internal fun SettingsScreenContent(
    userPreferences: UserPreferences?,
    onCaseSensitiveRomajiSearchChanged: (Boolean) -> Unit,
    onDefaultSearchTypeChanged: (Int) -> Unit,
    onLimitSearchResultsChanged: (Int) -> Unit,
    onLimitHistoryResultsChanged: (Int) -> Unit,
    onLimitFavouriteResultsChanged: (Int) -> Unit,
    onStartupPageChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            userPreferences?.let { prefs ->
                PreferenceCategory(title = stringResource(R.string.pref_search_settings_title))

                SwitchPreference(
                    checked = prefs.shouldUseCaseSensitiveRomajiSearch,
                    onCheckedChange = onCaseSensitiveRomajiSearchChanged,
                    title = stringResource(R.string.pref_caseSensitiveRomajiSearch),
                    summary = stringResource(R.string.pref_caseSensitiveRomajiSearch_summ)
                )

                val searchTypeEntries = stringArrayResource(R.array.pref_defaultSearchType_entries).toList()
                val searchTypeValues = stringArrayResource(R.array.pref_defaultSearchType_values).toList()
                ListPreference(
                    title = stringResource(R.string.pref_defaultSearchType),
                    summary = searchTypeEntries.getOrElse(searchTypeValues.indexOf(prefs.defaultSearchType.toString())) { "" },
                    entries = searchTypeEntries,
                    entryValues = searchTypeValues,
                    selectedValue = prefs.defaultSearchType.toString(),
                    onValueChange = { onDefaultSearchTypeChanged(it.toInt()) }
                )

                val limitResultsEntries = stringArrayResource(R.array.pref_limitResults_entries).toList()
                val limitResultsValues = stringArrayResource(R.array.pref_limitResults_values).toList()
                ListPreference(
                    title = stringResource(R.string.pref_limitSearchResults),
                    summary = limitResultsEntries.getOrElse(limitResultsValues.indexOf(prefs.searchResultsLimit.toString())) { "" },
                    entries = limitResultsEntries,
                    entryValues = limitResultsValues,
                    selectedValue = prefs.searchResultsLimit.toString(),
                    onValueChange = { onLimitSearchResultsChanged(it.toInt()) }
                )

                PreferenceCategory(title = stringResource(R.string.pref_history_settings_title))
                ListPreference(
                    title = stringResource(R.string.pref_limitHistoryResults),
                    summary = limitResultsEntries.getOrElse(limitResultsValues.indexOf(prefs.historyLimit.toString())) { "" },
                    entries = limitResultsEntries,
                    entryValues = limitResultsValues,
                    selectedValue = prefs.historyLimit.toString(),
                    onValueChange = { onLimitHistoryResultsChanged(it.toInt()) }
                )

                PreferenceCategory(title = stringResource(R.string.pref_favourites_settings_title))
                ListPreference(
                    title = stringResource(R.string.pref_limitFavouritesResults),
                    summary = limitResultsEntries.getOrElse(limitResultsValues.indexOf(prefs.favouritesLimit.toString())) { "" },
                    entries = limitResultsEntries,
                    entryValues = limitResultsValues,
                    selectedValue = prefs.favouritesLimit.toString(),
                    onValueChange = { onLimitFavouriteResultsChanged(it.toInt()) }
                )

                PreferenceCategory(title = stringResource(R.string.pref_general_settings_title))
                val startupPageEntries = stringArrayResource(R.array.pref_startupPage_entries).toList()
                val startupPageValues = stringArrayResource(R.array.pref_startupPage_values).toList()
                ListPreference(
                    title = stringResource(R.string.pref_startupPage),
                    summary = startupPageEntries.getOrElse(startupPageValues.indexOf(prefs.startupPage.toString())) { "" },
                    entries = startupPageEntries,
                    entryValues = startupPageValues,
                    selectedValue = prefs.startupPage.toString(),
                    onValueChange = { onStartupPageChanged(it.toInt()) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    IkueTheme {
        SettingsScreenContent(
            userPreferences = UserPreferences(
                shouldUseCaseSensitiveRomajiSearch = true,
                defaultSearchType = UserPreferences.SearchType.STARTING_WITH,
                searchResultsLimit = UserPreferences.ResultsLimit.NO_LIMIT,
                historyLimit = UserPreferences.ResultsLimit.FIFTY,
                favouritesLimit = UserPreferences.ResultsLimit.ONE_HUNDRED,
                startupPage = UserPreferences.StartupPage.HOME
            ),
            onCaseSensitiveRomajiSearchChanged = {},
            onDefaultSearchTypeChanged = {},
            onLimitSearchResultsChanged = {},
            onLimitHistoryResultsChanged = {},
            onLimitFavouriteResultsChanged = {},
            onStartupPageChanged = {}
        )
    }
}
