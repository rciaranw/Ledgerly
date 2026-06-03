package com.ledgerly.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LedgerlyLightColorScheme = lightColorScheme(
    primary = LedgerlyTurquoise,
    onPrimary = Color.White,

    secondary = LedgerlyTurquoiseDark,
    onSecondary = Color.White,

    tertiary = LedgerlyTurquoise,
    onTertiary = Color.White,

    background = LedgerlyBackground,
    onBackground = LedgerlyPrimaryText,

    surface = LedgerlyBackground,
    onSurface = LedgerlyPrimaryText,

    surfaceVariant = LedgerlyCardBackground,
    onSurfaceVariant = LedgerlyPrimaryText,

    primaryContainer = LedgerlyTurquoise,
    onPrimaryContainer = Color.White,

    secondaryContainer = LedgerlyTurquoiseLight,
    onSecondaryContainer = LedgerlyPrimaryText,

    error = LedgerlyExpenseRed
)

@Composable
fun LedgerlyTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LedgerlyLightColorScheme,
        typography = Typography,
        content = content
    )
}