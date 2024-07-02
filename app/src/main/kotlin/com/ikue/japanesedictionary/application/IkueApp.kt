package com.ikue.japanesedictionary.application

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ikue.japanesedictionary.application.navigation.BottomNavigationUiModel
import com.ikue.japanesedictionary.application.navigation.Favourites
import com.ikue.japanesedictionary.application.navigation.History
import com.ikue.japanesedictionary.application.navigation.Home
import com.ikue.japanesedictionary.application.navigation.IkueBottomNavigation
import com.ikue.japanesedictionary.application.navigation.IkueNavGraph
import com.ikue.japanesedictionary.application.navigation.rememberBottomNavigationUiModel
import com.ikue.japanesedictionary.application.theme.IkueTheme
import com.ikue.japanesedictionary.search.IkueSearchBar
import com.ikue.japanesedictionary.search.SearchBarUiModel
import com.ikue.japanesedictionary.search.SearchViewModel
import com.ikue.japanesedictionary.search.rememberSearchBarUiModel
import kotlinx.collections.immutable.persistentListOf

@Composable
fun IkueApp() {
    IkueTheme {
        val navController = rememberNavController()
        val currentEntry = navController.currentBackStackEntryAsState().value
        val currentDestination = currentEntry?.destination

        val navigationBarItems = persistentListOf(History, Home, Favourites)
        val showTopAndBottomBars = remember(currentDestination) {
            navigationBarItems.any { currentDestination?.hasRoute(it::class) ?: false }
        }

        val bottomNavigationUiModel = rememberBottomNavigationUiModel(
            showTopAndBottomBars = showTopAndBottomBars,
            navigationBarItems = navigationBarItems,
            navController = navController,
            currentDestination = currentDestination,
        )

        val searchViewModel = hiltViewModel<SearchViewModel>()
        val searchBarUiModel = rememberSearchBarUiModel(
            showTopAndBottomBars = showTopAndBottomBars,
            searchViewModel = searchViewModel,
        )


        IkueApp(
            navController = navController,
            bottomNavigationUiModel = bottomNavigationUiModel,
            searchBarUiModel = searchBarUiModel
        )
    }
}

@Composable
fun IkueApp(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    bottomNavigationUiModel: BottomNavigationUiModel?,
    searchBarUiModel: SearchBarUiModel?,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            searchBarUiModel?.let {
                IkueSearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    uiModel = it,
                )
            }
        },
        bottomBar = {
            bottomNavigationUiModel?.let {
                IkueBottomNavigation(uiModel = it)
            }
        }
    ) { innerPadding ->
        IkueNavGraph(
            modifier = Modifier.padding(innerPadding),
            navController = navController
        )
    }
}

@Preview
@Composable
private fun IkueAppPreview() {
    val bottomNavigationUiModel = BottomNavigationUiModel(
        items = persistentListOf(History, Home, Favourites),
        onItemClick = {},
        isItemSelected = { it == Home }
    )

    val searchBarUiModel = SearchBarUiModel(
        query = "",
        onQueryChange = {}
    )

    IkueTheme {
        IkueApp(
            navController = rememberNavController(),
            bottomNavigationUiModel = bottomNavigationUiModel,
            searchBarUiModel = searchBarUiModel
        )
    }
}
