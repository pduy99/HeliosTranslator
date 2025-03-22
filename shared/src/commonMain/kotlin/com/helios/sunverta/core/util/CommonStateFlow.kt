package com.helios.sunverta.core.util

import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
expect class CommonStateFlow<T>(flow: StateFlow<T>) : StateFlow<T> {
    override val value: T
    override suspend fun collect(collector: FlowCollector<T>): Nothing
    override val replayCache: List<T>
}

fun <T> StateFlow<T>.toCommonStateFlow() = CommonStateFlow(this)
