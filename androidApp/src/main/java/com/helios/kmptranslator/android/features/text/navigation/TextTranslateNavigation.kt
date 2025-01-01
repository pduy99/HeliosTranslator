package com.helios.kmptranslator.android.features.text.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helios.kmptranslator.android.features.text.presentation.AndroidTranslateViewModel
import com.helios.kmptranslator.android.features.text.presentation.TextTranslateScreen
import com.helios.kmptranslator.features.translate.TranslateEvent

const val TEXT_TRANSLATE_ROUTE = "text_translate"

fun NavGraphBuilder.textTranslateScreen(
    onOpenHistoryScreen: () -> Unit,
    onOpenCameraTranslateScreen: () -> Unit,
    onOpenConversationTranslateScreen: () -> Unit
) {
    composable(route = TEXT_TRANSLATE_ROUTE) {

        val viewModel = hiltViewModel<AndroidTranslateViewModel>()
        val state by viewModel.state.collectAsState()

        TextTranslateScreen(
            state = state,
            onEvent = { event ->
                when (event) {
                    is TranslateEvent.OpenCameraTranslateScreen -> onOpenCameraTranslateScreen()
                    is TranslateEvent.OpenConversationTranslateScreen -> onOpenConversationTranslateScreen()
                    is TranslateEvent.OpenHistoryScreen -> onOpenHistoryScreen()
                    else -> viewModel.onEvent(event)
                }
            }
        )
    }

}