package com.helios.sunverta.android.features.conversation.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helios.sunverta.android.features.conversation.presentation.AndroidVoiceToTextViewModel
import com.helios.sunverta.android.features.conversation.presentation.VoiceTranslateScreen
import kotlinx.serialization.Serializable

@Serializable
object VoiceTranslateDestination

fun NavGraphBuilder.voiceTranslateScreen(
    onNavigateUp: () -> Unit,
) {
    composable<VoiceTranslateDestination> {
        val viewModel = hiltViewModel<AndroidVoiceToTextViewModel>()
        val state by viewModel.state.collectAsState()

        VoiceTranslateScreen(
            uiState = state,
            onNavigateUp = onNavigateUp,
            onEvent = viewModel::onEvent
        )
    }
}