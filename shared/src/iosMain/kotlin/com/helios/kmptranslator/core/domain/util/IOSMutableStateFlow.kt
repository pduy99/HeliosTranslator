package com.helios.kmptranslator.core.domain.util

import kotlinx.coroutines.flow.MutableStateFlow

class IOSMutableStateFlow<T>(
    private val initialValue: T
) : CommonMutableStateFlow<T>(MutableStateFlow(initialValue)) {
}