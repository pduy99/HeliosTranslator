package com.helios.kmptranslator.core.domain.model

enum class DeletionPeriod(val hours: Long) {
    TWELVE_HOURS(12),
    ONE_DAY(24),
    ONE_MONTH(24 * 30),
    NEVER(-1)
}