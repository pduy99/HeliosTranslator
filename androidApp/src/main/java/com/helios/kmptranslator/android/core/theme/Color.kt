package com.helios.kmptranslator.android.core.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.helios.kmptranslator.core.presentation.CommonColors

val AccentViolet = Color(CommonColors.AccentViolet)
val LightBlue = Color(CommonColors.LightBlue)
val LightBlueGrey = Color(CommonColors.LightBlueGrey)
val TextBlack = Color(CommonColors.TextBlack)


val Black = Color(0xFF000000)
val Yellow = Color(0xFFFFEB3B)

val md_theme_dark_primary = Black
val md_theme_dark_onPrimary = Color(0xFFFFFFFF)
val md_theme_dark_primaryContainer = Color(0xFF1E1E1E)
val md_theme_dark_onPrimaryContainer = Yellow

val md_theme_dark_secondary = Color(0xFF424242)
val md_theme_dark_onSecondary = Color(0xFFFFFFFF)
val md_theme_dark_secondaryContainer = Color(0xFF616161)
val md_theme_dark_onSecondaryContainer = Color(0xFFFAFAFA)

val md_theme_dark_tertiary = Color(0xFF303F9F)
val md_theme_dark_onTertiary = Color(0xFFFFFFFF)
val md_theme_dark_tertiaryContainer = Color(0xFF3F51B5)
val md_theme_dark_onTertiaryContainer = Color(0xFFBBDEFB)

val md_theme_dark_error = Color(0xFFCF6679)
val md_theme_dark_errorContainer = Color(0xFFB00020)
val md_theme_dark_onError = Color(0xFFFFFFFF)
val md_theme_dark_onErrorContainer = Color(0xFF370617)

val md_theme_dark_background = Color(0xFF121212)
val md_theme_dark_onBackground = Color(0xFFE0E0E0)
val md_theme_dark_surface = Color(0xFF121212)
val md_theme_dark_onSurface = Color(0xFFE0E0E0)
val md_theme_dark_surfaceVariant = Color(0xFF37474F)
val md_theme_dark_onSurfaceVariant = Color(0xFFB0BEC5)
val md_theme_dark_outline = Color(0xFFB0BEC5)
val md_theme_dark_inverseOnSurface = Color(0xFF121212)
val md_theme_dark_inverseSurface = Color(0xFFE0E0E0)
val md_theme_dark_inversePrimary = Yellow
val md_theme_dark_surfaceTint = Yellow
val md_theme_dark_outlineVariant = Color(0xFF37474F)
val md_theme_dark_scrim = Color(0xFF000000)

val lightColors = lightColorScheme(
    primary = AccentViolet,
    background = LightBlueGrey,
    onPrimary = Color.White,
    onBackground = TextBlack,
    surface = Color.White,
    onSurface = TextBlack
)

val darkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)



