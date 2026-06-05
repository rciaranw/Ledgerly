package com.ledgerly.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.viewmodel.BudgetViewModel

@Composable
fun EditBudgetScreen(
    budgetId: String,
    budgetViewModel: BudgetViewModel,
    onSaved: () -> Unit,
    onDeleted: () -> Unit,
    onCancel: () -> Unit
) {
    val budget = budgetViewModel.budgets.firstOrNull {
        it.id == budgetId
    }

    if (budget == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Budget not found",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go Back")
            }
        }

        return
    }

    val isOverallBudget =
        budget.id == BudgetViewModel.OVERALL_BUDGET_ID

    var amountText by remember {
        mutableStateOf(
            budget.limit.toString()
        )
    }

    var validationMessage by remember {
        mutableStateOf<String?>(null)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text(
                    text = if (isOverallBudget) {
                        "Delete Overall Budget?"
                    } else {
                        "Delete Allocation?"
                    }
                )
            },
            text = {
                Text(
                    text = if (isOverallBudget) {
                        "This will delete your overall budget. Category allocations will remain unless deleted separately."
                    } else {
                        "This category allocation will be permanently deleted."
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        budgetViewModel.deleteBudget(budget)
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
            text = if (isOverallBudget) {
                "Edit Overall Budget"
            } else {
                "Edit Allocation"
            },
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
                    text = budget.category.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (isOverallBudget) {
                        "Update the total amount you want to spend during the selected period."
                    } else {
                        "Update the amount assigned to this category."
                    },
                    style = MaterialTheme.typography.bodySmall
                )

                OutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it
                        validationMessage = null
                    },
                    label = {
                        Text(
                            text = if (isOverallBudget) {
                                "Overall Budget"
                            } else {
                                "Allocation Amount"
                            }
                        )
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
                                "Enter a valid amount."
                            return@Button
                        }

                        if (isOverallBudget) {
                            budgetViewModel.setOverallBudget(
                                limit = amount
                            )
                        } else {
                            budgetViewModel.setBudget(
                                category = budget.category,
                                limit = amount
                            )
                        }

                        onSaved()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Changes")
                }

                TextButton(
                    onClick = {
                        showDeleteDialog = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isOverallBudget) {
                            "Delete Overall Budget"
                        } else {
                            "Delete Allocation"
                        },
                        color = LedgerlyExpenseRed
                    )
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