package com.helios.sunverta.features.translate

import com.helios.sunverta.core.domain.util.Error

sealed interface TranslateError : Error {
    enum class Network : TranslateError {
        SERVICE_UNAVAILABLE,
        CLIENT_ERROR,
        SERVER_ERROR,
        UNKNOWN_ERROR
    }
}