package com.ledgerly.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
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
import com.ledgerly.ui.components.EmptyStateCard
import com.ledgerly.ui.components.LedgerlyPrimaryButton
import com.ledgerly.ui.components.RecurringTransactionCard
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.viewmodel.RecurringTransactionViewModel

@Composable
fun RecurringTransactionsScreen(
    recurringTransactionViewModel: RecurringTransactionViewModel,
    onAddRecurring: () -> Unit,
    onEditRecurring: (String) -> Unit
) {
    val recurringTransactions =
        recurringTransactionViewModel.recurringTransactions

    var selectedId by remember {
        mutableStateOf<String?>(null)
    }

    if (selectedId != null) {
        AlertDialog(
            onDismissRequest = {
                selectedId = null
            },
            title = {
                Text("Delete Recurring Transaction?")
            },
            text = {
                Text(
                    "This recurring transaction will be removed."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        recurringTransactions
                            .firstOrNull {
                                it.id == selectedId
                            }
                            ?.let {
                                recurringTransactionViewModel
                                    .deleteRecurringTransaction(it)
                            }

                        selectedId = null
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
                        selectedId = null
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier =
                Modifier.fillMaxSize()
        ) {
            Text(
                text = "Recurring Transactions",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            LedgerlyPrimaryButton(
                text = "Add Recurring",
                onClick = onAddRecurring,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            if (recurringTransactions.isEmpty()) {
                EmptyStateCard(
                    emoji = "🔁",
                    title = "No recurring transactions",
                    message = "Add recurring income and expenses to automate tracking.",
                    modifier = Modifier
                        .padding(top = 16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {
                    items(recurringTransactions) { recurring ->
                        RecurringTransactionCard(
                            recurring = recurring,
                            onClick = {
                                onEditRecurring(
                                    recurring.id
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}