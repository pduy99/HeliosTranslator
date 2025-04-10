package com.helios.sunverta.android.core.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val md_theme_dark_primary = Color(0xFF434343)
val md_theme_dark_onPrimary = Color(0xFFF6F6F6)

val md_theme_inverse_primary = Color(0xFF296CE4)

val md_theme_primary_container = Color(0xFFADC6FF) // FAB
val md_theme_on_primary_container = Color(0xFF102E60)


val md_theme_dark_background = Color.Black
val md_theme_dark_onBackground = Color.White
val md_theme_dark_surface = Color(0xFF2E2E2E)
val md_theme_dark_onSurface = Color(0xFFA6A6A6)
val md_theme_surface_variant = Color(0xFFE6F7FF)
val md_theme_on_surface_container = Color(0xFF1A1A1A)
val md_theme_inverse_on_surface = Color(0xFFE6F7FF)

val darkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_surface_variant,
    onSurfaceVariant = md_theme_on_surface_container,
    inverseOnSurface = md_theme_inverse_on_surface,
    inversePrimary = md_theme_inverse_primary,
    primaryContainer = md_theme_primary_container,
    onPrimaryContainer = md_theme_on_primary_container
)



