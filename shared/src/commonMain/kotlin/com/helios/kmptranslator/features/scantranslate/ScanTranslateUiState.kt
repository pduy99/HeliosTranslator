package com.helios.kmptranslator.features.scantranslate

import com.helios.kmptranslator.core.domain.model.CommonImage
import com.helios.kmptranslator.core.presentation.UiLanguage
import com.helios.kmptranslator.features.scantranslate.domain.TextBlockData

data class ScanTranslateUiState(
    val isTranslating: Boolean = false,
    val capturedImage: CommonImage? = null,
    val isTorchEnable: Boolean = false,
    val translatedTextBlock: List<TextBlockData> = emptyList(),
    val fromLanguage: UiLanguage = UiLanguage.byCode("en"),
    val toLanguage: UiLanguage = UiLanguage.byCode("es")
)