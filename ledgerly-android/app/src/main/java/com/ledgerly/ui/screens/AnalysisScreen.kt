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
import com.ledgerly.ui.components.CategoryBreakdownChartCard
import com.ledgerly.ui.components.IncomeExpenseChartCard
import com.ledgerly.ui.components.MonthlyTrendChartCard
import com.ledgerly.ui.components.PeriodSelector
import com.ledgerly.ui.components.SpendingPieChartCard
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.ui.theme.LedgerlyIncomeGreen
import com.ledgerly.utils.AnalysisDataHelper
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

    val overallBudget =
        budgetViewModel.overallBudget

    val categoryBudgets =
        budgetViewModel.categoryBudgets

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
        overallBudget?.limit ?: 0.0

    val totalBudgetSpent =
        overallBudget?.spent ?: 0.0

    val totalBudgetRemaining =
        overallBudget?.remaining ?: 0.0

    val budgetProgress =
        if (totalBudgetLimit <= 0.0) {
            0f
        } else {
            (totalBudgetSpent / totalBudgetLimit)
                .toFloat()
                .coerceIn(0f, 1f)
        }

    val categoryBreakdown =
        AnalysisDataHelper
            .categoryBreakdownData(
                periodTransactions
            )
            .take(5)

    val monthlyTrendData =
        AnalysisDataHelper
            .monthlyTrendData(
                transactions
            )
            .takeLast(6)

    val budgetCards =
        listOfNotNull(overallBudget) + categoryBudgets

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
                    periodViewModel.updatePeriodType(type)
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
            IncomeExpenseChartCard(
                income = totalIncome,
                expenses = totalExpenses,
                currencyCode = settings.currencyCode
            )
        }

        item {
            SpendingPieChartCard(
                data = categoryBreakdown,
                currencyCode = settings.currencyCode
            )
        }

        item {
            CategoryBreakdownChartCard(
                data = categoryBreakdown,
                currencyCode = settings.currencyCode
            )
        }

        item {
            MonthlyTrendChartCard(
                data = monthlyTrendData,
                currencyCode = settings.currencyCode
            )
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

                    if (overallBudget == null) {
                        Text(
                            text = "No overall budget has been created yet."
                        )
                    } else {
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

                        Text(
                            text = "Remaining: ${
                                CurrencyFormatter.format(
                                    amount =
                                        totalBudgetRemaining,
                                    currencyCode =
                                        settings.currencyCode
                                )
                            }",
                            color =
                                if (totalBudgetRemaining < 0.0) {
                                    LedgerlyExpenseRed
                                } else {
                                    LedgerlyIncomeGreen
                                },
                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = onAddBudget,
                modifier =
                    Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (overallBudget == null) {
                        "Add Budget"
                    } else {
                        "Edit Budget Plan"
                    }
                )
            }
        }

        item {
            Text(
                text = "Category Allocations",
                style =
                    MaterialTheme.typography
                        .titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (categoryBudgets.isEmpty()) {
            item {
                Text(
                    text = "No category allocations configured yet."
                )
            }
        } else {
            items(
                categoryBudgets.take(5)
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
                                category.label,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                CurrencyFormatter
                                    .format(
                                        amount =
                                            category.amount,
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

        if (budgetCards.isEmpty()) {
            item {
                Text(
                    "No budget configured yet."
                )
            }
        } else {
            items(budgetCards) { budget ->
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

@Composable
private fun BudgetHealthCard(
    category: String,
    progress: Double
) {
    val percentage =
        (progress * 100)
            .toInt()
            .coerceAtLeast(0)

    val status =
        when {
            progress >= 1.0 ->
                "Over Budget"

            progress >= 0.8 ->
                "Near Limit"

            else ->
                "Healthy"
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {
                Text(
                    text = category,
                    fontWeight = FontWeight.Bold
                )

                Text(status)
            }

            LinearProgressIndicator(
                progress = {
                    progress.toFloat()
                        .coerceIn(0f, 1f)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "$percentage% used"
            )
        }
    }
}

@Composable
private fun AnalysisMiniCard(
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
                style =
                    MaterialTheme.typography.bodyMedium
            )

            Text(
                text = value,
                style =
                    MaterialTheme.typography.titleLarge,
                fontWeight =
                    FontWeight.Bold,
                color =
                    valueColour
            )
        }
    }
}