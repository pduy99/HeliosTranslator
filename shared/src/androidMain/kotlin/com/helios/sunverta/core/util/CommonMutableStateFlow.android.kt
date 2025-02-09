package com.helios.sunverta.core.util

import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
actual class CommonMutableStateFlow<T> actual constructor(private val flow: MutableStateFlow<T>) :
    MutableStateFlow<T> by flow