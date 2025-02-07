package com.helios.sunverta.android.features.history.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helios.sunverta.android.features.history.presentation.AndroidTranslateHistoryViewModel
import com.helios.sunverta.android.features.history.presentation.TranslateHistoryScreen

const val TRANSLATE_HISTORY_ROUTE = "translate_history"

fun NavGraphBuilder.translateHistoryScreen(
    onNavigateUp: () -> Unit
) {
    composable(route = TRANSLATE_HISTORY_ROUTE) {

        val viewModel = hiltViewModel<AndroidTranslateHistoryViewModel>()
        val uiState by viewModel.state.collectAsState()

        TranslateHistoryScreen(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            onNavigateUp = onNavigateUp
        )
    }
}