package com.ledgerly.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LedgerlyDefaultColorScheme =
    darkColorScheme(
        primary =
            LedgerlyBrandTurquoise,
        onPrimary =
            LedgerlyBrandNavy,
        secondary =
            LedgerlyBrandTurquoiseLight,
        onSecondary =
            LedgerlyBrandNavy,
        tertiary =
            LedgerlyBrandTurquoise,
        onTertiary =
            LedgerlyBrandNavy,
        background =
            LedgerlyBrandNavy,
        onBackground =
            LedgerlyBrandOffWhite,
        surface =
            LedgerlyBrandNavy,
        onSurface =
            LedgerlyBrandOffWhite,
        surfaceVariant =
            LedgerlyBrandSlate,
        onSurfaceVariant =
            LedgerlyBrandOffWhite,
        primaryContainer =
            LedgerlyBrandTurquoise,
        onPrimaryContainer =
            LedgerlyBrandNavy,
        secondaryContainer =
            LedgerlyBrandSlate,
        onSecondaryContainer =
            LedgerlyBrandOffWhite,
        error =
            LedgerlyExpenseRed
    )

private val LedgerlyLightColorScheme =
    lightColorScheme(
        primary =
            LedgerlyBrandTurquoise,
        onPrimary =
            Color.White,
        secondary =
            LedgerlyBrandSlate,
        onSecondary =
            Color.White,
        tertiary =
            LedgerlyBrandTurquoiseLight,
        onTertiary =
            LedgerlyWhiteText,
        background =
            LedgerlyWhiteBackground,
        onBackground =
            LedgerlyWhiteText,
        surface =
            LedgerlyWhiteBackground,
        onSurface =
            LedgerlyWhiteText,
        surfaceVariant =
            LedgerlyWhiteSurface,
        onSurfaceVariant =
            LedgerlyWhiteText,
        primaryContainer =
            LedgerlyBrandTurquoise,
        onPrimaryContainer =
            Color.White,
        secondaryContainer =
            LedgerlyWhiteSurface,
        onSecondaryContainer =
            LedgerlyWhiteText,
        error =
            LedgerlyExpenseRed
    )

private val LedgerlyDarkColorScheme =
    darkColorScheme(
        primary =
            LedgerlyBrandTurquoise,
        onPrimary =
            Color.Black,
        secondary =
            LedgerlyBrandTurquoiseLight,
        onSecondary =
            Color.Black,
        tertiary =
            LedgerlyBrandTurquoise,
        onTertiary =
            Color.Black,
        background =
            LedgerlyDarkBackground,
        onBackground =
            LedgerlyDarkText,
        surface =
            LedgerlyDarkBackground,
        onSurface =
            LedgerlyDarkText,
        surfaceVariant =
            LedgerlyDarkSurface,
        onSurfaceVariant =
            LedgerlyDarkText,
        primaryContainer =
            LedgerlyBrandTurquoise,
        onPrimaryContainer =
            Color.Black,
        secondaryContainer =
            LedgerlyDarkSurface,
        onSecondaryContainer =
            LedgerlyDarkText,
        error =
            LedgerlyExpenseRed
    )

private val LedgerlyPinkColorScheme =
    lightColorScheme(
        primary =
            LedgerlyPink,
        onPrimary =
            Color.White,
        secondary =
            LedgerlyPinkLight,
        onSecondary =
            LedgerlyWhiteText,
        tertiary =
            LedgerlyPink,
        onTertiary =
            Color.White,
        background =
            Color(0xFFFFF7FA),
        onBackground =
            LedgerlyWhiteText,
        surface =
            Color(0xFFFFF7FA),
        onSurface =
            LedgerlyWhiteText,
        surfaceVariant =
            Color(0xFFFFE8F0),
        onSurfaceVariant =
            LedgerlyWhiteText,
        primaryContainer =
            LedgerlyPinkLight,
        onPrimaryContainer =
            LedgerlyWhiteText,
        secondaryContainer =
            Color(0xFFFFE8F0),
        onSecondaryContainer =
            LedgerlyWhiteText,
        error =
            LedgerlyExpenseRed
    )

