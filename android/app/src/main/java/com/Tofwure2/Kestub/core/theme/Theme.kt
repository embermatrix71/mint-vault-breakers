package com.Tofwure2.Kestub.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val VaultColorScheme = darkColorScheme(
    primary = MintPrimary,
    onPrimary = MintInk,
    primaryContainer = MintDark,
    onPrimaryContainer = MintInk,
    secondary = GoldAccent,
    onSecondary = MintInk,
    secondaryContainer = SurfaceAlt,
    onSecondaryContainer = TextPrimary,
    tertiary = PlatePrime,
    onTertiary = MintInk,
    background = BackgroundDeep,
    onBackground = TextPrimary,
    surface = SurfaceBase,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = TextSecondary,
    outline = TextTertiary,
    error = AlarmRed,
    onError = MintInk
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = VaultColorScheme,
        typography = AppTypography,
        content = content
    )
}
