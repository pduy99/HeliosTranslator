package com.helios.kmptranslator.android.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.helios.kmptranslator.android.features.conversation.navigation.VOICE_TRANSLATE_ROUTE
import com.helios.kmptranslator.android.features.conversation.navigation.voiceTranslateScreen
import com.helios.kmptranslator.android.features.history.navigation.TRANSLATE_HISTORY_ROUTE
import com.helios.kmptranslator.android.features.history.navigation.translateHistoryScreen
import com.helios.kmptranslator.android.features.scan.navigation.SCAN_TRANSLATE_ROUTE
import com.helios.kmptranslator.android.features.scan.navigation.scanTranslateScreen
import com.helios.kmptranslator.android.features.settings.navigation.settingScreen
import com.helios.kmptranslator.android.features.text.navigation.TEXT_TRANSLATE_ROUTE
import com.helios.kmptranslator.android.features.text.navigation.textTranslateScreen

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
        textTranslateScreen(
            onOpenHistoryScreen = {
                navController.navigate(TRANSLATE_HISTORY_ROUTE)
            },
            onOpenConversationTranslateScreen = {
                navController.navigate(VOICE_TRANSLATE_ROUTE)
            },
            onOpenCameraTranslateScreen = {
                navController.navigate(SCAN_TRANSLATE_ROUTE)
            }
        )
        voiceTranslateScreen(
            onNavigateUp = {
                navController.navigateUp()
            },
        )
        scanTranslateScreen()
        settingScreen()
        translateHistoryScreen(
            onNavigateUp = {
                navController.navigateUp()
            }
        )
    }
}