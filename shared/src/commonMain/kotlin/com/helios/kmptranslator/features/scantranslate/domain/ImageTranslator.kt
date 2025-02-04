package com.helios.kmptranslator.features.scantranslate.domain

import com.helios.kmptranslator.core.domain.model.CommonImage
import com.helios.kmptranslator.core.domain.model.Language

interface ImageTranslator {

    suspend fun translateImage(fromLanguage: Language, toLanguage: Language, image: CommonImage): List<TextBlockData>
}