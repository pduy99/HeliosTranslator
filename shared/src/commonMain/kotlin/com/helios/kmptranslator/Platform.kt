package com.helios.kmptranslator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform