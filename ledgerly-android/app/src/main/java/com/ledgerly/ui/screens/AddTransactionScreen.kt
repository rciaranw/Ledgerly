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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import com.ledgerly.data.models.DefaultCategories
import com.ledgerly.data.models.Transaction
import com.ledgerly.ui.components.CategoryIcon
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.ui.theme.LedgerlyIncomeGreen
import com.ledgerly.viewmodel.BudgetViewModel
import com.ledgerly.viewmodel.CategoryViewModel
import com.ledgerly.viewmodel.TransactionViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    transactionViewModel: TransactionViewModel,
    budgetViewModel: BudgetViewModel,
    categoryViewModel: CategoryViewModel,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    var isIncome by remember {
        mutableStateOf(false)
    }

    var selectedCategory by remember {
        mutableStateOf(DefaultCategories.fallback)
    }

    var title by remember {
        mutableStateOf("")
    }

    var notes by remember {
        mutableStateOf("")
    }

    var amountText by remember {
        mutableStateOf("")
    }

    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    var categoryExpanded by remember {
        mutableStateOf(false)
    }

    var validationMessage by remember {
        mutableStateOf<String?>(null)
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    val categories = categoryViewModel.allCategories

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis
                            ?.let { millis ->
                                selectedDate = Instant
                                    .ofEpochMilli(millis)
                                    .atZone(
                                        ZoneId.systemDefault()
                                    )
                                    .toLocalDate()
                            }

                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text(
                        text = "Cancel",
                        color = LedgerlyExpenseRed
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
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
            text = "Add Transaction",
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
                        leadingIcon = {
                            CategoryIcon(
                                iconName =
                                    selectedCategory.systemIcon,
                                contentDescription =
                                    selectedCategory.name,
                                modifier = Modifier.size(22.dp)
                            )
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
                                    Row(
                                        verticalAlignment =
                                            Alignment.CenterVertically,
                                        horizontalArrangement =
                                            Arrangement.spacedBy(
                                                10.dp
                                            )
                                    ) {
                                        Surface(
                                            modifier =
                                                Modifier.size(32.dp),
                                            shape = CircleShape,
                                            color =
                                                MaterialTheme
                                                    .colorScheme
                                                    .background
                                        ) {
                                            Row(
                                                horizontalArrangement =
                                                    Arrangement.Center,
                                                verticalAlignment =
                                                    Alignment.CenterVertically
                                            ) {
                                                CategoryIcon(
                                                    iconName =
                                                        category.systemIcon,
                                                    contentDescription =
                                                        category.name,
                                                    modifier =
                                                        Modifier.size(
                                                            20.dp
                                                        )
                                                )
                                            }
                                        }

                                        Text(category.name)
                                    }
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
                    },
                    label = {
                        Text("Amount")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showDatePicker = true
                        }
                ) {
                    OutlinedTextField(
                        value = selectedDate.toString(),
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        label = {
                            Text("Date")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
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
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(
                    onClick = {
                        val amount =
                            amountText.toDoubleOrNull()

                        if (amount == null || amount <= 0.0) {
                            validationMessage =
                                "Please enter a valid amount."
                            return@Button
                        }

                        val transaction = Transaction(
                            id = UUID.randomUUID().toString(),
                            title = title.ifBlank {
                                selectedCategory.name
                            },
                            notes = notes,
                            amount = amount,
                            date = selectedDate,
                            category = selectedCategory,
                            isIncome = isIncome
                        )

                        transactionViewModel
                            .addTransaction(transaction)

                        budgetViewModel
                            .refreshCurrentMonthBudgets(
                                transactionViewModel.transactions
                            )

                        onSaved()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Transaction")
                }

                TextButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Cancel",
                        color = LedgerlyExpenseRed
                    )
                }
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
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            fontWeight = FontWeight.Bold
        )
    }
}