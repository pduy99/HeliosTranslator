package com.helios.sunverta.features.scantranslate

import com.helios.sunverta.core.domain.model.CommonImage
import com.helios.sunverta.core.presentation.UiLanguage
import com.helios.sunverta.features.scantranslate.domain.TextBlockData

data class ScanTranslateUiState(
    val isTranslating: Boolean = false,
    val capturedImage: CommonImage? = null,
    val isTorchEnable: Boolean = false,
    val translatedTextBlock: List<TextBlockData> = emptyList(),
    val fromLanguage: UiLanguage = UiLanguage.byCode("en"),
    val toLanguage: UiLanguage = UiLanguage.byCode("es")
)