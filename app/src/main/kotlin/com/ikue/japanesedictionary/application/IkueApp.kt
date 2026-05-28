package com.ikue.japanesedictionary.application

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ikue.japanesedictionary.application.navigation.BottomNavigationUiModel
import com.ikue.japanesedictionary.application.navigation.Favourites
import com.ikue.japanesedictionary.application.navigation.History
import com.ikue.japanesedictionary.application.navigation.Home
import com.ikue.japanesedictionary.application.navigation.IkueBottomNavigation
import com.ikue.japanesedictionary.application.navigation.IkueNavGraph
import com.ikue.japanesedictionary.application.navigation.NavigationState
import com.ikue.japanesedictionary.application.navigation.Navigator
import com.ikue.japanesedictionary.application.navigation.Settings
import com.ikue.japanesedictionary.application.navigation.createBottomNavigationUiModel
import com.ikue.japanesedictionary.application.navigation.rememberNavigationState
import com.ikue.japanesedictionary.application.theme.IkueTheme
import com.ikue.japanesedictionary.search.IkueSearchBar
import com.ikue.japanesedictionary.search.SearchBarUiModel
import com.ikue.japanesedictionary.search.SearchFloatingActionButton
import com.ikue.japanesedictionary.search.SearchViewModel
import com.ikue.japanesedictionary.search.createSearchBarUiModel

@Composable
fun IkueApp() {
    IkueTheme {
        val navigationState = rememberNavigationState(
            startRoute = Home,
            topLevelRoutes = setOf(History, Home, Favourites),
        )
        val navigator = remember { Navigator(navigationState) }

        val currentRoute = navigationState.backStacks[navigationState.topLevelRoute]?.last()

        val navigationBarItems = listOf(History, Home, Favourites)
        val showTopAndBottomBars = remember(currentRoute) {
            navigationBarItems.contains(currentRoute)
        }

        val bottomNavigationUiModel = createBottomNavigationUiModel(
            showTopAndBottomBars = showTopAndBottomBars,
            navigationBarItems = navigationBarItems,
            onItemClick = {
                navigator.navigate(it)
            },
            onItemSelected = { it == navigationState.topLevelRoute }
        )

        val searchViewModel = hiltViewModel<SearchViewModel>()
        val searchBarUiModel = createSearchBarUiModel(
            showTopAndBottomBars = showTopAndBottomBars,
            viewModel = searchViewModel,
            onNavigateToSettings = {
                navigator.navigate(Settings)
            },
        )

        val onBack = { navigator.goBack() }

        IkueApp(
            navigationState = navigationState,
            bottomNavigationUiModel = bottomNavigationUiModel,
            searchBarUiModel = searchBarUiModel,
            onBack = onBack,
        )
    }
}

@Composable
internal fun IkueApp(
    modifier: Modifier = Modifier,
    navigationState: NavigationState,
    bottomNavigationUiModel: BottomNavigationUiModel?,
    searchBarUiModel: SearchBarUiModel?,
    onBack: () -> Unit,
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
        },
        floatingActionButton = {
            searchBarUiModel?.let {
                SearchFloatingActionButton(
                    onClick = { it.onExpandedChange(true) }
                )
            }
        }
    ) { innerPadding ->
        IkueNavGraph(
            modifier = Modifier.padding(innerPadding),
            navigationState = navigationState,
            onBack = onBack,
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
        isSearchBarExpanded = false,
        onExpandedChange = {},
        onSubmit = {},
        onLeadingIconClick = {},
        onTrailingIconClick = {},
        isSearchBarMenuExpanded = false,
        onSearchBarMenuDismissed = {},
        onSettingsMenuItemClick = {},
        navigateToSettings = false,
        onNavigateToSettings = {},
    )

    IkueTheme {
        val navigationState = rememberNavigationState(
            startRoute = Home,
            topLevelRoutes = setOf(History, Home, Favourites),
        )

        IkueApp(
            navigationState = navigationState,
            bottomNavigationUiModel = bottomNavigationUiModel,
            searchBarUiModel = searchBarUiModel,
            onBack = {},
        )
    }
}
