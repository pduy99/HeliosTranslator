package com.helios.sunverta.core.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
actual open class CommonMutableStateFlow<T> actual constructor(private val flow: MutableStateFlow<T>) :
    CommonStateFlow<T>(flow), MutableStateFlow<T> {

    actual override var value: T
        get() = super.value
        set(value) {
            flow.value = value
        }

    actual override val subscriptionCount: StateFlow<Int>
        get() = flow.subscriptionCount

    actual override fun compareAndSet(expect: T, update: T): Boolean {
        return flow.compareAndSet(expect, update)
    }

    @ExperimentalCoroutinesApi
    actual override fun resetReplayCache() {
        flow.resetReplayCache()
    }

    actual override fun tryEmit(value: T): Boolean {
        return flow.tryEmit(value)
    }

    actual override suspend fun emit(value: T) {
        flow.emit(value)
    }

}