package com.helios.sunverta.features.scantranslate.domain

import com.helios.sunverta.core.domain.model.CommonImage
import com.helios.sunverta.core.domain.model.Language

interface ImageTranslator {

    suspend fun translateImage(
        fromLanguage: Language,
        toLanguage: Language,
        image: CommonImage
    ): List<TextBlockData>
}