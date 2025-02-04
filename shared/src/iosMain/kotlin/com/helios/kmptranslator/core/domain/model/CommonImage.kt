package com.helios.kmptranslator.core.domain.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import org.jetbrains.skia.Image
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret

actual class CommonImage(private val image: UIImage) {

    @OptIn(ExperimentalForeignApi::class)
    actual fun toByteArray(): ByteArray {
        return run {
            val imageData = UIImageJPEGRepresentation(image, COMPRESSION_QUALITY)
                ?: throw IllegalArgumentException("image data is null")
            val bytes = imageData.bytes ?: throw IllegalArgumentException("image bytes is null")
            val length = imageData.length

            val data: CPointer<ByteVar> = bytes.reinterpret()
            ByteArray(length.toInt()) { index -> data[index] }
        }

    }

    actual fun toImageBitmap(): ImageBitmap {
        val byteArray = toByteArray()
        return Image.makeFromEncoded(byteArray).toComposeImageBitmap()
    }

    private companion object {
        const val COMPRESSION_QUALITY = 0.99
    }
}