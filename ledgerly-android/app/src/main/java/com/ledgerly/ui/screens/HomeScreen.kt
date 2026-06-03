package com.ledgerly.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.ui.components.TransactionCard
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.ui.theme.LedgerlyIncomeGreen
import com.ledgerly.utils.CurrencyFormatter
import com.ledgerly.viewmodel.BudgetViewModel
import com.ledgerly.viewmodel.SettingsViewModel
import com.ledgerly.viewmodel.TransactionViewModel

@Composable
fun HomeScreen(
    transactionViewModel: TransactionViewModel,
    budgetViewModel: BudgetViewModel,
    settingsViewModel: SettingsViewModel
) {
    val transactions = transactionViewModel.transactions
    val settings = settingsViewModel.settings
    val activeBudgets = budgetViewModel.activeBudgets

    LaunchedEffect(transactions.size) {
        budgetViewModel.refreshCurrentMonthBudgets(
            transactions = transactions
        )
    }

    val balance = transactions.sumOf { transaction ->
        if (transaction.isIncome) {
            transaction.amount
        } else {
            -transaction.amount
        }
    }

    val totalIncome = transactions
        .filter { it.isIncome }
        .sumOf { it.amount }

    val totalExpenses = transactions
        .filter { !it.isIncome }
        .sumOf { it.amount }

    val savedAmount = totalIncome - totalExpenses

    val savedPercentage =
        if (totalIncome <= 0.0) {
            0
        } else {
            ((savedAmount / totalIncome) * 100)
                .toInt()
                .coerceAtLeast(0)
        }

    val recentTransactions = transactions
        .sortedByDescending { it.date }
        .take(5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Ledgerly",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardMetricCard(
                title = "Balance",
                value = CurrencyFormatter.format(
                    amount = balance,
                    currencyCode = settings.currencyCode
                ),
                modifier = Modifier.weight(1f)
            )

            DashboardMetricCard(
                title = "Income",
                value = CurrencyFormatter.format(
                    amount = totalIncome,
                    currencyCode = settings.currencyCode
                ),
                modifier = Modifier.weight(1f),
                valueColour = LedgerlyIncomeGreen
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardMetricCard(
                title = "Spent",
                value = CurrencyFormatter.format(
                    amount = totalExpenses,
                    currencyCode = settings.currencyCode
                ),
                modifier = Modifier.weight(1f),
                valueColour = LedgerlyExpenseRed
            )

            DashboardMetricCard(
                title = "Saved",
                value = "$savedPercentage%",
                modifier = Modifier.weight(1f)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Budget Snapshot",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (activeBudgets.isEmpty()) {
                    Text("No budgets set yet")
                } else {
                    activeBudgets
                        .take(3)
                        .forEach { budget ->
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = budget.category.name,
                                            fontWeight = FontWeight.Medium
                                        )

                                        Text(
                                            text = "Limit: ${
                                                CurrencyFormatter.format(
                                                    amount = budget.limit,
                                                    currencyCode = settings.currencyCode
                                                )
                                            }",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }

                                    Text(
                                        text = CurrencyFormatter.format(
                                            amount = budget.remaining,
                                            currencyCode = settings.currencyCode
                                        ),
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                LinearProgressIndicator(
                                    progress = {
                                        budget.progress.toFloat()
                                            .coerceIn(0f, 1f)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (recentTransactions.isEmpty()) {
                    Text("No transactions yet")
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        recentTransactions.forEach { transaction ->
                            TransactionCard(
                                transaction = transaction,
                                currencyCode = settings.currencyCode
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardMetricCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColour: androidx.compose.ui.graphics.Color =
        MaterialTheme.colorScheme.onSurface
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = valueColour
            )
        }
    }
}