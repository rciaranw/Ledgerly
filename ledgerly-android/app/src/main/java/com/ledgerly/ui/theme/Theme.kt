package com.ledgerly.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LedgerlyDefaultColorScheme = darkColorScheme(
    primary = LedgerlyBrandTurquoise,
    onPrimary = LedgerlyBrandNavy,

    secondary = LedgerlyBrandTurquoiseLight,
    onSecondary = LedgerlyBrandNavy,

    tertiary = LedgerlyBrandTurquoise,
    onTertiary = LedgerlyBrandNavy,

    background = LedgerlyBrandNavy,
    onBackground = LedgerlyBrandOffWhite,

    surface = LedgerlyBrandNavy,
    onSurface = LedgerlyBrandOffWhite,

    surfaceVariant = LedgerlyBrandSlate,
    onSurfaceVariant = LedgerlyBrandOffWhite,

    primaryContainer = LedgerlyBrandTurquoise,
    onPrimaryContainer = LedgerlyBrandNavy,

    secondaryContainer = LedgerlyBrandSlate,
    onSecondaryContainer = LedgerlyBrandOffWhite,

    error = LedgerlyExpenseRed
)

private val LedgerlyWhiteColorScheme = lightColorScheme(
    primary = LedgerlyBrandTurquoise,
    onPrimary = Color.White,

    secondary = LedgerlyBrandSlate,
    onSecondary = Color.White,

    tertiary = LedgerlyBrandTurquoiseLight,
    onTertiary = LedgerlyWhiteText,

    background = LedgerlyWhiteBackground,
    onBackground = LedgerlyWhiteText,

    surface = LedgerlyWhiteBackground,
    onSurface = LedgerlyWhiteText,

    surfaceVariant = LedgerlyWhiteSurface,
    onSurfaceVariant = LedgerlyWhiteText,

    primaryContainer = LedgerlyBrandTurquoise,
    onPrimaryContainer = Color.White,

    secondaryContainer = LedgerlyWhiteSurface,
    onSecondaryContainer = LedgerlyWhiteText,

    error = LedgerlyExpenseRed
)

private val LedgerlyDarkColorScheme = darkColorScheme(
    primary = LedgerlyBrandTurquoise,
    onPrimary = Color.Black,

    secondary = LedgerlyBrandTurquoiseLight,
    onSecondary = Color.Black,

    tertiary = LedgerlyBrandTurquoise,
    onTertiary = Color.Black,

    background = LedgerlyDarkBackground,
    onBackground = LedgerlyDarkText,

    surface = LedgerlyDarkBackground,
    onSurface = LedgerlyDarkText,

    surfaceVariant = LedgerlyDarkSurface,
    onSurfaceVariant = LedgerlyDarkText,

    primaryContainer = LedgerlyBrandTurquoise,
    onPrimaryContainer = Color.Black,

    secondaryContainer = LedgerlyDarkSurface,
    onSecondaryContainer = LedgerlyDarkText,

    error = LedgerlyExpenseRed
)

@Composable
fun LedgerlyTheme(
    themeName: String = "Default",
    content: @Composable () -> Unit
) {
    val colorScheme =
        when (themeName) {
            "White" -> LedgerlyWhiteColorScheme
            "Dark" -> LedgerlyDarkColorScheme
            "Turquoise" -> LedgerlyDefaultColorScheme
            "Light" -> LedgerlyWhiteColorScheme
            else -> LedgerlyDefaultColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}