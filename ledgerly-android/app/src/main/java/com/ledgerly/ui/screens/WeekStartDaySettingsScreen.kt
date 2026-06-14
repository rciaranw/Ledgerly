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

private data class WeekDayOption(
    val value: Int,
    val label: String
)

@Composable
fun WeekStartDaySettingsScreen(
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val settings =
        settingsViewModel.settings

    val days =
        listOf(
            WeekDayOption(
                value = 1,
                label = "Monday"
            ),
            WeekDayOption(
                value = 2,
                label = "Tuesday"
            ),
            WeekDayOption(
                value = 3,
                label = "Wednesday"
            ),
            WeekDayOption(
                value = 4,
                label = "Thursday"
            ),
            WeekDayOption(
                value = 5,
                label = "Friday"
            ),
            WeekDayOption(
                value = 6,
                label = "Saturday"
            ),
            WeekDayOption(
                value = 7,
                label = "Sunday"
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
                text = "Week Start Day",
                style =
                    MaterialTheme.typography.headlineMedium,
                fontWeight =
                    FontWeight.Bold
            )
        }

        Text(
            text =
                "Choose the first day used for weekly periods.",
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        days.forEach { day ->
            val selected =
                settings.weekStartDay ==
                    day.value

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        settingsViewModel
                            .setWeekStartDay(
                                day.value
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
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Text(
                        text = day.label,
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