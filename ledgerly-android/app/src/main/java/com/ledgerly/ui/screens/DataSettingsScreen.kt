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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.viewmodel.SettingsViewModel

@Composable
fun DataSettingsScreen(
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit,
    onOpenCustomCategories: () -> Unit,
    onOpenRecurringTransactions: () -> Unit,
    onOpenExportData: () -> Unit
) {
    val settings =
        settingsViewModel.settings

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
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
                text = "Data",
                style =
                    MaterialTheme.typography.headlineMedium,
                fontWeight =
                    FontWeight.Bold
            )
        }

        DataOptionCard(
            title = "Custom Categories",
            description =
                "Create, review and remove custom transaction categories",
            onClick =
                onOpenCustomCategories
        )

        DataOptionCard(
            title = "Manage Recurring Transactions",
            description =
                "Review repeating income, bills and subscriptions",
            onClick =
                onOpenRecurringTransactions
        )

        DataOptionCard(
            title = "Export Data",
            description =
                "Export transaction data as a CSV file",
            onClick =
                onOpenExportData
        )

        DataToggleCard(
            title = "Carry Over Budget",
            description =
                "Carry remaining budget into the next period",
            checked =
                settings.carryOverEnabled,
            onCheckedChange = { enabled ->
                settingsViewModel
                    .setCarryOverEnabled(
                        enabled
                    )
            }
        )

        DataToggleCard(
            title = "Biometric Lock",
            description =
                "Require fingerprint, face recognition or device authentication",
            checked =
                settings.biometricLockEnabled,
            onCheckedChange = { enabled ->
                settingsViewModel
                    .setBiometricLockEnabled(
                        enabled
                    )
            }
        )
    }
}

@Composable
private fun DataOptionCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape =
            RoundedCornerShape(18.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
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
                    text = title,
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text = description,
                    style =
                        MaterialTheme.typography.bodySmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector =
                    Icons.Filled.ChevronRight,
                contentDescription =
                    "Open $title"
            )
        }
    }
}

@Composable
private fun DataToggleCard(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(18.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
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
                    text = title,
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text = description,
                    style =
                        MaterialTheme.typography.bodySmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Switch(
                checked =
                    checked,
                onCheckedChange =
                    onCheckedChange
            )
        }
    }
}