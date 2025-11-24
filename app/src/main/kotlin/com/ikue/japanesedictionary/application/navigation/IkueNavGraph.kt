package com.ikue.japanesedictionary.application.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ikue.japanesedictionary.application.settings.SettingsScreen
import com.ikue.japanesedictionary.favourites.presentation.FavouritesScreen
import com.ikue.japanesedictionary.history.presentation.HistoryScreen
import com.ikue.japanesedictionary.home.presentation.HomeScreen

@Composable
fun IkueNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Any = Home
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Home> {
            HomeScreen(
                modifier = Modifier.fillMaxSize()
            )
        }
        composable<Favourites> {
            FavouritesScreen()
        }
        composable<History> {
            HistoryScreen()
        }
        composable<Settings> {
            SettingsScreen()
        }
    }
}