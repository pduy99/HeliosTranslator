package com.helios.kmptranslator.android.features.text.presentation

import com.helios.kmptranslator.android.R
import com.helios.kmptranslator.core.presentation.UiText
import com.helios.kmptranslator.features.translate.TranslateError

fun TranslateError.asUiText(): UiText {
    return when (this) {
        TranslateError.Network.SERVICE_UNAVAILABLE -> UiText.StringResource(R.string.service_unavailable)
        TranslateError.Network.CLIENT_ERROR -> UiText.StringResource(R.string.client_error)
        TranslateError.Network.SERVER_ERROR -> UiText.StringResource(R.string.server_error)
        TranslateError.Network.UNKNOWN_ERROR -> UiText.StringResource(R.string.unknown_error)
    }
}