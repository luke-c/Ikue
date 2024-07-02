package com.ikue.japanesedictionary.application

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ikue.japanesedictionary.application.navigation.Favourites
import com.ikue.japanesedictionary.application.navigation.History
import com.ikue.japanesedictionary.application.navigation.Home
import com.ikue.japanesedictionary.application.navigation.IkueBottomNavigation
import com.ikue.japanesedictionary.application.navigation.IkueNavGraph
import com.ikue.japanesedictionary.application.theme.IkueTheme
import com.ikue.japanesedictionary.search.IkueSearchBar

@Composable
fun IkueApp() {
    IkueTheme {
        val navController = rememberNavController()
        val currentEntry = navController.currentBackStackEntryAsState().value
        val currentDestination = currentEntry?.destination

        val navigationBarItems = listOf(History, Home, Favourites)
        val showTopAndBottomBars = remember(currentDestination) {
            navigationBarItems.any { currentDestination?.hasRoute(it::class) ?: false }
        }

        Scaffold(
            topBar = {
                if (showTopAndBottomBars) {
                    IkueSearchBar(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            bottomBar = {
                if (showTopAndBottomBars) {
                    IkueBottomNavigation(
                        items = navigationBarItems,
                        currentDestination = currentDestination,
                        navController = navController
                    )
                }
            }
        ) { innerPadding ->
            IkueNavGraph(
                modifier = Modifier.padding(innerPadding),
                navController = navController
            )
        }
    }
}

@Preview
@Composable
private fun IkueAppPreview() {
    IkueApp()
}
