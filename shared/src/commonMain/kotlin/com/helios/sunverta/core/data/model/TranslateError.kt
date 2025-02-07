package com.helios.sunverta.core.data.model

import com.helios.sunverta.core.result.Error

sealed interface TranslateError : Error {
    enum class Network : TranslateError {
        SERVICE_UNAVAILABLE,
        CLIENT_ERROR,
        SERVER_ERROR,
        UNKNOWN_ERROR
    }
}