package com.ikue.japanesedictionary.application.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.graphics.vector.ImageVector
import com.ikue.japanesedictionary.R
import kotlinx.serialization.Serializable

@Serializable
object Home: BottomNavigationItem {
    override val title: Int = R.string.home_fragment_title
    override val icon: ImageVector = Icons.Filled.Home
}

@Serializable
object Favourites: BottomNavigationItem {
    override val title: Int = R.string.favourites_fragment_title
    override val icon: ImageVector = Icons.Filled.Favorite
}

@Serializable
object History : BottomNavigationItem {
    override val title: Int = R.string.history_fragment_title
    override val icon: ImageVector = Icons.Filled.Refresh
}