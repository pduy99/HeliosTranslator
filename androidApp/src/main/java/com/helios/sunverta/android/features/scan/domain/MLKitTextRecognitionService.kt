package com.helios.sunverta.android.features.scan.domain

import android.graphics.Rect
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.helios.sunverta.core.domain.model.CommonImage
import com.helios.sunverta.features.scantranslate.domain.TextRecognitionService
import com.helios.sunverta.features.scantranslate.domain.TextWithBound
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MLKitTextRecognitionService @Inject constructor() : TextRecognitionService {
    override suspend fun detectTextFromImage(
        image: CommonImage,
        languageCode: String
    ): List<TextWithBound> =
        suspendCoroutine { continuation ->
            val image = InputImage.fromBitmap(image.bitmap, 0)
            val recognizer = TextRecognition.getClient(getTextRecognitionOptions(languageCode))

            recognizer.process(image)
                .addOnSuccessListener { text ->
                    val detectedTextBlocks = mutableListOf<TextWithBound>()
                    text.textBlocks.forEach { block ->
                        block.lines.filter { it.recognizedLanguage == languageCode }
                            .forEach {
                                detectedTextBlocks.add(
                                    TextWithBound(
                                        it.text,
                                        it.boundingBox?.toComposeRect()
                                    )
                                )
                            }
                    }
                    continuation.resume(detectedTextBlocks)
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }

    private fun getTextRecognitionOptions(languageCode: String): TextRecognizerOptionsInterface {
        return when (languageCode) {
            "ja" -> JapaneseTextRecognizerOptions.Builder().build()
            "ko" -> KoreanTextRecognizerOptions.Builder().build()
            "hi", "mr", "sa" -> DevanagariTextRecognizerOptions.Builder().build()
            "zh" -> ChineseTextRecognizerOptions.Builder().build()
            else -> TextRecognizerOptions.DEFAULT_OPTIONS
        }
    }
}


fun Rect.toComposeRect(): androidx.compose.ui.geometry.Rect {
    return androidx.compose.ui.geometry.Rect(
        left = this.left.toFloat(),
        top = this.top.toFloat(),
        right = this.right.toFloat(),
        bottom = this.bottom.toFloat()
    )
}