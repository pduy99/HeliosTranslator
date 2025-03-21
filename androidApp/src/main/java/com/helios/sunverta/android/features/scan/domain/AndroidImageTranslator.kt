package com.helios.sunverta.android.features.scan.domain

import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.helios.sunverta.core.domain.model.CommonImage
import com.helios.sunverta.core.domain.model.Language
import com.helios.sunverta.core.domain.usecase.TranslateUseCase
import com.helios.sunverta.core.result.Result
import com.helios.sunverta.features.scantranslate.domain.ImageTranslator
import com.helios.sunverta.features.scantranslate.domain.TextWithBound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AndroidImageTranslator @Inject constructor(
    private val translateUseCase: TranslateUseCase
) : ImageTranslator {

    override suspend fun translateImage(
        fromLanguage: Language,
        toLanguage: Language,
        image: CommonImage
    ): List<TextWithBound> = withContext(Dispatchers.IO) {
        val textBlocks = detectTextFromImage(image.bitmap, fromLanguage.langCode)
        textBlocks
        val translatedTextBlocks = coroutineScope {
            textBlocks.map { block ->
                async {
                    val translateResult = translateUseCase.execute(
                        fromLanguage = fromLanguage,
                        toLanguage = toLanguage,
                        fromText = block.text
                    )
                    if (translateResult is Result.Success) {
                        block.copy(text = translateResult.data)
                    } else {
                        null
                    }
                }
            }.awaitAll()
        }
        translatedTextBlocks.filterNotNull()
    }

    suspend fun detectTextFromImage(bitmap: Bitmap, languageCode: String): List<TextWithBound> =
        suspendCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, 0)
            val recognizer = TextRecognition.getClient(getTextRecognitionOptions(languageCode))

            recognizer.process(image)
                .addOnSuccessListener { text ->
                    val detectedTextBlocks = mutableListOf<TextWithBound>()
                    text.textBlocks.forEach { block ->
                        block.lines.filter { it.confidence >= 0.5f && it.recognizedLanguage == languageCode }
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
        val (optionName, options) = when (languageCode) {
            "ja" -> "JapaneseTextRecognizerOptions" to JapaneseTextRecognizerOptions.Builder()
                .build()

            "ko" -> "KoreanTextRecognizerOptions" to KoreanTextRecognizerOptions.Builder().build()
            "hi", "mr", "sa" -> "DevanagariTextRecognizerOptions" to DevanagariTextRecognizerOptions.Builder()
                .build()

            "zh" -> "ChineseTextRecognizerOptions" to ChineseTextRecognizerOptions.Builder().build()
            else -> "DefaultTextRecognizerOptions" to TextRecognizerOptions.DEFAULT_OPTIONS
        }

        Log.d("TextRecognitionOptions", "Using $optionName for language code: $languageCode")
        return options
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

