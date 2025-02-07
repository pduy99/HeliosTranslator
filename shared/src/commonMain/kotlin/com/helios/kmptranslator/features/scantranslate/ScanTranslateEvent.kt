package com.helios.kmptranslator.features.scantranslate

import com.helios.kmptranslator.core.domain.model.CommonImage

sealed class ScanTranslateEvent {

    data object CaptureImage : ScanTranslateEvent()

    data class TranslateImage(val image: CommonImage) : ScanTranslateEvent()

    data object ToggleTorch : ScanTranslateEvent()

    data object Reset : ScanTranslateEvent()
}