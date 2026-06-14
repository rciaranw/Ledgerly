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

private data class LanguageOption(
    val value: String,
    val label: String,
    val flag: String
)

@Composable
fun LanguageSettingsScreen(
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val settings =
        settingsViewModel.settings

    val languages =
        listOf(
            LanguageOption(
                value = "English",
                label = "English",
                flag = "🇬🇧"
            ),
            LanguageOption(
                value = "Dutch",
                label = "Nederlands",
                flag = "🇳🇱"
            ),
            LanguageOption(
                value = "French",
                label = "Français",
                flag = "🇫🇷"
            ),
            LanguageOption(
                value = "German",
                label = "Deutsch",
                flag = "🇩🇪"
            ),
            LanguageOption(
                value = "Spanish",
                label = "Español",
                flag = "🇪🇸"
            ),
            LanguageOption(
                value = "Italian",
                label = "Italiano",
                flag = "🇮🇹"
            ),
            LanguageOption(
                value = "Portuguese",
                label = "Português",
                flag = "🇵🇹"
            ),
            LanguageOption(
                value = "Swedish",
                label = "Svenska",
                flag = "🇸🇪"
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
                text = "Language",
                style =
                    MaterialTheme.typography.headlineMedium,
                fontWeight =
                    FontWeight.Bold
            )
        }

        Text(
            text =
                "Choose the language used throughout Ledgerly.",
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        languages.forEach { language ->
            val selected =
                settings.language ==
                    language.value

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        settingsViewModel
                            .setLanguage(
                                language.value
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
                    Text(
                        text = language.flag,
                        style =
                            MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = language.label,
                        modifier =
                            Modifier.weight(1f),
                        fontWeight =
                            FontWeight.SemiBold
                    )

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