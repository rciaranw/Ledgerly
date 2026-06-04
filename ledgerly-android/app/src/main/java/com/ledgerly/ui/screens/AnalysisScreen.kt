package com.ledgerly.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.ui.components.BudgetCard
import com.ledgerly.ui.components.PeriodSelector
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.ui.theme.LedgerlyIncomeGreen
import com.ledgerly.utils.CurrencyFormatter
import com.ledgerly.utils.DatePeriodHelper
import com.ledgerly.viewmodel.BudgetViewModel
import com.ledgerly.viewmodel.PeriodViewModel
import com.ledgerly.viewmodel.SettingsViewModel
import com.ledgerly.viewmodel.TransactionViewModel

@Composable
fun AnalysisScreen(
    transactionViewModel: TransactionViewModel,
    budgetViewModel: BudgetViewModel,
    settingsViewModel: SettingsViewModel,
    periodViewModel: PeriodViewModel,
    onAddBudget: () -> Unit,
    onEditBudget: (String) -> Unit
) {
    val settings = settingsViewModel.settings
    val transactions = transactionViewModel.transactions
    val activeBudgets = budgetViewModel.activeBudgets

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

    val totalIncome =
        periodTransactions
            .filter { it.isIncome }
            .sumOf { it.amount }

    val totalExpenses =
        periodTransactions
            .filter { !it.isIncome }
            .sumOf { it.amount }

    val balance =
        totalIncome - totalExpenses

    val totalBudgetLimit =
        activeBudgets.sumOf { it.limit }

    val totalBudgetSpent =
        activeBudgets.sumOf { it.spent }

    val totalBudgetRemaining =
        activeBudgets.sumOf { it.remaining }

    val budgetProgress =
        if (totalBudgetLimit <= 0.0) {
            0f
        } else {
            (totalBudgetSpent / totalBudgetLimit)
                .toFloat()
                .coerceIn(0f, 1f)
        }

    val categoryBreakdown =
        periodTransactions
            .filter { !it.isIncome }
            .groupBy { it.category.name }
            .mapValues { entry ->
                entry.value.sumOf { it.amount }
            }
            .toList()
            .sortedByDescending { it.second }
            .take(5)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {

        item {
            Text(
                text = "Analysis",
                style =
                    MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            PeriodSelector(
                periodType =
                    periodViewModel.periodType,
                anchorDate =
                    periodViewModel.anchorDate,
                weekStartDay =
                    settings.weekStartDay,
                monthStartDay =
                    settings.monthStartDay,
                onPrevious = {
                    periodViewModel.previousPeriod()
                },
                onNext = {
                    periodViewModel.nextPeriod()
                },
                onPeriodTypeChanged = { type ->
                    periodViewModel.setPeriodType(type)
                }
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.primary
                ),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
            ) {
                Column(
                    modifier =
                        Modifier.padding(20.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Financial Overview",
                        color =
                            MaterialTheme.colorScheme
                                .onPrimary,
                        style =
                            MaterialTheme.typography
                                .titleMedium
                    )

                    Text(
                        text =
                            CurrencyFormatter.format(
                                amount = balance,
                                currencyCode =
                                    settings.currencyCode
                            ),
                        color =
                            MaterialTheme.colorScheme
                                .onPrimary,
                        style =
                            MaterialTheme.typography
                                .headlineLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {
                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Income",
                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .onPrimary
                            )

                            Text(
                                text =
                                    CurrencyFormatter
                                        .format(
                                            amount =
                                                totalIncome,
                                            currencyCode =
                                                settings.currencyCode
                                        ),
                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .onPrimary,
                                fontWeight =
                                    FontWeight.Bold
                            )
                        }

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Expenses",
                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .onPrimary
                            )

                            Text(
                                text =
                                    CurrencyFormatter
                                        .format(
                                            amount =
                                                totalExpenses,
                                            currencyCode =
                                                settings.currencyCode
                                        ),
                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .onPrimary,
                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                AnalysisMiniCard(
                    title = "Budgeted",
                    value =
                        CurrencyFormatter.format(
                            amount =
                                totalBudgetLimit,
                            currencyCode =
                                settings.currencyCode
                        ),
                    modifier =
                        Modifier.weight(1f)
                )

                AnalysisMiniCard(
                    title = "Remaining",
                    value =
                        CurrencyFormatter.format(
                            amount =
                                totalBudgetRemaining,
                            currencyCode =
                                settings.currencyCode
                        ),
                    modifier =
                        Modifier.weight(1f),
                    valueColour =
                        if (totalBudgetRemaining < 0) {
                            LedgerlyExpenseRed
                        } else {
                            LedgerlyIncomeGreen
                        }
                )
            }
        }

        item {
            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(18.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme
                                .colorScheme
                                .surfaceVariant
                    )
            ) {
                Column(
                    modifier =
                        Modifier.padding(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Overall Budget Use",
                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text = "${
                            CurrencyFormatter.format(
                                amount =
                                    totalBudgetSpent,
                                currencyCode =
                                    settings.currencyCode
                            )
                        } spent of ${
                            CurrencyFormatter.format(
                                amount =
                                    totalBudgetLimit,
                                currencyCode =
                                    settings.currencyCode
                            )
                        }"
                    )

                    LinearProgressIndicator(
                        progress = {
                            budgetProgress
                        },
                        modifier =
                            Modifier.fillMaxWidth()
                    )
                }
            }
        }

        item {
            Button(
                onClick = onAddBudget,
                modifier =
                    Modifier.fillMaxWidth()
            ) {
                Text("Add Budget")
            }
        }

        item {
            Text(
                text = "Budget Health",
                style =
                    MaterialTheme.typography
                        .titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (activeBudgets.isEmpty()) {
            item {
                Text(
                    "No budget data available."
                )
            }
        } else {
            items(
                activeBudgets.take(5)
            ) { budget ->
                BudgetHealthCard(
                    category =
                        budget.category.name,
                    progress =
                        budget.progress
                )
            }
        }

        item {
            Text(
                text =
                    "Top Spending Categories",
                style =
                    MaterialTheme.typography
                        .titleLarge,
                fontWeight =
                    FontWeight.Bold
            )
        }

        if (categoryBreakdown.isEmpty()) {
            item {
                Text(
                    "No spending data available."
                )
            }
        } else {
            items(categoryBreakdown) { category ->

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(18.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme
                                    .colorScheme
                                    .surfaceVariant
                        )
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {
                        Text(
                            text =
                                category.first,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                CurrencyFormatter
                                    .format(
                                        amount =
                                            category.second,
                                        currencyCode =
                                            settings.currencyCode
                                    )
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Budget Progress",
                style =
                    MaterialTheme.typography
                        .titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (activeBudgets.isEmpty()) {
            item {
                Text(
                    "No budgets configured yet."
                )
            }
        } else {
            items(activeBudgets) { budget ->
                BudgetCard(
                    budget = budget,
                    currencyCode =
                        settings.currencyCode,
                    onClick = {
                        onEditBudget(
                            budget.id
                        )
                    }
                )
            }
        }
    }
}