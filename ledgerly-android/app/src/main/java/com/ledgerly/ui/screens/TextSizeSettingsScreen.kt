package com.ledgerly.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.viewmodel.SettingsViewModel

private data class TextSizeOption(
    val value: String,
    val title: String,
    val description: String
)

@Composable
fun TextSizeSettingsScreen(
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val settings =
        settingsViewModel.settings

    val options =
        listOf(
            TextSizeOption(
                value = "Small",
                title = "Small",
                description =
                    "More content with smaller text"
            ),
            TextSizeOption(
                value = "Standard",
                title = "Standard",
                description =
                    "Ledgerly's default text size"
            ),
            TextSizeOption(
                value = "Large",
                title = "Large",
                description =
                    "Larger text for easier reading"
            ),
            TextSizeOption(
                value = "Extra Large",
                title = "Extra Large",
                description =
                    "Maximum text size for improved accessibility"
            )
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier =
                Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector =
                        Icons.Filled.ArrowBack,
                    contentDescription =
                        "Back"
                )
            }

            Text(
                text = "Text Size",
                style =
                    MaterialTheme.typography.headlineMedium,
                fontWeight =
                    FontWeight.Bold
            )
        }

        Text(
            text =
                "Choose the size of text used throughout Ledgerly.",
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        options.forEach { option ->
            val selected =
                settings.textSize ==
                    option.value

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        settingsViewModel
                            .setTextSize(
                                option.value
                            )
                    },
                shape =
                    RoundedCornerShape(18.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            if (selected) {
                                MaterialTheme
                                    .colorScheme
                                    .primaryContainer
                            } else {
                                MaterialTheme
                                    .colorScheme
                                    .surfaceVariant
                            }
                    ),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Column(
                        modifier =
                            Modifier.weight(1f),
                        verticalArrangement =
                            Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = option.title,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                option.description,
                            style =
                                MaterialTheme.typography.bodySmall,
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (selected) {
                        Icon(
                            imageVector =
                                Icons.Filled.Check,
                            contentDescription =
                                "Selected",
                            tint =
                                MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}