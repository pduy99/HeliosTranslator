package com.helios.sunverta.android.features.scan.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helios.kmptranslator.android.features.scan.presentation.AndroidScanTranslateViewModel
import com.helios.kmptranslator.android.features.scan.presentation.ScanTranslateScreen
import kotlinx.serialization.Serializable

@Serializable
object ScanTranslateDestination

fun NavGraphBuilder.scanTranslateScreen(
    onPermissionDenied: () -> Unit,
    onNavigateUp: () -> Unit
) {
    composable<ScanTranslateDestination> {
        val viewModel = hiltViewModel<AndroidScanTranslateViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        ScanTranslateScreen(
            onPermissionDenied = onPermissionDenied,
            onNavigateUp = onNavigateUp,
            uiState = uiState,
            onEvent = viewModel::onEvent
        )
    }
}