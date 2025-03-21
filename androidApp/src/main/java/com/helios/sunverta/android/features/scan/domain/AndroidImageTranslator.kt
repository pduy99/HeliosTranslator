package com.helios.sunverta.android.features.scan.domain

import com.helios.sunverta.core.domain.model.CommonImage
import com.helios.sunverta.core.domain.model.Language
import com.helios.sunverta.core.domain.usecase.TranslateUseCase
import com.helios.sunverta.core.result.Result
import com.helios.sunverta.features.scantranslate.domain.ImageTranslator
import com.helios.sunverta.features.scantranslate.domain.TextRecognitionService
import com.helios.sunverta.features.scantranslate.domain.TextWithBound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AndroidImageTranslator @Inject constructor(
    private val textRecognitionService: TextRecognitionService,
    private val translateUseCase: TranslateUseCase
) : ImageTranslator {

    override suspend fun translateImage(
        fromLanguage: Language,
        toLanguage: Language,
        image: CommonImage
    ): List<TextWithBound> = withContext(Dispatchers.IO) {
        val textBlocks = textRecognitionService.detectTextFromImage(image, fromLanguage.langCode)

        coroutineScope {
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
        }.filterNotNull()
    }
}

