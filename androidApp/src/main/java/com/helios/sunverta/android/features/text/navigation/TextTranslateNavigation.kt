package com.helios.sunverta.android.features.text.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helios.sunverta.android.features.text.presentation.AndroidTranslateViewModel
import com.helios.sunverta.android.features.text.presentation.TextTranslateScreen
import com.helios.sunverta.features.translate.TranslateEvent
import kotlinx.serialization.Serializable

@Serializable
object TextTranslateDestination

fun NavGraphBuilder.textTranslateScreen(
    onOpenHistoryScreen: () -> Unit,
    onOpenCameraTranslateScreen: () -> Unit,
    onOpenConversationTranslateScreen: () -> Unit
) {
    composable<TextTranslateDestination> {
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