package com.ikue.japanesedictionary.application.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import kotlinx.collections.immutable.ImmutableList

@Composable
fun rememberBottomNavigationUiModel(
    showTopAndBottomBars: Boolean,
    navigationBarItems: ImmutableList<BottomNavigationItem>,
    navController: NavHostController,
    currentDestination: NavDestination?,
): BottomNavigationUiModel? {
    return remember(showTopAndBottomBars, navigationBarItems, navController, currentDestination) {
        if (!showTopAndBottomBars) {
            null
        } else {
            BottomNavigationUiModel(
                items = navigationBarItems,
                onItemClick = { destination ->
                    navController.navigate(destination) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                },
                isItemSelected = { destination ->
                    currentDestination?.hasRoute(destination::class) ?: false
                }
            )
        }
    }
}

@Immutable
data class BottomNavigationUiModel(
    val items: ImmutableList<BottomNavigationItem>,
    val isItemSelected: (BottomNavigationItem) -> Boolean,
    val onItemClick: (BottomNavigationItem) -> Unit,
)

@Composable
fun IkueBottomNavigation(
    modifier: Modifier = Modifier,
    uiModel: BottomNavigationUiModel,
) {
    NavigationBar(modifier = modifier) {
        uiModel.items.forEach { destination ->
            val selected = uiModel.isItemSelected(destination)
            NavigationBarItem(
                selected = selected,
                label = { Text(stringResource(destination.title)) },
                icon = { Icon(destination.icon, contentDescription = null) },
                onClick = { uiModel.onItemClick(destination) }
            )
        }
    }
}