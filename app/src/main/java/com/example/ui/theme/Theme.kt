package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = EmeraldLight,
    onPrimaryContainer = Color.White,
    secondary = SacredGold,
    onSecondary = Color(0xFF161A18),
    secondaryContainer = Color(0xFFF6ECD5),
    onSecondaryContainer = Color(0xFF4A3712),
    tertiary = EmeraldLight,
    onTertiary = Color.White,
    background = WarmSandBg,
    onBackground = DeepInk,
    surface = CreamSurface,
    onSurface = DeepInk,
    surfaceVariant = Color(0xFFF3F0E8),
    onSurfaceVariant = MutedSage,
    outline = SandBorder,
    error = StatusDanger,
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = SacredGold,
    onPrimary = Color(0xFF161A18),
    primaryContainer = EmeraldNight,
    onPrimaryContainer = Color.White,
    secondary = SacredGoldLight,
    onSecondary = Color(0xFF161A18),
    secondaryContainer = Color(0xFF2B2415),
    onSecondaryContainer = SacredGoldBright,
    tertiary = EmeraldNightLight,
    onTertiary = Color.White,
    background = NightBg,
    onBackground = NightText,
    surface = NightCard,
    onSurface = NightText,
    surfaceVariant = Color(0xFF1E2824),
    onSurfaceVariant = NightMuted,
    outline = NightBorder,
    error = Color(0xFFE57373),
    onError = Color(0xFF2B0B09)
)

val PilgrimShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp)
)

@Composable
fun PilgrimCompanionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    elderMode: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val typography = buildPilgrimTypography(scale = if (elderMode) 1.18f else 1.0f)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = PilgrimShapes,
        content = content
    )
}
