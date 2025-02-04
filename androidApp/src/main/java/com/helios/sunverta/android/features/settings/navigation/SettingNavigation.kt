package com.helios.kmptranslator.android.features.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helios.kmptranslator.android.features.settings.SettingScreen
import kotlinx.serialization.Serializable

@Serializable
object SettingDestination

fun NavGraphBuilder.settingScreen() {
    composable<SettingDestination> {
        SettingScreen()
    }
}