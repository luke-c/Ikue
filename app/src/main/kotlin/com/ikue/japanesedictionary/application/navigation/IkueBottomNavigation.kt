package com.ikue.japanesedictionary.application.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun createBottomNavigationUiModel(
    showTopAndBottomBars: Boolean,
    navigationBarItems: List<BottomNavigationItem>,
    onItemClick: (BottomNavigationItem) -> Unit,
    onItemSelected: (BottomNavigationItem) -> Boolean,
): BottomNavigationUiModel? {
    if (!showTopAndBottomBars) return null
    return BottomNavigationUiModel(
        items = navigationBarItems,
        onItemClick = onItemClick,
        isItemSelected = onItemSelected
    )
}

@Immutable
data class BottomNavigationUiModel(
    val items: List<BottomNavigationItem>,
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