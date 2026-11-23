package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val CairoFontFamily = FontFamily(
    Font(R.font.cairo, FontWeight.Normal),
    Font(R.font.cairo, FontWeight.Medium),
    Font(R.font.cairo, FontWeight.SemiBold),
    Font(R.font.cairo, FontWeight.Bold),
    Font(R.font.cairo, FontWeight.ExtraBold)
)

val AmiriFontFamily = FontFamily(
    Font(R.font.amiri, FontWeight.Normal),
    Font(R.font.amiri, FontWeight.Bold)
)

fun buildPilgrimTypography(scale: Float = 1f): Typography = Typography(
    displaySmall = TextStyle(
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = (28f * scale).sp,
        lineHeight = (36f * scale).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = (22f * scale).sp,
        lineHeight = (30f * scale).sp
    ),
    titleLarge = TextStyle(
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = (18f * scale).sp,
        lineHeight = (26f * scale).sp
    ),
    titleMedium = TextStyle(
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = (15.5f * scale).sp,
        lineHeight = (22f * scale).sp
    ),
    bodyLarge = TextStyle(
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = (15f * scale).sp,
        lineHeight = (24f * scale).sp
    ),
    bodyMedium = TextStyle(
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = (13.5f * scale).sp,
        lineHeight = (21f * scale).sp
    ),
    bodySmall = TextStyle(
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = (12f * scale).sp,
        lineHeight = (18f * scale).sp
    ),
    labelLarge = TextStyle(
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = (13f * scale).sp,
        lineHeight = (18f * scale).sp
    ),
    labelSmall = TextStyle(
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = (11f * scale).sp,
        lineHeight = (15f * scale).sp
    )
)
