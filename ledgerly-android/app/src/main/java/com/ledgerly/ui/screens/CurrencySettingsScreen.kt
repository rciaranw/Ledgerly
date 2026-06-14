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

private data class CurrencyOption(
    val code: String,
    val name: String,
    val flag: String
)

@Composable
fun CurrencySettingsScreen(
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val settings =
        settingsViewModel.settings

    val currencies =
        listOf(
            CurrencyOption(
                code = "GBP",
                name = "British Pound",
                flag = "🇬🇧"
            ),
            CurrencyOption(
                code = "EUR",
                name = "Euro",
                flag = "🇪🇺"
            ),
            CurrencyOption(
                code = "USD",
                name = "US Dollar",
                flag = "🇺🇸"
            ),
            CurrencyOption(
                code = "CAD",
                name = "Canadian Dollar",
                flag = "🇨🇦"
            ),
            CurrencyOption(
                code = "AUD",
                name = "Australian Dollar",
                flag = "🇦🇺"
            ),
            CurrencyOption(
                code = "NZD",
                name = "New Zealand Dollar",
                flag = "🇳🇿"
            ),
            CurrencyOption(
                code = "CHF",
                name = "Swiss Franc",
                flag = "🇨🇭"
            ),
            CurrencyOption(
                code = "SEK",
                name = "Swedish Krona",
                flag = "🇸🇪"
            ),
            CurrencyOption(
                code = "NOK",
                name = "Norwegian Krone",
                flag = "🇳🇴"
            ),
            CurrencyOption(
                code = "DKK",
                name = "Danish Krone",
                flag = "🇩🇰"
            ),
            CurrencyOption(
                code = "PLN",
                name = "Polish Złoty",
                flag = "🇵🇱"
            ),
            CurrencyOption(
                code = "JPY",
                name = "Japanese Yen",
                flag = "🇯🇵"
            ),
            CurrencyOption(
                code = "CNY",
                name = "Chinese Yuan",
                flag = "🇨🇳"
            ),
            CurrencyOption(
                code = "INR",
                name = "Indian Rupee",
                flag = "🇮🇳"
            ),
            CurrencyOption(
                code = "ZAR",
                name = "South African Rand",
                flag = "🇿🇦"
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
                text = "Currency",
                style =
                    MaterialTheme.typography.headlineMedium,
                fontWeight =
                    FontWeight.Bold
            )
        }

        Text(
            text =
                "Choose the currency used throughout Ledgerly.",
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        currencies.forEach { currency ->
            val selected =
                settings.currencyCode ==
                    currency.code

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        settingsViewModel
                            .setCurrency(
                                currency.code
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
                        text = currency.flag,
                        style =
                            MaterialTheme.typography.headlineSmall
                    )

                    Column(
                        modifier =
                            Modifier.weight(1f),
                        verticalArrangement =
                            Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = currency.name,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text = currency.code,
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