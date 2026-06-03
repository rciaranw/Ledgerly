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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.ui.components.RecurringTransactionCard
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
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedId = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Recurring Transactions",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onAddRecurring,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Add Recurring Transaction")
            }

            if (recurringTransactions.isEmpty()) {
                Text(
                    text = "No recurring transactions",
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(top = 16.dp),
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