package com.ikue.japanesedictionary.application.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

@Composable
fun IkueBottomNavigation(
    modifier: Modifier = Modifier,
    items: List<BottomNavigationItem>,
    currentDestination: NavDestination?,
    navController: NavHostController,
) {
    NavigationBar(modifier = modifier) {
        items.forEach { destination ->
            val selected = currentDestination?.hasRoute(destination::class) ?: false
            NavigationBarItem(
                selected = selected,
                label = { Text(stringResource(destination.title)) },
                icon = { Icon(destination.icon, contentDescription = null) },
                onClick = {
                    navController.navigate(destination) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                }
            )
        }
    }
}