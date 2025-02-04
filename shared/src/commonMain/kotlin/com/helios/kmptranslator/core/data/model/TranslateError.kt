<<<<<<<< HEAD:shared/src/commonMain/kotlin/com/helios/sunverta/features/translate/TranslateError.kt
package com.helios.sunverta.features.translate

import com.helios.sunverta.core.domain.util.Error
========
package com.helios.kmptranslator.core.data.model

import com.helios.kmptranslator.core.result.Error
>>>>>>>> dd3abf0 (Implement ScanTranslate feature):shared/src/commonMain/kotlin/com/helios/kmptranslator/core/data/model/TranslateError.kt

sealed interface TranslateError : Error {
    enum class Network : TranslateError {
        SERVICE_UNAVAILABLE,
        CLIENT_ERROR,
        SERVER_ERROR,
        UNKNOWN_ERROR
    }
}