package com.helios.kmptranslator.android.core.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.helios.kmptranslator.core.presentation.CommonColors

val AccentViolet = Color(CommonColors.AccentViolet)
val LightBlue = Color(CommonColors.LightBlue)
val LightBlueGrey = Color(CommonColors.LightBlueGrey)
val TextBlack = Color(CommonColors.TextBlack)
val DarkGrey = Color(CommonColors.DarkGrey)

val lightColors = lightColorScheme(
    primary = AccentViolet,
    background = LightBlueGrey,
    onPrimary = Color.White,
    onBackground = TextBlack,
    surface = Color.White,
    onSurface = TextBlack
)

val darkColors = darkColorScheme(
    primary = AccentViolet,
    background = DarkGrey,
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = DarkGrey,
)
