package com.helios.sunverta.features.scantranslate

import com.helios.sunverta.core.domain.model.CommonImage

sealed class ScanTranslateEvent {

    data object CaptureImage : ScanTranslateEvent()

    data class TranslateImage(val image: CommonImage) : ScanTranslateEvent()

    data object ToggleTorch : ScanTranslateEvent()

    data object Reset : ScanTranslateEvent()
}