package com.helios.sunverta.android.features.scan.navigation

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helios.kmptranslator.android.features.scan.presentation.CameraPreviewScreen

const val SCAN_TRANSLATE_ROUTE = "scan_translate"

fun NavGraphBuilder.scanTranslateScreen(
    onPermissionDenied: () -> Unit,
    onNavigateUp: () -> Unit
) {
    composable(route = SCAN_TRANSLATE_ROUTE) {
        CameraPreviewScreen(
            onImageCapture = {
                Log.d("CameraPreviewScreen", "Image captured: $it")
            },
            onPermissionDenied = onPermissionDenied,
            onNavigateUp = onNavigateUp
        )
    }
}