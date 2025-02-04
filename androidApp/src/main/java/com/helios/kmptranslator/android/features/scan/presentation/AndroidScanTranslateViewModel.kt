package com.helios.kmptranslator.android.features.scan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helios.kmptranslator.features.scantranslate.ScanTranslateEvent
import com.helios.kmptranslator.features.scantranslate.ScanTranslateViewModel
import com.helios.kmptranslator.features.scantranslate.domain.ImageTranslator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidScanTranslateViewModel @Inject constructor(
    private val imageTranslator: ImageTranslator,
) : ViewModel() {

    private val viewModel = ScanTranslateViewModel(
        imageTranslator,
        viewModelScope
    )

    val uiState = viewModel.state

    fun onEvent(event: ScanTranslateEvent) {
        viewModel.onEvent(event)
    }
}