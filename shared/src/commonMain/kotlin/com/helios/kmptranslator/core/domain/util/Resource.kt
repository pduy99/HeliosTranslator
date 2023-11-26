package com.helios.kmptranslator.core.domain.util

sealed class Resource<out T> {

    data class Success<out T>(val data: T): Resource<T>()

    data class Error(val throwable: Throwable): Resource<Nothing>()
}