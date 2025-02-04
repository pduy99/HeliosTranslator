package com.helios.kmptranslator.core.domain.model

import androidx.compose.ui.graphics.ImageBitmap

expect class CommonImage {
    fun toByteArray(): ByteArray
    fun toImageBitmap(): ImageBitmap
}