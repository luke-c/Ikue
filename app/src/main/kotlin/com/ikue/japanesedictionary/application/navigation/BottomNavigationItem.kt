package com.ikue.japanesedictionary.application.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

interface BottomNavigationItem {
    @get:StringRes
    val title: Int
    val icon: ImageVector
}