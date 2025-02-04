package com.helios.kmptranslator.features.scantranslate

import com.helios.kmptranslator.core.domain.model.CommonImage

sealed class ScanTranslateEvent {

    data class CaptureImage(val image: CommonImage) : ScanTranslateEvent()

    data object Reset : ScanTranslateEvent()
}