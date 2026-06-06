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
import com.ledgerly.data.models.RecurringTransaction
import com.ledgerly.ui.components.PeriodSelector
import com.ledgerly.ui.components.TransactionCard
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.ui.theme.LedgerlyIncomeGreen
import com.ledgerly.utils.CurrencyFormatter
import com.ledgerly.utils.DatePeriodHelper
import com.ledgerly.viewmodel.BudgetViewModel
import com.ledgerly.viewmodel.PeriodViewModel
import com.ledgerly.viewmodel.RecurringTransactionViewModel
import com.ledgerly.viewmodel.SettingsViewModel
import com.ledgerly.viewmodel.TransactionViewModel
import java.time.LocalDate

@Composable
fun HomeScreen(
    transactionViewModel: TransactionViewModel,
    budgetViewModel: BudgetViewModel,
    settingsViewModel: SettingsViewModel,
    recurringTransactionViewModel: RecurringTransactionViewModel,
    periodViewModel: PeriodViewModel
) {
    val transactions = transactionViewModel.transactions
    val settings = settingsViewModel.settings
    val overallBudget = budgetViewModel.overallBudget
    val categoryBudgets = budgetViewModel.categoryBudgets
    val recurringTransactions =
        recurringTransactionViewModel.recurringTransactions

    val startDate =
        DatePeriodHelper.startDate(
            anchorDate = periodViewModel.anchorDate,
            periodType = periodViewModel.periodType,
            weekStartDay = settings.weekStartDay,
            monthStartDay = settings.monthStartDay
        )

    val endDate =
        DatePeriodHelper.endDate(
            anchorDate = periodViewModel.anchorDate,
            periodType = periodViewModel.periodType,
            weekStartDay = settings.weekStartDay,
            monthStartDay = settings.monthStartDay
        )

    val periodTransactions =
        transactions.filter { transaction ->
            !transaction.date.isBefore(startDate) &&
                !transaction.date.isAfter(endDate)
        }

    LaunchedEffect(
        transactions.size,
        startDate,
        endDate
    ) {
        budgetViewModel.updateBudgets(
            transactions = transactions,
            startDate = startDate,
            endDate = endDate
        )
    }

    val balance =
        periodTransactions.sumOf { transaction ->
            if (transaction.isIncome) {
                transaction.amount
            } else {
                -transaction.amount
            }
        }

    val totalIncome =
        periodTransactions
            .filter { it.isIncome }
            .sumOf { it.amount }

    val totalExpenses =
        periodTransactions
            .filter { !it.isIncome }
            .sumOf { it.amount }

    val savedAmount =
        totalIncome - totalExpenses

    val savedPercentage =
        if (totalIncome <= 0.0) {
            0
        } else {
            ((savedAmount / totalIncome) * 100)
                .toInt()
                .coerceAtLeast(0)
        }

    val upcomingRecurring =
        recurringTransactions
            .mapNotNull { recurring ->
                val dueDate = nextDueDate(recurring)

                if (dueDate == null) {
                    null
                } else {
                    recurring to dueDate
                }
            }
            .filter {
                !it.second.isBefore(startDate) &&
                    !it.second.isAfter(endDate)
            }
            .sortedBy {
                it.second
            }
            .take(3)

    val recentTransactions =
        periodTransactions
            .sortedByDescending { it.date }
            .take(5)

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
            text = "Ledgerly",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        PeriodSelector(
            periodType = periodViewModel.periodType,
            anchorDate = periodViewModel.anchorDate,
            weekStartDay = settings.weekStartDay,
            monthStartDay = settings.monthStartDay,
            onPrevious = {
                periodViewModel.previousPeriod()
            },
            onNext = {
                periodViewModel.nextPeriod()
            },
            onPeriodTypeChanged = { type ->
                periodViewModel.updatePeriodType(type)
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
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
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
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
                    text = "Budget",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (overallBudget == null) {
                    Text(
                        text = "No budget has been created yet."
                    )

                    Text(
                        text = "Create an overall budget to start tracking your spending.",
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    Text(
                        text = "Overall Budget",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${
                            CurrencyFormatter.format(
                                amount = overallBudget.spent,
                                currencyCode = settings.currencyCode
                            )
                        } spent of ${
                            CurrencyFormatter.format(
                                amount = overallBudget.limit,
                                currencyCode = settings.currencyCode
                            )
                        }"
                    )

                    LinearProgressIndicator(
                        progress = {
                            overallBudget.progress
                                .toFloat()
                                .coerceIn(0f, 1f)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Remaining: ${
                            CurrencyFormatter.format(
                                amount = overallBudget.remaining,
                                currencyCode = settings.currencyCode
                            )
                        }",
                        fontWeight = FontWeight.Bold,
                        color = if (overallBudget.isOverBudget) {
                            LedgerlyExpenseRed
                        } else {
                            LedgerlyIncomeGreen
                        }
                    )

                    if (categoryBudgets.isNotEmpty()) {
                        Text(
                            text = "Category Allocations",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        categoryBudgets
                            .take(3)
                            .forEach { budget ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement =
                                        Arrangement.SpaceBetween,
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = budget.category.name
                                    )

                                    Text(
                                        text = "${
                                            CurrencyFormatter.format(
                                                amount = budget.spent,
                                                currencyCode =
                                                    settings.currencyCode
                                            )
                                        } / ${
                                            CurrencyFormatter.format(
                                                amount = budget.limit,
                                                currencyCode =
                                                    settings.currencyCode
                                            )
                                        }"
                                    )
                                }
                            }
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
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
                    text = "Upcoming Recurring Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (upcomingRecurring.isEmpty()) {
                    Text(
                        text = "No upcoming recurring transactions for this period"
                    )
                } else {
                    upcomingRecurring.forEach { item ->
                        val recurring = item.first
                        val dueDate = item.second

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.SpaceBetween,
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = recurring.title,
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    text = "Due: $dueDate",
                                    style =
                                        MaterialTheme.typography.bodySmall
                                )

                                Text(
                                    text = recurring.category.name,
                                    style =
                                        MaterialTheme.typography.bodySmall
                                )
                            }

                            Text(
                                text = CurrencyFormatter.format(
                                    amount = recurring.amount,
                                    currencyCode = settings.currencyCode
                                ),
                                color = if (recurring.isIncome) {
                                    LedgerlyIncomeGreen
                                } else {
                                    LedgerlyExpenseRed
                                },
                                fontWeight = FontWeight.Bold
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
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation =
                CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (recentTransactions.isEmpty()) {
                    Text(
                        text = "No transactions for this period"
                    )
                } else {
                    Column(
                        verticalArrangement =
                            Arrangement.spacedBy(10.dp)
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
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation =
            CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement =
                Arrangement.spacedBy(6.dp)
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

private fun nextDueDate(
    recurring: RecurringTransaction
): LocalDate? {
    val today = LocalDate.now()

    var candidate =
        recurring.lastGeneratedDate
            ?: recurring.startDate.minusDays(1)

    while (true) {
        candidate =
            when (recurring.unit) {
                "DAILY" ->
                    candidate.plusDays(
                        recurring.interval.toLong()
                    )

                "WEEKLY" ->
                    candidate.plusWeeks(
                        recurring.interval.toLong()
                    )

                "MONTHLY" ->
                    candidate.plusMonths(
                        recurring.interval.toLong()
                    )

                "YEARLY" ->
                    candidate.plusYears(
                        recurring.interval.toLong()
                    )

                else ->
                    candidate.plusMonths(
                        recurring.interval.toLong()
                    )
            }

        val endDate = recurring.endDate

        if (endDate != null && candidate.isAfter(endDate)) {
            return null
        }

        if (!candidate.isBefore(today)) {
            return candidate
        }
    }
}