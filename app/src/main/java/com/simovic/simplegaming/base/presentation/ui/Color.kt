@file:Suppress("MagicNumber")

package com.simovic.simplegaming.base.presentation.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Primitive Palette Definitions
val PrimaryLight = Color(0xFF0061A4)
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFD1E4FF)
val OnPrimaryContainerLight = Color(0xFF001D36)

val SecondaryLight = Color(0xFF535F70)
val OnSecondaryLight = Color(0xFFFFFFFF)

val ErrorLight = Color(0xFFBA1A1A)
val OnErrorLight = Color(0xFFFFFFFF)

val BackgroundLight = Color(0xFFFDFCFF)
val OnBackgroundLight = Color(0xFF1A1C1E)
val SurfaceLight = Color(0xFFFDFCFF)
val OnSurfaceLight = Color(0xFF1A1C1E)

val PrimaryDark = Color(0xFF9ECAFF)
val OnPrimaryDark = Color(0xFF003258)
val PrimaryContainerDark = Color(0xFF00497D)
val OnPrimaryContainerDark = Color(0xFFD1E4FF)

val BackgroundDark = Color(0xFF1A1C1E)
val OnBackgroundDark = Color(0xFFE2E2E6)
val SurfaceDark = Color(0xFF1A1C1E)
val OnSurfaceDark = Color(0xFFE2E2E6)

// Semantic Palette Additions (Missing from your list but needed for ExtendedColors)
val SuccessLight = Color(0xFF2E7D32)
val WarningLight = Color(0xFFF9A825)
val TextMutedLight = Color(0xFF74777F)
val DividerLight = Color(0xFFC4C7C8)

val SuccessDark = Color(0xFF81C784)
val WarningDark = Color(0xFFFFF176)
val TextMutedDark = Color(0xFF8E9199)
val DividerDark = Color(0xFF444748)
val SurfaceHighDark = Color(0xFF2D2F31)

@Immutable
data class ExtendedColors(
    val brandPrimary: Color,
    val brandSecondary: Color,
    val success: Color,
    val warning: Color,
    val error: Color,
    val surfaceDeep: Color,
    val surfaceHigh: Color,
    val textMain: Color,
    val textMuted: Color,
    val divider: Color,
)

val LightExtendedColors =
    ExtendedColors(
        brandPrimary = PrimaryLight,
        brandSecondary = SecondaryLight,
        success = SuccessLight,
        warning = WarningLight,
        error = ErrorLight,
        surfaceDeep = BackgroundLight,
        surfaceHigh = SurfaceLight,
        textMain = OnBackgroundLight,
        textMuted = TextMutedLight,
        divider = DividerLight,
    )

val DarkExtendedColors =
    ExtendedColors(
        brandPrimary = PrimaryDark,
        brandSecondary = BackgroundDark,
        success = SuccessDark,
        warning = WarningDark,
        error = ErrorLight,
        surfaceDeep = BackgroundDark,
        surfaceHigh = SurfaceHighDark,
        textMain = OnBackgroundDark,
        textMuted = TextMutedDark,
        divider = DividerDark,
    )
