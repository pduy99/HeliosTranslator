package com.helios.sunverta.core.domain.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

actual class CommonImage(val bitmap: Bitmap) {
    actual fun toByteArray(): ByteArray {
        return run {
            val byteArrayOutputStream = ByteArrayOutputStream()
            @Suppress("MagicNumber") bitmap.compress(
                Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream
            )
            byteArrayOutputStream.toByteArray()
        }
    }

    actual fun toImageBitmap(): ImageBitmap {
        val byteArray = toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
    }
}