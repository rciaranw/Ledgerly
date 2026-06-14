package com.ledgerly.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val DefaultTypography =
    Typography()

fun ledgerlyTypography(
    textSize: String
): Typography {
    val scale =
        when (textSize) {
            "Small" ->
                0.90f

            "Large" ->
                1.12f

            "Extra Large" ->
                1.25f

            else ->
                1f
        }

    return Typography(
        displayLarge =
            DefaultTypography
                .displayLarge
                .scaled(scale),

        displayMedium =
            DefaultTypography
                .displayMedium
                .scaled(scale),

        displaySmall =
            DefaultTypography
                .displaySmall
                .scaled(scale),

        headlineLarge =
            DefaultTypography
                .headlineLarge
                .scaled(scale),

        headlineMedium =
            DefaultTypography
                .headlineMedium
                .scaled(scale),

        headlineSmall =
            DefaultTypography
                .headlineSmall
                .scaled(scale),

        titleLarge =
            DefaultTypography
                .titleLarge
                .scaled(scale),

        titleMedium =
            DefaultTypography
                .titleMedium
                .scaled(scale),

        titleSmall =
            DefaultTypography
                .titleSmall
                .scaled(scale),

        bodyLarge =
            DefaultTypography
                .bodyLarge
                .scaled(scale),

        bodyMedium =
            DefaultTypography
                .bodyMedium
                .scaled(scale),

        bodySmall =
            DefaultTypography
                .bodySmall
                .scaled(scale),

        labelLarge =
            DefaultTypography
                .labelLarge
                .scaled(scale),

        labelMedium =
            DefaultTypography
                .labelMedium
                .scaled(scale),

        labelSmall =
            DefaultTypography
                .labelSmall
                .scaled(scale)
    )
}

val Typography =
    Typography(
        bodyLarge =
            TextStyle(
                fontFamily =
                    FontFamily.Default,
                fontWeight =
                    FontWeight.Normal,
                fontSize =
                    16.sp,
                lineHeight =
                    24.sp,
                letterSpacing =
                    0.5.sp
            )
    )

private fun TextStyle.scaled(
    scale: Float
): TextStyle {
    val scaledFontSize =
        if (fontSize.isSpecified) {
            fontSize * scale
        } else {
            fontSize
        }

    val scaledLineHeight =
        if (lineHeight.isSpecified) {
            lineHeight * scale
        } else {
            lineHeight
        }

    return copy(
        fontSize =
            scaledFontSize,
        lineHeight =
            scaledLineHeight
    )
}