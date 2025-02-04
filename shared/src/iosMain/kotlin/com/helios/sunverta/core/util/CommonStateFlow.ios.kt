package com.helios.sunverta.core.util

import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
actual open class CommonStateFlow<T> actual constructor(private val flow: StateFlow<T>) :
    CommonFlow<T>(flow),
    StateFlow<T> {

    actual override val replayCache: List<T>
        get() = flow.replayCache
    actual override val value: T
        get() = flow.value

    actual override suspend fun collect(collector: FlowCollector<T>): Nothing {
        flow.collect(collector)
    }

}