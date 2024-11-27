package com.helios.kmptranslator.android.features.texttranslate.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.helios.kmptranslator.android.features.texttranslate.presentation.AndroidTranslateViewModel
import com.helios.kmptranslator.android.features.texttranslate.presentation.TextTranslateScreen

const val TEXT_TRANSLATE_ROUTE = "text_translate"

fun NavController.navigateToTextTranslate(navOptions: NavOptions) {
    navigate(TEXT_TRANSLATE_ROUTE, navOptions)
}

fun NavGraphBuilder.textTranslateScreen() {
    composable(route = TEXT_TRANSLATE_ROUTE) {

        val viewModel = hiltViewModel<AndroidTranslateViewModel>()
        val state by viewModel.state.collectAsState()

        TextTranslateScreen(
            state = state,
            onEvent = { event ->
                viewModel.onEvent(event)
            }
        )
    }

}