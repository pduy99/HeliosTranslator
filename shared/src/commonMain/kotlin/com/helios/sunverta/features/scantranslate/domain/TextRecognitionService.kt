package com.helios.sunverta.features.scantranslate.domain

import com.helios.sunverta.core.domain.model.CommonImage

interface TextRecognitionService {

    suspend fun detectTextFromImage(image: CommonImage, languageCode: String): List<TextWithBound>
}