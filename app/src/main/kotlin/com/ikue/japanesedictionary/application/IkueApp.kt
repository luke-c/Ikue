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
import com.ikue.japanesedictionary.application.navigation.createBottomNavigationUiModel
import com.ikue.japanesedictionary.application.theme.IkueTheme
import com.ikue.japanesedictionary.search.IkueSearchBar
import com.ikue.japanesedictionary.search.SearchBarUiModel
import com.ikue.japanesedictionary.search.SearchViewModel
import com.ikue.japanesedictionary.search.createSearchBarUiModel

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

        val bottomNavigationUiModel = createBottomNavigationUiModel(
            showTopAndBottomBars = showTopAndBottomBars,
            navigationBarItems = navigationBarItems,
            navController = navController,
            currentDestination = currentDestination,
        )

        val searchViewModel = hiltViewModel<SearchViewModel>()
        val searchBarUiModel = createSearchBarUiModel(
            showTopAndBottomBars = showTopAndBottomBars,
            viewModel = searchViewModel,
        )

        IkueApp(
            navController = navController,
            bottomNavigationUiModel = bottomNavigationUiModel,
            searchBarUiModel = searchBarUiModel
        )
    }
}

@Composable
internal fun IkueApp(
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
        items = listOf(History, Home, Favourites),
        onItemClick = {},
        isItemSelected = { it == Home }
    )

    val searchBarUiModel = SearchBarUiModel(
        query = "",
        onQueryChange = {},
        expanded = false,
        onExpandedChange = {},
        onSubmit = {},
        onLeadingIconClick = {},
        onTrailingIconClick = {},
    )

    IkueTheme {
        IkueApp(
            navController = rememberNavController(),
            bottomNavigationUiModel = bottomNavigationUiModel,
            searchBarUiModel = searchBarUiModel
        )
    }
}
