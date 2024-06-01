package com.helios.kmptranslator.android.scantranslate.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.helios.kmptranslator.android.scantranslate.ScanTranslateScreen

const val SCAN_TRANSLATE_ROUTE = "scan_translate"

fun NavController.navigateToScanTranslate(navOptions: NavOptions) {
    navigate(SCAN_TRANSLATE_ROUTE, navOptions)
}

fun NavGraphBuilder.scanTranslateScreen() {
    composable(route = SCAN_TRANSLATE_ROUTE) {
        ScanTranslateScreen()
    }
}