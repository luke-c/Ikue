package com.ikue.japanesedictionary.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikue.japanesedictionary.settings.domain.UserPreferences
import com.ikue.japanesedictionary.settings.domain.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val userPreferences: StateFlow<UserPreferences?> = userPreferencesRepository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun onCaseSensitiveRomajiSearchChanged(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateCaseSensitiveRomajiSearch(enabled)
        }
    }

    fun onDefaultSearchTypeChanged(searchType: Int) {
        viewModelScope.launch {
            //userPreferencesRepository.updateDefaultSearchType(searchType)
        }
    }

    fun onLimitSearchResultsChanged(limit: Int) {
        viewModelScope.launch {
            //userPreferencesRepository.updateLimitSearchResults(limit)
        }
    }

    fun onLimitHistoryResultsChanged(limit: Int) {
        viewModelScope.launch {
            //userPreferencesRepository.updateLimitHistoryResults(limit)
        }
    }

    fun onLimitFavouriteResultsChanged(limit: Int) {
        viewModelScope.launch {
            //userPreferencesRepository.updateLimitFavouriteResults(limit)
        }
    }

    fun onStartupPageChanged(page: Int) {
        viewModelScope.launch {
            //userPreferencesRepository.updateStartupPage(page)
        }
    }
}
