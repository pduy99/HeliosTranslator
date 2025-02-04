package com.helios.kmptranslator.android.features.history.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helios.kmptranslator.android.features.history.presentation.AndroidTranslateHistoryViewModel
import com.helios.kmptranslator.android.features.history.presentation.TranslateHistoryScreen
import kotlinx.serialization.Serializable

@Serializable
object TranslateHistoryDestination

fun NavGraphBuilder.translateHistoryScreen(
    onNavigateUp: () -> Unit
) {
    composable<TranslateHistoryDestination> {

        val viewModel = hiltViewModel<AndroidTranslateHistoryViewModel>()
        val uiState by viewModel.state.collectAsState()

        TranslateHistoryScreen(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            onNavigateUp = onNavigateUp
        )
    }
}