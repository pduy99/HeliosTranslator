package com.helios.sunverta.core.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
expect class CommonMutableStateFlow<T>(flow: MutableStateFlow<T>) : MutableStateFlow<T> {
    override fun compareAndSet(expect: T, update: T): Boolean
    override var value: T
    override suspend fun collect(collector: FlowCollector<T>): Nothing
    override val replayCache: List<T>
    override suspend fun emit(value: T)

    @ExperimentalCoroutinesApi
    override fun resetReplayCache()
    override fun tryEmit(value: T): Boolean
    override val subscriptionCount: StateFlow<Int>
}

fun <T> MutableStateFlow<T>.toCommonMutableStateFlow() = CommonMutableStateFlow(this)