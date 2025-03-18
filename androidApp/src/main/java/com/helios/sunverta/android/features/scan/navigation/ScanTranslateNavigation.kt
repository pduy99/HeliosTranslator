package com.helios.sunverta.android.features.scan.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helios.sunverta.android.features.scan.presentation.AndroidScanTranslateViewModel
import com.helios.sunverta.android.features.scan.presentation.ScanTranslateScreen
import com.helios.sunverta.features.scantranslate.ScanTranslateEvent
import kotlinx.serialization.Serializable

@Serializable
object ScanTranslateDestination

fun NavGraphBuilder.scanTranslateScreen(
    onPermissionDenied: () -> Unit,
    onNavigateUp: () -> Unit
) {
    composable<ScanTranslateDestination> {
        val viewModel = hiltViewModel<AndroidScanTranslateViewModel>()
        val context = LocalContext.current
        val lifeCycleOwner = LocalLifecycleOwner.current
        val uiState by viewModel.uiState.collectAsState()
        val surfaceRequest by viewModel.surfaceRequest.collectAsState()

        LaunchedEffect(lifeCycleOwner, uiState.capturedImage) {
            if (uiState.capturedImage == null) {
                viewModel.bindToCamera(context.applicationContext, lifeCycleOwner)
            }
        }

        ScanTranslateScreen(
            surfaceRequest = surfaceRequest,
            onPermissionDenied = onPermissionDenied,
            onTapToFocus = viewModel::tapToFocus,
            onNavigateUp = {
                viewModel.cleanup()
                onNavigateUp()
            },
            uiState = uiState,
            onEvent = viewModel::onEvent
        )

        BackHandler {
            if (uiState.capturedImage == null) {
                viewModel.cleanup()
                onNavigateUp()
            } else {
                viewModel.onEvent(ScanTranslateEvent.Reset)
            }
        }
    }
}