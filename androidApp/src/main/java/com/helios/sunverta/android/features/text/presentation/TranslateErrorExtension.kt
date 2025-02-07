package com.helios.sunverta.android.features.text.presentation

import com.helios.sunverta.android.R
import com.helios.sunverta.core.presentation.UiText
import com.helios.sunverta.features.translate.TranslateError

fun TranslateError.asUiText(): UiText {
    return when (this) {
        TranslateError.Network.SERVICE_UNAVAILABLE -> UiText.StringResource(R.string.service_unavailable)
        TranslateError.Network.CLIENT_ERROR -> UiText.StringResource(R.string.client_error)
        TranslateError.Network.SERVER_ERROR -> UiText.StringResource(R.string.server_error)
        TranslateError.Network.UNKNOWN_ERROR -> UiText.StringResource(R.string.unknown_error)
    }
}