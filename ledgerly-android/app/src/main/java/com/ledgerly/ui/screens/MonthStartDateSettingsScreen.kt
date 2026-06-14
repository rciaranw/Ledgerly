package com.ledgerly.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun MonthStartDateSettingsScreen(
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val settings =
        settingsViewModel.settings

    val days =
        (1..31).toList()

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                text = "Month Start Date",
                style =
                    MaterialTheme.typography.headlineMedium,
                fontWeight =
                    FontWeight.Bold
            )
        }

        Text(
            text =
                "Choose the day your monthly period begins.",
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        LazyColumn(
            modifier =
                Modifier.fillMaxSize(),
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {
            items(days) { day ->
                val selected =
                    settings.monthStartDay ==
                        day

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            settingsViewModel
                                .setMonthStartDay(
                                    day
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
                        Column(
                            verticalArrangement =
                                Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text =
                                    ordinalDay(day),
                                fontWeight =
                                    FontWeight.SemiBold
                            )

                            if (day > 28) {
                                Text(
                                    text =
                                        "Uses the final available day in shorter months",
                                    style =
                                        MaterialTheme.typography.bodySmall,
                                    color =
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
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
}

private fun ordinalDay(
    day: Int
): String {
    val suffix =
        when {
            day in 11..13 ->
                "th"

            day % 10 == 1 ->
                "st"

            day % 10 == 2 ->
                "nd"

            day % 10 == 3 ->
                "rd"

            else ->
                "th"
        }

    return "$day$suffix"
}