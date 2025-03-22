package com.helios.sunverta.android.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.helios.sunverta.android.features.conversation.navigation.VoiceTranslateDestination
import com.helios.sunverta.android.features.conversation.navigation.voiceTranslateScreen
import com.helios.sunverta.android.features.history.navigation.TranslateHistoryDestination
import com.helios.sunverta.android.features.history.navigation.translateHistoryScreen
import com.helios.sunverta.android.features.scan.navigation.ScanTranslateDestination
import com.helios.sunverta.android.features.scan.navigation.scanTranslateScreen
import com.helios.sunverta.android.features.settings.navigation.settingScreen
import com.helios.sunverta.android.features.text.navigation.TextTranslateDestination
import com.helios.sunverta.android.features.text.navigation.textTranslateScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: Any = TextTranslateDestination
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        textTranslateScreen(
            onOpenHistoryScreen = {
                navController.navigate(TranslateHistoryDestination)
            },
            onOpenConversationTranslateScreen = {
                navController.navigate(VoiceTranslateDestination)
            },
            onOpenCameraTranslateScreen = {
                navController.navigate(ScanTranslateDestination)
            }
        )
        voiceTranslateScreen(
            onNavigateUp = {
                navController.navigateUp()
            },
        )
        scanTranslateScreen(
            onPermissionDenied = {
                navController.navigateUp()
            },
            onNavigateUp = {
                navController.navigateUp()
            }
        )
        settingScreen()
        translateHistoryScreen(
            onNavigateUp = {
                navController.navigateUp()
            }
        )
    }
}