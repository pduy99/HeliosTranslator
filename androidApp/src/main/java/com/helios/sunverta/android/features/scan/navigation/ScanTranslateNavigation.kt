package com.helios.sunverta.android.features.scan.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helios.sunverta.android.features.scan.ScanTranslateScreen

const val SCAN_TRANSLATE_ROUTE = "scan_translate"

fun NavGraphBuilder.scanTranslateScreen() {
    composable(route = SCAN_TRANSLATE_ROUTE) {
        ScanTranslateScreen()
    }
}