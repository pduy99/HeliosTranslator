<<<<<<<< HEAD:shared/src/commonMain/kotlin/com/helios/sunverta/core/domain/util/Result.kt
package com.helios.sunverta.core.domain.util
========
package com.helios.kmptranslator.core.result
>>>>>>>> dd3abf0 (Implement ScanTranslate feature):shared/src/commonMain/kotlin/com/helios/kmptranslator/core/result/Result.kt

typealias RootError = Error

sealed interface Result<out D, out E : RootError> {
    data class Success<out D, out E : RootError>(val data: D) : Result<D, E>
    data class Failure<out D, out E : RootError>(val error: E) : Result<D, E>
}