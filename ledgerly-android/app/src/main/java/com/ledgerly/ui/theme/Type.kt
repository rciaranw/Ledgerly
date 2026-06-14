package com.ledgerly.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
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
    DefaultTypography

private fun TextStyle.scaled(
    scale: Float
): TextStyle {
    return copy(
        fontSize =
            fontSize.scaledOrUnspecified(
                scale
            ),
        lineHeight =
            lineHeight.scaledOrUnspecified(
                scale
            )
    )
}

private fun TextUnit.scaledOrUnspecified(
    scale: Float
): TextUnit {
    return if (
        this == TextUnit.Unspecified
    ) {
        TextUnit.Unspecified
    } else {
        value.times(scale).sp
    }
}