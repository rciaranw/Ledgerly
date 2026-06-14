package com.ledgerly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.ledgerly.ui.components.EmptyStateCard
import com.ledgerly.ui.components.LedgerlyPrimaryButton
import com.ledgerly.ui.components.LedgerlySecondaryButton
import com.ledgerly.ui.components.TransactionCard
import com.ledgerly.utils.DateHelper
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
    val transactions =
        transactionViewModel.transactions

    val settings =
        settingsViewModel.settings

    val focusManager =
        LocalFocusManager.current

    var searchText by remember {
        mutableStateOf("")
    }

    var selectedFilter by remember {
        mutableStateOf(
            TransactionFilter.All
        )
    }

    val filteredTransactions =
        transactions
            .filter { transaction ->
                when (selectedFilter) {
                    TransactionFilter.All ->
                        true

                    TransactionFilter.Income ->
                        transaction.isIncome

                    TransactionFilter.Expense ->
                        !transaction.isIncome
                }
            }
            .filter { transaction ->
                val query =
                    searchText.trim()

                if (query.isBlank()) {
                    true
                } else {
                    val rawAmount =
                        transaction.amount
                            .toString()

                    val formattedAmount =
                        "%.2f".format(
                            transaction.amount
                        )

                    val isoDate =
                        transaction.date
                            .toString()

                    val preferredDate =
                        DateHelper.formatDate(
                            date =
                                transaction.date,
                            format =
                                settings.dateFormat
                        )

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
                        ) ||
                        rawAmount.contains(
                            query,
                            ignoreCase = true
                        ) ||
                        formattedAmount.contains(
                            query,
                            ignoreCase = true
                        ) ||
                        isoDate.contains(
                            query,
                            ignoreCase = true
                        ) ||
                        preferredDate.contains(
                            query,
                            ignoreCase = true
                        )
                }
            }
            .sortedWith(
                compareByDescending<
                    com.ledgerly.data.models.Transaction
                > {
                    it.date
                }.thenByDescending {
                    it.id
                }
            )

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
                text = "Transactions",
                style =
                    MaterialTheme.typography.headlineMedium,
                fontWeight =
                    FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement =
                    Arrangement.Center,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                LedgerlyPrimaryButton(
                    text = "Add",
                    onClick =
                        onAddTransaction
                )

                Spacer(
                    modifier =
                        Modifier.padding(
                            horizontal = 5.dp
                        )
                )

                LedgerlySecondaryButton(
                    text = "Recurring",
                    onClick =
                        onAddRecurringTransaction
                )
            }

            OutlinedTextField(
                value =
                    searchText,
                onValueChange = {
                    searchText =
                        it.replace(
                            "\n",
                            ""
                        )
                },
                singleLine =
                    true,
                label = {
                    Text(
                        text =
                            "Search transactions"
                    )
                },
                keyboardOptions =
                    KeyboardOptions(
                        imeAction =
                            ImeAction.Search
                    ),
                keyboardActions =
                    KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                        }
                    ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            TransactionFilterSelector(
                selectedFilter =
                    selectedFilter,
                onFilterSelected = { filter ->
                    selectedFilter =
                        filter
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )

            if (
                filteredTransactions.isEmpty()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    if (
                        transactions.isEmpty()
                    ) {
                        EmptyStateCard(
                            emoji = "💳",
                            title =
                                "No transactions yet",
                            message =
                                "Add your first transaction to start tracking your money."
                        )
                    } else {
                        EmptyStateCard(
                            emoji = "🔍",
                            title =
                                "No matching transactions",
                            message =
                                "Try searching by title, amount, notes, date or category."
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement =
                        Arrangement.spacedBy(
                            10.dp
                        ),
                    modifier =
                        Modifier.padding(
                            top = 16.dp
                        )
                ) {
                    items(
                        items =
                            filteredTransactions,
                        key = { transaction ->
                            transaction.id
                        }
                    ) { transaction ->
                        TransactionCard(
                            transaction =
                                transaction,
                            currencyCode =
                                settings.currencyCode,
                            dateFormat =
                                settings.dateFormat,
                            onClick = {
                                onEditTransaction(
                                    transaction.id
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionFilterSelector(
    selectedFilter: TransactionFilter,
    onFilterSelected:
        (TransactionFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier =
            modifier,
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {
        TransactionFilterOption(
            label = "All",
            selected =
                selectedFilter ==
                    TransactionFilter.All,
            onClick = {
                onFilterSelected(
                    TransactionFilter.All
                )
            },
            modifier =
                Modifier.weight(1f)
        )

        TransactionFilterOption(
            label = "Expenses",
            selected =
                selectedFilter ==
                    TransactionFilter.Expense,
            onClick = {
                onFilterSelected(
                    TransactionFilter.Expense
                )
            },
            modifier =
                Modifier.weight(1f)
        )

        TransactionFilterOption(
            label = "Income",
            selected =
                selectedFilter ==
                    TransactionFilter.Income,
            onClick = {
                onFilterSelected(
                    TransactionFilter.Income
                )
            },
            modifier =
                Modifier.weight(1f)
        )
    }
}

@Composable
private fun TransactionFilterOption(
    label: String,
    selected: Boolean,
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
                    MaterialTheme
                        .colorScheme
                        .primary
                } else {
                    MaterialTheme
                        .colorScheme
                        .surfaceVariant
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
            text =
                label,
            color =
                if (selected) {
                    MaterialTheme
                        .colorScheme
                        .onPrimary
                } else {
                    MaterialTheme
                        .colorScheme
                        .onSurface
                },
            fontWeight =
                if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                }
        )
    }
}

private enum class TransactionFilter {
    All,
    Income,
    Expense
}