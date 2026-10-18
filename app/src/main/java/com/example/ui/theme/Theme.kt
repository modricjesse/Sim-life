package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val UnnDarkColorScheme = darkColorScheme(
    primary = LionGold,
    onPrimary = UnnDeepGreen,
    primaryContainer = UnnEmerald,
    onPrimaryContainer = LionGoldBright,
    secondary = UnnMintAccent,
    onSecondary = UnnDeepGreen,
    secondaryContainer = DarkCardElevated,
    onSecondaryContainer = TextIvory,
    tertiary = NsukkaClayRed,
    onTertiary = TextIvory,
    background = DarkCanvas,
    onBackground = TextIvory,
    surface = DarkSurface,
    onSurface = TextIvory,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextMutedSage,
    error = AlertCrimson,
    onError = Color.White
)

private val UnnLightColorScheme = lightColorScheme(
    primary = UnnForestGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6EFE2),
    onPrimaryContainer = UnnDeepGreen,
    secondary = LionAmber,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFF451A03),
    tertiary = NsukkaClayRed,
    onTertiary = Color.White,
    background = LightCanvas,
    onBackground = UnnDeepGreen,
    surface = LightSurface,
    onSurface = UnnDeepGreen,
    surfaceVariant = LightCard,
    onSurfaceVariant = Color(0xFF375847),
    error = AlertCrimson,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) UnnDarkColorScheme else UnnLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
