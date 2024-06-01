package com.helios.kmptranslator.core.presentation

import androidx.annotation.StringRes

actual sealed interface UiText {
    data class DynamicString(val value: String) : UiText
    class StringResource(@StringRes val id: Int, val args: Array<Any> = arrayOf()) : UiText
}