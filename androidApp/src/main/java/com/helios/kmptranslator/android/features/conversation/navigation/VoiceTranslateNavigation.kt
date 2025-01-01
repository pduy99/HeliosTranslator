package com.helios.kmptranslator.android.features.conversation.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helios.kmptranslator.android.features.conversation.presentation.AndroidVoiceToTextViewModel
import com.helios.kmptranslator.android.features.conversation.presentation.VoiceTranslateScreen

const val VOICE_TRANSLATE_ROUTE = "voice_translate"

fun NavGraphBuilder.voiceTranslateScreen(
    onNavigateUp: () -> Unit,
) {
    composable(
        route = VOICE_TRANSLATE_ROUTE
    ) {
        val viewModel = hiltViewModel<AndroidVoiceToTextViewModel>()
        val state by viewModel.state.collectAsState()

        VoiceTranslateScreen(
            uiState = state,
            onNavigateUp = onNavigateUp,
            onEvent = viewModel::onEvent
        )
    }
}