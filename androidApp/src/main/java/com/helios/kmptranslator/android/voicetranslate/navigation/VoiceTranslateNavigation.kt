package com.helios.kmptranslator.android.voicetranslate.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.helios.kmptranslator.android.voicetranslate.presentation.AndroidVoiceToTextViewModel
import com.helios.kmptranslator.android.voicetranslate.presentation.VoiceTranslateScreen
import com.helios.kmptranslator.voicetotext.VoiceToTextEvent

const val VOICE_TRANSLATE_ROUTE = "voice_translate"

fun NavController.navigateToVoiceTranslate(navOptions: NavOptions) {
    navigate(VOICE_TRANSLATE_ROUTE, navOptions)
}

fun NavGraphBuilder.voiceTranslateScreen() {
    composable(
        route = VOICE_TRANSLATE_ROUTE
    ) {
        val viewModel = hiltViewModel<AndroidVoiceToTextViewModel>()
        val state by viewModel.state.collectAsState()
        val languageCode = it.arguments?.getString("languageCode") ?: "en"
        VoiceTranslateScreen(
            state = state,
            languageCode = languageCode,
            onResult = {

            },
            onEvent = { event ->
                when (event) {
                    is VoiceToTextEvent.Close -> {

                    }

                    else -> viewModel.onEvent(event)
                }
            }
        )
    }

}