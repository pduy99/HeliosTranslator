package com.helios.sunverta.features.scantranslate.domain

import androidx.compose.ui.geometry.Rect

data class TextBlockData(
    val text: String,
    val boundingBox: Rect?
)