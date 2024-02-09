package com.helios.kmptranslator.core.util

import com.helios.kmptranslator.core.util.CommonMutableStateFlow
import kotlinx.coroutines.flow.MutableStateFlow

class IOSMutableStateFlow<T>(
    private val initialValue: T
) : CommonMutableStateFlow<T>(MutableStateFlow(initialValue)) {
}