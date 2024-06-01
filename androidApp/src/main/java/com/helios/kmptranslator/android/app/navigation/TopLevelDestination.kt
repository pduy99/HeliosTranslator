package com.helios.kmptranslator.android.app.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.rounded.DocumentScanner
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.ui.graphics.vector.ImageVector
import com.helios.kmptranslator.android.R

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val titleTextId: Int,
) {
    TEXT_TRANSLATE(
        selectedIcon = Icons.Rounded.TextFields,
        unselectedIcon = Icons.Outlined.TextFields,
        titleTextId = R.string.feature_text_translate_title,
    ),
    VOICE_TRANSLATE(
        selectedIcon = Icons.Rounded.Mic,
        unselectedIcon = Icons.Outlined.Mic,
        titleTextId = R.string.feature_voice_translate_title,
    ),
    SCAN_TRANSLATE(
        selectedIcon = Icons.Rounded.DocumentScanner,
        unselectedIcon = Icons.Outlined.DocumentScanner,
        titleTextId = R.string.feature_scan_translate_title,
    ),
    SETTINGS(
        selectedIcon = Icons.Rounded.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        titleTextId = R.string.settings,
    ),
}