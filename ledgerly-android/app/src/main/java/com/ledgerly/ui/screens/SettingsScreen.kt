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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.viewmodel.BudgetViewModel
import com.ledgerly.viewmodel.CategoryViewModel
import com.ledgerly.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    budgetViewModel: BudgetViewModel,
    settingsViewModel: SettingsViewModel,
    categoryViewModel: CategoryViewModel,
    onOpenPreferences: () -> Unit,
    onOpenDisplay: () -> Unit,
    onOpenData: () -> Unit,
    onOpenAppInfo: () -> Unit
) {
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
        Text(
            text = "Settings",
            style =
                MaterialTheme.typography.headlineMedium,
            fontWeight =
                FontWeight.Bold
        )

        Text(
            text =
                "Manage Ledgerly preferences, appearance, data and app information.",
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        SettingsFolderCard(
            title = "Preferences",
            description =
                "Currency, date format, week start day and month start date",
            icon =
                Icons.Filled.Tune,
            onClick =
                onOpenPreferences
        )

        SettingsFolderCard(
            title = "Display",
            description =
                "Theme, language and text size",
            icon =
                Icons.Filled.Palette,
            onClick =
                onOpenDisplay
        )

        SettingsFolderCard(
            title = "Data",
            description =
                "Custom categories, recurring transactions and data export",
            icon =
                Icons.Filled.DataUsage,
            onClick =
                onOpenData
        )

        SettingsFolderCard(
            title = "App Info",
            description =
                "Version, build number and support",
            icon =
                Icons.Filled.Info,
            onClick =
                onOpenAppInfo
        )
    }
}

@Composable
private fun SettingsFolderCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape =
            RoundedCornerShape(20.dp),
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
                .padding(18.dp),
            horizontalArrangement =
                Arrangement.spacedBy(16.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint =
                    MaterialTheme.colorScheme.primary
            )

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