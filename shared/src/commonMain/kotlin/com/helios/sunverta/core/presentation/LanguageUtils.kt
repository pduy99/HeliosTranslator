package com.helios.sunverta.core.presentation

object LanguageUtils {
    fun getFlagEmoji(languageCode: String): String {
        val code = languageCode.lowercase()

        // 1. Try direct match (e.g. "en-us", "zh-hant")
        languageToCountryMap[code]?.let {
            return countryCodeToEmoji(it)
        }

        // 2. Fallback to base language (e.g. "fr-ca" -> "fr" -> "FR")
        val baseLanguage = code.split("-", "_").firstOrNull()
        languageToCountryMap[baseLanguage]?.let {
            return countryCodeToEmoji(it)
        }

        return "🌐"
    }

    private val languageToCountryMap = mapOf(
        // API Specific Overrides
        "en-us" to "US",
        "en-gb" to "GB",
        "es-419" to "MX", // Latin American Spanish -> Mexico
        "pt-br" to "BR",
        "pt-pt" to "PT",
        "zh-hans" to "CN", // Script code 'Hans' (Simplified)
        "zh-hant" to "TW", // Script code 'Hant' (Traditional) -> Taiwan flag

        // Base Languages
        "af" to "ZA",
        "am" to "ET",
        "ar" to "SA",
        "as" to "IN",
        "az" to "AZ",
        "be" to "BY",
        "bg" to "BG",
        "bn" to "BD",
        "bs" to "BA",
        "ca" to "ES",
        "cs" to "CZ",
        "cy" to "GB",
        "da" to "DK",
        "de" to "DE",
        "el" to "GR",
        "en" to "GB",
        "es" to "ES",
        "et" to "EE",
        "eu" to "ES",
        "fa" to "IR",
        "fi" to "FI",
        "fr" to "FR",
        "ga" to "IE",
        "gl" to "ES",
        "gu" to "IN",
        "he" to "IL",
        "hi" to "IN",
        "hr" to "HR",
        "hu" to "HU",
        "hy" to "AM",
        "id" to "ID",
        "is" to "IS",
        "it" to "IT",
        "ja" to "JP",
        "ka" to "GE",
        "kk" to "KZ",
        "km" to "KH",
        "kn" to "IN",
        "ko" to "KR",
        "ky" to "KG",
        "lo" to "LA",
        "lt" to "LT",
        "lv" to "LV",
        "mk" to "MK",
        "ml" to "IN",
        "mn" to "MN",
        "mr" to "IN",
        "ms" to "MY",
        "my" to "MM",
        "nb" to "NO",
        "ne" to "NP",
        "nl" to "NL",
        "nn" to "NO",
        "no" to "NO",
        "or" to "IN",
        "pa" to "IN",
        "pl" to "PL",
        "ps" to "AF",
        "pt" to "PT",
        "ro" to "RO",
        "ru" to "RU",
        "si" to "LK",
        "sk" to "SK",
        "sl" to "SI",
        "sq" to "AL",
        "sr" to "RS",
        "sv" to "SE",
        "sw" to "TZ",
        "ta" to "IN",
        "te" to "IN",
        "th" to "TH",
        "tl" to "PH",
        "tr" to "TR",
        "uk" to "UA",
        "ur" to "PK",
        "uz" to "UZ",
        "vi" to "VN",
        "zh" to "CN",
        "zt" to "TW",
        "zu" to "ZA"
    )

    private fun countryCodeToEmoji(countryCode: String): String {
        val builder = StringBuilder()
        for (char in countryCode.uppercase()) {
            val base = 127397 // 0x1F1E6 - 'A' (65)
            val codePoint = char.code + base

            // Convert codePoint to surrogate pair
            val high = ((codePoint - 0x10000) / 0x400) + 0xD800
            val low = ((codePoint - 0x10000) % 0x400) + 0xDC00

            builder.append(high.toChar())
            builder.append(low.toChar())
        }
        return builder.toString()
    }
}
