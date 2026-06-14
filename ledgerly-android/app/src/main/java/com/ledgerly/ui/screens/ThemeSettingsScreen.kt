package com.ledgerly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.viewmodel.SettingsViewModel

private data class ThemeOption(
    val id: String,
    val title: String,
    val description: String,
    val previewColour: Color
)

@Composable
fun ThemeSettingsScreen(
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val settings =
        settingsViewModel.settings

    val selectedTheme =
        when (settings.theme) {
            "Turquoise" -> "Default"
            "White" -> "Light"
            else -> settings.theme
        }

    val themes =
        listOf(
            ThemeOption(
                id = "Default",
                title = "Default",
                description =
                    "Ledgerly turquoise branding",
                previewColour =
                    Color(0xFF00B8A9)
            ),
            ThemeOption(
                id = "Light",
                title = "Light",
                description =
                    "A clean light appearance",
                previewColour =
                    Color(0xFFF4F4F4)
            ),
            ThemeOption(
                id = "Dark",
                title = "Dark",
                description =
                    "A darker interface for low-light use",
                previewColour =
                    Color(0xFF202124)
            ),
            ThemeOption(
                id = "Pink",
                title = "Pink",
                description =
                    "A warm pink Ledgerly theme",
                previewColour =
                    Color(0xFFEC407A)
            ),
            ThemeOption(
                id = "Green",
                title = "Green",
                description =
                    "A fresh green Ledgerly theme",
                previewColour =
                    Color(0xFF43A047)
            ),
            ThemeOption(
                id = "Purple",
                title = "Purple",
                description =
                    "A bold purple Ledgerly theme",
                previewColour =
                    Color(0xFF7E57C2)
            ),
            ThemeOption(
                id = "Blue",
                title = "Blue",
                description =
                    "A calm blue Ledgerly theme",
                previewColour =
                    Color(0xFF1E88E5)
            ),
            ThemeOption(
                id = "Orange",
                title = "Orange",
                description =
                    "A brighter orange Ledgerly theme",
                previewColour =
                    Color(0xFFFB8C00)
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
                text = "Theme",
                style =
                    MaterialTheme.typography.headlineMedium,
                fontWeight =
                    FontWeight.Bold
            )
        }

        Text(
            text =
                "Choose the colour theme used throughout Ledgerly.",
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        themes.forEach { theme ->
            val selected =
                selectedTheme ==
                    theme.id

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        settingsViewModel
                            .setTheme(
                                theme.id
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
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                color =
                                    theme.previewColour,
                                shape =
                                    CircleShape
                            )
                    )

                    Column(
                        modifier =
                            Modifier.weight(1f),
                        verticalArrangement =
                            Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = theme.title,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text = theme.description,
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