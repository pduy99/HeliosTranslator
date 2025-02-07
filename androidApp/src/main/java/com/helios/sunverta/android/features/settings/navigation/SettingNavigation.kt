package com.helios.sunverta.android.features.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.helios.sunverta.android.features.settings.SettingScreen

const val SETTINGS_ROUTE = "settings"

fun NavController.navigateToSettings(navOptions: NavOptions) {
    navigate(SETTINGS_ROUTE, navOptions)
}

fun NavGraphBuilder.settingScreen() {
    composable(route = SETTINGS_ROUTE) {
        SettingScreen()
    }
}