package com.helios.sunverta.core.presentation

import com.helios.sunverta.core.domain.model.Language

expect class UiLanguage {

    val language: Language

    companion object {
        fun byCode(languageCode: String): UiLanguage
        val allLanguages: List<UiLanguage>
    }
}