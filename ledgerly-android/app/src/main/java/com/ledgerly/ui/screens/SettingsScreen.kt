package com.ledgerly.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.data.models.Category
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.viewmodel.BudgetViewModel
import com.ledgerly.viewmodel.CategoryViewModel
import com.ledgerly.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    budgetViewModel: BudgetViewModel,
    settingsViewModel: SettingsViewModel,
    categoryViewModel: CategoryViewModel,
    onAddCategory: () -> Unit,
    onViewRecurringTransactions: () -> Unit,
    onExport: () -> Unit
) {
    val settings = settingsViewModel.settings

    val customCategories =
        categoryViewModel.allCategories.filter {
            !it.isDefault
        }

    var currencyExpanded by remember {
        mutableStateOf(false)
    }

    var weekStartExpanded by remember {
        mutableStateOf(false)
    }

    var monthStartExpanded by remember {
        mutableStateOf(false)
    }

    var biometricLockEnabled by remember {
        mutableStateOf(false)
    }

    var categoryToDelete by remember {
        mutableStateOf<Category?>(null)
    }

    val currencies = listOf(
        "GBP",
        "USD",
        "EUR"
    )

    val weekDays = listOf(
        1 to "Monday",
        2 to "Tuesday",
        3 to "Wednesday",
        4 to "Thursday",
        5 to "Friday",
        6 to "Saturday",
        7 to "Sunday"
    )

    val monthDays = (1..31).toList()

    val selectedTheme =
        when (settings.theme) {
            "Turquoise" -> "Default"
            "White" -> "Light"
            "Light" -> "Light"
            else -> settings.theme
        }

    categoryToDelete?.let { category ->
        AlertDialog(
            onDismissRequest = {
                categoryToDelete = null
            },
            title = {
                Text("Delete Category?")
            },
            text = {
                Text(
                    "This will delete ${category.name}. Existing transactions using this category will keep their saved category name."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        categoryViewModel.deleteCategory(category)
                        categoryToDelete = null
                    }
                ) {
                    Text(
                        text = "Delete",
                        color = LedgerlyExpenseRed
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        categoryToDelete = null
                    }
                ) {
                    Text(
                        text = "Cancel",
                        color = LedgerlyExpenseRed
                    )
                }
            }
        )
    }

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
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation =
                CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Preferences",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                ExposedDropdownMenuBox(
                    expanded = currencyExpanded,
                    onExpandedChange = {
                        currencyExpanded = !currencyExpanded
                    }
                ) {
                    OutlinedTextField(
                        value = settings.currencyCode,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Currency")
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = currencyExpanded
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = currencyExpanded,
                        onDismissRequest = {
                            currencyExpanded = false
                        }
                    ) {
                        currencies.forEach { currency ->
                            DropdownMenuItem(
                                text = {
                                    Text(currency)
                                },
                                onClick = {
                                    settingsViewModel.setCurrency(currency)
                                    currencyExpanded = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = weekStartExpanded,
                    onExpandedChange = {
                        weekStartExpanded = !weekStartExpanded
                    }
                ) {
                    val selectedDayName =
                        weekDays.firstOrNull {
                            it.first == settings.weekStartDay
                        }?.second ?: "Monday"

                    OutlinedTextField(
                        value = selectedDayName,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Week Start Day")
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = weekStartExpanded
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = weekStartExpanded,
                        onDismissRequest = {
                            weekStartExpanded = false
                        }
                    ) {
                        weekDays.forEach { day ->
                            DropdownMenuItem(
                                text = {
                                    Text(day.second)
                                },
                                onClick = {
                                    settingsViewModel.setWeekStartDay(day.first)
                                    weekStartExpanded = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = monthStartExpanded,
                    onExpandedChange = {
                        monthStartExpanded = !monthStartExpanded
                    }
                ) {
                    OutlinedTextField(
                        value = settings.monthStartDay.toString(),
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Month Start Day")
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = monthStartExpanded
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = monthStartExpanded,
                        onDismissRequest = {
                            monthStartExpanded = false
                        }
                    ) {
                        monthDays.forEach { day ->
                            DropdownMenuItem(
                                text = {
                                    Text(day.toString())
                                },
                                onClick = {
                                    settingsViewModel.setMonthStartDay(day)
                                    monthStartExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation =
                CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                ThemeOptionCard(
                    title = "Default",
                    description = "Ledgerly branded appearance",
                    selected = selectedTheme == "Default",
                    onClick = {
                        settingsViewModel.setTheme("Default")
                    }
                )

                ThemeOptionCard(
                    title = "Light",
                    description = "Clean light interface",
                    selected = selectedTheme == "Light",
                    onClick = {
                        settingsViewModel.setTheme("White")
                    }
                )

                ThemeOptionCard(
                    title = "Dark",
                    description = "Dark appearance for low-light use",
                    selected = selectedTheme == "Dark",
                    onClick = {
                        settingsViewModel.setTheme("Dark")
                    }
                )
            }
        }

        SettingsActionCard(
            title = "Carry Over Budget",
            description =
                "Carry remaining budget into the next period"
        ) {
            Switch(
                checked = settings.carryOverEnabled,
                onCheckedChange = {
                    settingsViewModel.setCarryOverEnabled(it)
                }
            )
        }

        SettingsActionCard(
            title = "Biometric Lock",
            description =
                "Use Face ID, fingerprint or device security to protect Ledgerly"
        ) {
            Switch(
                checked = biometricLockEnabled,
                onCheckedChange = {
                    biometricLockEnabled = it
                }
            )
        }

        SettingsButtonCard(
            title = "Recurring Transactions",
            description =
                "Manage repeating income, bills and subscriptions",
            buttonText = "Manage Recurring",
            onClick = onViewRecurringTransactions
        )

        SettingsButtonCard(
            title = "Data Export",
            description = "Export all transactions as CSV.",
            buttonText = "Export Data",
            onClick = onExport
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation =
                CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Custom Categories",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                if (customCategories.isEmpty()) {
                    Text(
                        text = "No custom categories yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "Create custom categories to organise transactions beyond the default options.",
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    customCategories.forEach { category ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.SpaceBetween,
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = category.name,
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    text = "Custom category",
                                    style =
                                        MaterialTheme.typography.bodySmall
                                )
                            }

                            TextButton(
                                onClick = {
                                    categoryToDelete = category
                                }
                            ) {
                                Text(
                                    text = "Delete",
                                    color = LedgerlyExpenseRed
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = onAddCategory,
                    modifier = Modifier
                        .width(220.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Add Category")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation =
                CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "App Info",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                InfoRow(
                    label = "Version",
                    value = "1.0.0"
                )

                InfoRow(
                    label = "Build",
                    value = "1"
                )

                TextButton(
                    onClick = {},
                    modifier = Modifier
                        .width(220.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Contact Support")
                }
            }
        }
    }
}

@Composable
private fun ThemeOptionCard(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (selected) {
                    MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.14f
                    )
                } else {
                    MaterialTheme.colorScheme.background
                }
        ),
        border =
            if (selected) {
                BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                BorderStroke(
                    width = 1.dp,
                    color =
                        MaterialTheme.colorScheme.outline.copy(
                            alpha = 0.35f
                        )
                )
            }
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement =
                Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun SettingsActionCard(
    title: String,
    description: String,
    action: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation =
            CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            action()
        }
    }
}

@Composable
private fun SettingsButtonCard(
    title: String,
    description: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation =
            CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )

            Button(
                onClick = onClick,
                modifier = Modifier
                    .width(220.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(buttonText)
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween,
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = value,
            fontWeight = FontWeight.Bold
        )
    }
}