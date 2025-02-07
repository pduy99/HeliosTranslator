package com.helios.sunverta.android.app.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MicNone
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.ui.graphics.vector.ImageVector
import com.helios.sunverta.android.R

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val titleTextId: Int,
) {
    TEXT_TRANSLATE(
        selectedIcon = Icons.Rounded.Translate,
        unselectedIcon = Icons.Outlined.Translate,
        titleTextId = R.string.feature_text_translate_title,
    ),
    VOICE_TRANSLATE(
        selectedIcon = Icons.Rounded.Mic,
        unselectedIcon = Icons.Outlined.MicNone,
        titleTextId = R.string.feature_voice_translate_title,
    ),
    SCAN_TRANSLATE(
        selectedIcon = Icons.Rounded.PhotoCamera,
        unselectedIcon = Icons.Outlined.PhotoCamera,
        titleTextId = R.string.feature_scan_translate_title,
    ),
    SETTINGS(
        selectedIcon = Icons.Rounded.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        titleTextId = R.string.settings,
    ),
}