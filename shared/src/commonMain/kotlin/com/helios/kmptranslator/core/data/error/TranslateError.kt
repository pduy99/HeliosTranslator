package com.helios.kmptranslator.core.data.error

enum class TranslateError {
    SERVICE_UNAVAILABLE,
    CLIENT_ERROR,
    SERVER_ERROR,
    UNKNOWN_ERROR
}

class TranslateException(val error: TranslateError) : Exception(
    "An error occurred when translating: $error"
)