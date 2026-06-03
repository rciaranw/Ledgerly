package com.ledgerly.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.ui.components.TransactionCard
import com.ledgerly.viewmodel.CategoryViewModel
import com.ledgerly.viewmodel.SettingsViewModel
import com.ledgerly.viewmodel.TransactionViewModel

@Composable
fun TransactionsScreen(
    transactionViewModel: TransactionViewModel,
    settingsViewModel: SettingsViewModel,
    categoryViewModel: CategoryViewModel,
    onAddTransaction: () -> Unit,
    onAddRecurringTransaction: () -> Unit,
    onEditTransaction: (String) -> Unit
) {
    val transactions = transactionViewModel.transactions
    val settings = settingsViewModel.settings

    var searchText by remember {
        mutableStateOf("")
    }

    var selectedFilter by remember {
        mutableStateOf(TransactionFilter.All)
    }

    val filteredTransactions = transactions
        .filter { transaction ->
            when (selectedFilter) {
                TransactionFilter.All -> true
                TransactionFilter.Income -> transaction.isIncome
                TransactionFilter.Expense -> !transaction.isIncome
            }
        }
        .filter { transaction ->
            val query = searchText.trim()

            if (query.isBlank()) {
                true
            } else {
                transaction.title.contains(
                    query,
                    ignoreCase = true
                ) ||
                    transaction.category.name.contains(
                        query,
                        ignoreCase = true
                    ) ||
                    transaction.notes.contains(
                        query,
                        ignoreCase = true
                    )
            }
        }
        .sortedByDescending {
            it.date
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Transactions",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onAddTransaction,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Add Transaction")
            }

            Button(
                onClick = onAddRecurringTransaction,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Add Recurring Transaction")
            }

            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                },
                label = {
                    Text("Search transactions")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement =
                    androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == TransactionFilter.All,
                    onClick = {
                        selectedFilter = TransactionFilter.All
                    },
                    label = {
                        Text("All")
                    }
                )

                FilterChip(
                    selected = selectedFilter == TransactionFilter.Expense,
                    onClick = {
                        selectedFilter = TransactionFilter.Expense
                    },
                    label = {
                        Text("Expenses")
                    }
                )

                FilterChip(
                    selected = selectedFilter == TransactionFilter.Income,
                    onClick = {
                        selectedFilter = TransactionFilter.Income
                    },
                    label = {
                        Text("Income")
                    }
                )
            }

            if (filteredTransactions.isEmpty()) {
                Text(
                    text = if (transactions.isEmpty()) {
                        "No transactions yet"
                    } else {
                        "No matching transactions"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                LazyColumn(
                    verticalArrangement =
                        androidx.compose.foundation.layout.Arrangement.spacedBy(
                            10.dp
                        ),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    items(filteredTransactions) { transaction ->
                        TransactionCard(
                            transaction = transaction,
                            currencyCode = settings.currencyCode,
                            onClick = {
                                onEditTransaction(transaction.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

private enum class TransactionFilter {
    All,
    Income,
    Expense
}