package com.helios.kmptranslator.android.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.helios.kmptranslator.android.translate.presentation.AndroidTranslateViewModel
import com.helios.kmptranslator.android.translate.presentation.TranslateScreen
import com.helios.kmptranslator.android.voicetotext.presentation.AndroidVoiceToTextViewModel
import com.helios.kmptranslator.android.voicetotext.presentation.VoiceToTextScreen
import com.helios.kmptranslator.translate.presentation.TranslateEvent
import com.helios.kmptranslator.voicetotext.presentation.VoiceToTextEvent

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = Routes.Translate.name
    ) {
        composable(route = Routes.Translate.name) {
            val viewModel = hiltViewModel<AndroidTranslateViewModel>()
            val state by viewModel.state.collectAsState()
            val voiceResult by it
                .savedStateHandle
                .getStateFlow<String?>("voiceResult", null)
                .collectAsState()

            LaunchedEffect(key1 = voiceResult) {
                viewModel.onEvent(TranslateEvent.SubmitVoiceResult(voiceResult))
                it.savedStateHandle["voiceResult"] = null
            }

            TranslateScreen(
                state = state,
                onEvent = { event ->
                    when (event) {
                        is TranslateEvent.RecordVoice -> {
                            navController.navigate(
                                Routes.VoiceToText.name + "/${state.fromLanguage.language.langCode}"
                            )
                        }

                        else -> viewModel.onEvent(event)
                    }
                }
            )
        }
        composable(
            route = Routes.VoiceToText.name + "/{languageCode}",
            arguments = listOf(
                navArgument("languageCode") {
                    type = NavType.StringType
                    defaultValue = "en"
                },
            )
        ) {
            val viewModel = hiltViewModel<AndroidVoiceToTextViewModel>()
            val state by viewModel.state.collectAsState()
            val languageCode = it.arguments?.getString("languageCode") ?: "en"
            VoiceToTextScreen(
                state = state,
                languageCode = languageCode,
                onResult = {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "voiceResult",
                        it
                    )
                    navController.popBackStack()
                },
                onEvent = { event ->
                    when (event) {
                        is VoiceToTextEvent.Close -> {
                            navController.popBackStack()
                        }

                        else -> viewModel.onEvent(event)
                    }
                }
            )
        }
    }
}