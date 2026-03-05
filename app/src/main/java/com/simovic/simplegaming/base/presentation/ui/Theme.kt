package com.simovic.simplegaming.base.presentation.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalExtendedTypography =
    staticCompositionLocalOf<ExtendedTypography> {
        error("No ExtendedTypography provided")
    }

val LocalExtendedColors =
    staticCompositionLocalOf<ExtendedColors> {
        error("No ExtendedColors provided")
    }

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    // Map custom colors to M3 slots
    val m3ColorScheme =
        if (darkTheme) {
            darkColorScheme(
                primary = extendedColors.brandPrimary,
                background = extendedColors.surfaceDeep,
                onBackground = extendedColors.textMain,
                surface = extendedColors.surfaceHigh,
                onSurface = extendedColors.textMain,
            )
        } else {
            lightColorScheme(
                primary = extendedColors.brandPrimary,
                background = extendedColors.surfaceDeep,
                onBackground = extendedColors.textMain,
                surface = extendedColors.surfaceHigh,
                onSurface = extendedColors.textMain,
            )
        }

    // Map custom typography to M3 slots
    val m3Typography =
        Typography(
            headlineLarge = AppExtendedTypography.head1,
            headlineMedium = AppExtendedTypography.head2,
            bodyLarge = AppExtendedTypography.body1,
            bodyMedium = AppExtendedTypography.body2,
            labelSmall = AppExtendedTypography.body7,
        )

    CompositionLocalProvider(
        LocalExtendedTypography provides AppExtendedTypography,
        LocalExtendedColors provides extendedColors,
    ) {
        MaterialTheme(
            colorScheme = m3ColorScheme,
            typography = m3Typography,
            content = content,
        )
    }
}

// Global Accessor Object
object AppTheme {
    val typo: ExtendedTypography @Composable get() = LocalExtendedTypography.current
    val color: ExtendedColors @Composable get() = LocalExtendedColors.current
}
