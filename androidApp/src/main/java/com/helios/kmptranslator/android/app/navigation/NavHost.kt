package com.helios.kmptranslator.android.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.helios.kmptranslator.android.features.scantranslate.navigation.scanTranslateScreen
import com.helios.kmptranslator.android.features.settings.navigation.settingScreen
import com.helios.kmptranslator.android.features.texttranslate.navigation.TEXT_TRANSLATE_ROUTE
import com.helios.kmptranslator.android.features.texttranslate.navigation.textTranslateScreen
import com.helios.kmptranslator.android.features.voicetranslate.navigation.voiceTranslateScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = TEXT_TRANSLATE_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        textTranslateScreen()
        voiceTranslateScreen()
        scanTranslateScreen()
        settingScreen()
    }
}