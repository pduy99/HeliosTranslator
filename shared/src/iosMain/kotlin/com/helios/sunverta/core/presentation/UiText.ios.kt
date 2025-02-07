package com.helios.sunverta.core.presentation

actual sealed interface UiText {
    data class DynamicString(val value: String) : UiText
}