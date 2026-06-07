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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.data.models.RecurringTransaction
import com.ledgerly.ui.components.LedgerlyNegativeTextButton
import com.ledgerly.ui.components.LedgerlyPrimaryButton
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.ui.theme.LedgerlyIncomeGreen
import com.ledgerly.viewmodel.CategoryViewModel
import com.ledgerly.viewmodel.RecurringTransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecurringTransactionScreen(
    recurringId: String,
    recurringTransactionViewModel: RecurringTransactionViewModel,
    categoryViewModel: CategoryViewModel,
    onSaved: () -> Unit,
    onDeleted: () -> Unit,
    onCancel: () -> Unit
) {
    val recurring =
        recurringTransactionViewModel.recurringTransactions
            .firstOrNull {
                it.id == recurringId
            }

    if (recurring == null) {
        Text("Recurring transaction not found")
        return
    }

    var title by remember {
        mutableStateOf(recurring.title)
    }

    var notes by remember {
        mutableStateOf(recurring.notes)
    }

    var amountText by remember {
        mutableStateOf(recurring.amount.toString())
    }

    var selectedCategory by remember {
        mutableStateOf(recurring.category)
    }

    var intervalText by remember {
        mutableStateOf(recurring.interval.toString())
    }

    var selectedUnit by remember {
        mutableStateOf(recurring.unit)
    }

    var isIncome by remember {
        mutableStateOf(recurring.isIncome)
    }

    var categoryExpanded by remember {
        mutableStateOf(false)
    }

    var unitExpanded by remember {
        mutableStateOf(false)
    }

    var validationMessage by remember {
        mutableStateOf<String?>(null)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    val categories =
        categoryViewModel.allCategories

    val units = listOf(
        "DAILY" to "Daily",
        "WEEKLY" to "Weekly",
        "MONTHLY" to "Monthly",
        "YEARLY" to "Yearly"
    )

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("Delete Recurring Transaction?")
            },
            text = {
                Text("This cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        recurringTransactionViewModel
                            .deleteRecurringTransaction(
                                recurring
                            )

                        showDeleteDialog = false
                        onDeleted()
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
                        showDeleteDialog = false
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
            text = "Edit Recurring",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(16.dp)
            ) {
                IncomeExpenseToggle(
                    isIncome = isIncome,
                    onSelected = {
                        isIncome = it
                    }
                )

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = {
                        categoryExpanded =
                            !categoryExpanded
                    }
                ) {
                    OutlinedTextField(
                        value = selectedCategory.name,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Category")
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults
                                .TrailingIcon(
                                    expanded =
                                        categoryExpanded
                                )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = {
                            categoryExpanded = false
                        }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = {
                                    Text(category.name)
                                },
                                onClick = {
                                    selectedCategory = category
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        validationMessage = null
                    },
                    label = {
                        Text("Title")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it
                        validationMessage = null
                    },
                    label = {
                        Text("Amount")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = intervalText,
                    onValueChange = {
                        intervalText = it
                        validationMessage = null
                    },
                    label = {
                        Text("Every")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = unitExpanded,
                    onExpandedChange = {
                        unitExpanded =
                            !unitExpanded
                    }
                ) {
                    val selectedUnitLabel =
                        units.firstOrNull {
                            it.first == selectedUnit
                        }?.second ?: "Monthly"

                    OutlinedTextField(
                        value = selectedUnitLabel,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Frequency")
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults
                                .TrailingIcon(
                                    expanded =
                                        unitExpanded
                                )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = unitExpanded,
                        onDismissRequest = {
                            unitExpanded = false
                        }
                    ) {
                        units.forEach { unit ->
                            DropdownMenuItem(
                                text = {
                                    Text(unit.second)
                                },
                                onClick = {
                                    selectedUnit = unit.first
                                    unitExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = {
                        notes = it
                    },
                    label = {
                        Text("Notes")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                validationMessage?.let { message ->
                    Text(
                        text = message,
                        color =
                            MaterialTheme.colorScheme.error
                    )
                }

                LedgerlyPrimaryButton(
                    text = "Save Changes",
                    onClick = {
                        val amount =
                            amountText.toDoubleOrNull()

                        val interval =
                            intervalText.toIntOrNull()

                        if (amount == null || amount <= 0.0) {
                            validationMessage =
                                "Please enter a valid amount."
                            return@LedgerlyPrimaryButton
                        }

                        if (interval == null || interval <= 0) {
                            validationMessage =
                                "Please enter a valid interval."
                            return@LedgerlyPrimaryButton
                        }

                        recurringTransactionViewModel
                            .saveRecurringTransaction(
                                recurring.copy(
                                    title = title.ifBlank {
                                        selectedCategory.name
                                    },
                                    notes = notes,
                                    amount = amount,
                                    category = selectedCategory,
                                    isIncome = isIncome,
                                    interval = interval,
                                    unit = selectedUnit
                                )
                            )

                        onSaved()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )

                LedgerlyNegativeTextButton(
                    text = "Delete Recurring",
                    onClick = {
                        showDeleteDialog = true
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )

                LedgerlyNegativeTextButton(
                    text = "Cancel",
                    onClick = onCancel,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
private fun IncomeExpenseToggle(
    isIncome: Boolean,
    onSelected: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement =
                Arrangement.spacedBy(4.dp)
        ) {
            ToggleOption(
                text = "Expense",
                selected = !isIncome,
                selectedColour = LedgerlyExpenseRed,
                onClick = {
                    onSelected(false)
                },
                modifier = Modifier.weight(1f)
            )

            ToggleOption(
                text = "Income",
                selected = isIncome,
                selectedColour = LedgerlyIncomeGreen,
                onClick = {
                    onSelected(true)
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ToggleOption(
    text: String,
    selected: Boolean,
    selectedColour: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(50)
            )
            .background(
                if (selected) {
                    selectedColour
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            )
            .clickable {
                onClick()
            }
            .padding(
                vertical = 10.dp
            ),
        contentAlignment =
            Alignment.Center
    ) {
        Text(
            text = text,
            color =
                if (selected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            fontWeight =
                FontWeight.Bold
        )
    }
}