private val LedgerlyGreenColorScheme =
    lightColorScheme(
        primary =
            LedgerlyGreen,
        onPrimary =
            Color.White,
        secondary =
            LedgerlyGreenLight,
        onSecondary =
            LedgerlyWhiteText,
        tertiary =
            LedgerlyGreen,
        onTertiary =
            Color.White,
        background =
            Color(0xFFF7FCF7),
        onBackground =
            LedgerlyWhiteText,
        surface =
            Color(0xFFF7FCF7),
        onSurface =
            LedgerlyWhiteText,
        surfaceVariant =
            Color(0xFFE8F5E9),
        onSurfaceVariant =
            LedgerlyWhiteText,
        primaryContainer =
            LedgerlyGreenLight,
        onPrimaryContainer =
            LedgerlyWhiteText,
        secondaryContainer =
            Color(0xFFE8F5E9),
        onSecondaryContainer =
            LedgerlyWhiteText,
        error =
            LedgerlyExpenseRed
    )

private val LedgerlyPurpleColorScheme =
    lightColorScheme(
        primary =
            LedgerlyPurple,
        onPrimary =
            Color.White,
        secondary =
            LedgerlyPurpleLight,
        onSecondary =
            LedgerlyWhiteText,
        tertiary =
            LedgerlyPurple,
        onTertiary =
            Color.White,
        background =
            Color(0xFFFAF8FF),
        onBackground =
            LedgerlyWhiteText,
        surface =
            Color(0xFFFAF8FF),
        onSurface =
            LedgerlyWhiteText,
        surfaceVariant =
            Color(0xFFF0EBFA),
        onSurfaceVariant =
            LedgerlyWhiteText,
        primaryContainer =
            LedgerlyPurpleLight,
        onPrimaryContainer =
            LedgerlyWhiteText,
        secondaryContainer =
            Color(0xFFF0EBFA),
        onSecondaryContainer =
            LedgerlyWhiteText,
        error =
            LedgerlyExpenseRed
    )

private val LedgerlyBlueColorScheme =
    lightColorScheme(
        primary =
            LedgerlyBlue,
        onPrimary =
            Color.White,
        secondary =
            LedgerlyBlueLight,
        onSecondary =
            LedgerlyWhiteText,
        tertiary =
            LedgerlyBlue,
        onTertiary =
            Color.White,
        background =
            Color(0xFFF6FAFF),
        onBackground =
            LedgerlyWhiteText,
        surface =
            Color(0xFFF6FAFF),
        onSurface =
            LedgerlyWhiteText,
        surfaceVariant =
            Color(0xFFE7F2FD),
        onSurfaceVariant =
            LedgerlyWhiteText,
        primaryContainer =
            LedgerlyBlueLight,
        onPrimaryContainer =
            LedgerlyWhiteText,
        secondaryContainer =
            Color(0xFFE7F2FD),
        onSecondaryContainer =
            LedgerlyWhiteText,
        error =
            LedgerlyExpenseRed
    )

private val LedgerlyOrangeColorScheme =
    lightColorScheme(
        primary =
            LedgerlyOrange,
        onPrimary =
            Color.White,
        secondary =
            LedgerlyOrangeLight,
        onSecondary =
            LedgerlyWhiteText,
        tertiary =
            LedgerlyOrange,
        onTertiary =
            Color.White,
        background =
            Color(0xFFFFFAF5),
        onBackground =
            LedgerlyWhiteText,
        surface =
            Color(0xFFFFFAF5),
        onSurface =
            LedgerlyWhiteText,
        surfaceVariant =
            Color(0xFFFFEEDB),
        onSurfaceVariant =
            LedgerlyWhiteText,
        primaryContainer =
            LedgerlyOrangeLight,
        onPrimaryContainer =
            LedgerlyWhiteText,
        secondaryContainer =
            Color(0xFFFFEEDB),
        onSecondaryContainer =
            LedgerlyWhiteText,
        error =
            LedgerlyExpenseRed
    )

@Composable
fun LedgerlyTheme(
    themeName: String = "Default",
    textSize: String = "Standard",
    content: @Composable () -> Unit
) {
    val colourScheme =
        when (themeName) {
            "White",
            "Light" ->
                LedgerlyLightColorScheme

            "Dark" ->
                LedgerlyDarkColorScheme

            "Pink" ->
                LedgerlyPinkColorScheme

            "Green" ->
                LedgerlyGreenColorScheme

            "Purple" ->
                LedgerlyPurpleColorScheme

            "Blue" ->
                LedgerlyBlueColorScheme

            "Orange" ->
                LedgerlyOrangeColorScheme

            "Turquoise",
            "Default" ->
                LedgerlyDefaultColorScheme

            else ->
                LedgerlyDefaultColorScheme
        }

    MaterialTheme(
        colorScheme =
            colourScheme,
        typography =
            ledgerlyTypography(
                textSize
            ),
        content =
            content
    )
}