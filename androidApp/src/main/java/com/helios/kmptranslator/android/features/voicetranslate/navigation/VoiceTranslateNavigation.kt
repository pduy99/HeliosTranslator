package com.helios.kmptranslator.android.features.voicetranslate.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.helios.kmptranslator.android.features.voicetranslate.presentation.AndroidVoiceToTextViewModel
import com.helios.kmptranslator.android.features.voicetranslate.presentation.VoiceTranslateScreen
import com.helios.kmptranslator.features.voicetotext.ConversationTranslateEvent

const val VOICE_TRANSLATE_ROUTE = "voice_translate"

fun NavController.navigateToVoiceTranslate(navOptions: NavOptions) {
    navigate(VOICE_TRANSLATE_ROUTE, navOptions)
}

fun NavGraphBuilder.voiceTranslateScreen(
    onOpenHistoryScreen: () -> Unit,
) {
    composable(
        route = VOICE_TRANSLATE_ROUTE
    ) {
        val viewModel = hiltViewModel<AndroidVoiceToTextViewModel>()
        val state by viewModel.state.collectAsState()

        VoiceTranslateScreen(
            uiState = state,
            onEvent = {
                if (it is ConversationTranslateEvent.OpenHistory) {
                    onOpenHistoryScreen()
                } else {
                    viewModel.onEvent(it)
                }
            }
        )
    }
}