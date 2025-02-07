package com.helios.sunverta.android.features.scan.domain

import android.graphics.Bitmap
import android.graphics.Rect
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.helios.sunverta.core.domain.model.CommonImage
import com.helios.sunverta.core.domain.model.Language
import com.helios.sunverta.core.domain.usecase.TranslateUseCase
import com.helios.sunverta.core.result.Result
import com.helios.sunverta.features.scantranslate.domain.ImageTranslator
import com.helios.sunverta.features.scantranslate.domain.TextBlockData
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
    ): List<TextBlockData> = withContext(Dispatchers.IO) {
        val textBlocks = detectTextFromImage(image.bitmap)
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

    suspend fun detectTextFromImage(bitmap: Bitmap): List<TextBlockData> =
        suspendCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, 0)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(image)
                .addOnSuccessListener { text ->
                    val detectedTextBlocks = text.textBlocks.map { block ->
                        TextBlockData(block.text, block.boundingBox?.toComposeRect())
                    }
                    continuation.resume(detectedTextBlocks)
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
}

fun Rect.toComposeRect(): androidx.compose.ui.geometry.Rect {
    return androidx.compose.ui.geometry.Rect(
        this.left.toFloat(),
        this.top.toFloat(),
        this.right.toFloat(),
        this.bottom.toFloat()
    )
}

