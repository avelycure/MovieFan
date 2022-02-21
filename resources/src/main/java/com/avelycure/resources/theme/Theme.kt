package com.avelycure.resources.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Indigo,
    primaryVariant = Signal,
    secondary = LightBlue,
    onSurface = Night,
    error = Karmin,
    onError = AlazarRed
)

private val LightColorPalette = lightColors(
    surface = Magnolia,
    onSurface = LightPurple,
    primary = LightBlue,
    secondary = Indigo,
    error = Karmin,
    onError = AlazarRed,
)

@Composable
fun MovieFanTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}