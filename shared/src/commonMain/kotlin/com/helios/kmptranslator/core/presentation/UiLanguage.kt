package com.helios.kmptranslator.core.presentation

import com.helios.kmptranslator.core.domain.language.Language

expect class UiLanguage {

    val language: Language

    companion object {
        fun byCode(languageCode: String): UiLanguage
        val allLanguages: List<UiLanguage>
    }
}