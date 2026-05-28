package com.ikue.japanesedictionary.application.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.ikue.japanesedictionary.settings.presentation.SettingsScreen
import com.ikue.japanesedictionary.favourites.presentation.FavouritesScreen
import com.ikue.japanesedictionary.history.presentation.HistoryScreen
import com.ikue.japanesedictionary.home.presentation.HomeScreen

@Composable
fun IkueNavGraph(
    modifier: Modifier = Modifier,
    navigationState: NavigationState,
    onBack: () -> Unit,
) {
    val entryProvider = entryProvider<NavKey> {
        entry<Home> {
            HomeScreen(
                modifier = Modifier.fillMaxSize()
            )
        }
        entry<Favourites> {
            FavouritesScreen()
        }
        entry<History> {
            HistoryScreen()
        }
        entry<Settings> {
            SettingsScreen()
        }
    }

    NavDisplay(
        modifier = modifier,
        entries = navigationState.toEntries(entryProvider),
        onBack = onBack,
    )
}
