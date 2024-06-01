package com.helios.kmptranslator.core.presentation

actual sealed interface UiText {
    data class DynamicString(val value: String) : UiText
}