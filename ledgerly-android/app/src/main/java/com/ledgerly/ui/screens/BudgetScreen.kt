package com.ledgerly.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.ledgerly.ui.components.BudgetCard
import com.ledgerly.ui.components.EmptyStateCard
import com.ledgerly.ui.components.LedgerlyPrimaryButton
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
fun BudgetScreen(
    transactionViewModel: TransactionViewModel,
    budgetViewModel: BudgetViewModel,
    settingsViewModel: SettingsViewModel,
    periodViewModel: PeriodViewModel,
    onAddBudget: () -> Unit,
    onEditBudget: (String) -> Unit
) {
    val settings =
        settingsViewModel.settings

    val transactions =
        transactionViewModel.transactions

    val overallBudget =
        budgetViewModel.overallBudget

    val categoryBudgets =
        budgetViewModel.categoryBudgets

    val startDate =
        DatePeriodHelper.startDate(
            anchorDate =
                periodViewModel.anchorDate,
            periodType =
                periodViewModel.periodType,
            weekStartDay =
                settings.weekStartDay,
            monthStartDay =
                settings.monthStartDay
        )

    val endDate =
        DatePeriodHelper.endDate(
            anchorDate =
                periodViewModel.anchorDate,
            periodType =
                periodViewModel.periodType,
            weekStartDay =
                settings.weekStartDay,
            monthStartDay =
                settings.monthStartDay
        )

    LaunchedEffect(
        transactions.size,
        startDate,
        endDate
    ) {
        budgetViewModel.updateBudgets(
            transactions =
                transactions,
            startDate =
                startDate,
            endDate =
                endDate
        )
    }

    val totalBudgetLimit =
        overallBudget?.limit
            ?: 0.0

    val totalBudgetSpent =
        overallBudget?.spent
            ?: 0.0

    val totalBudgetRemaining =
        overallBudget?.remaining
            ?: 0.0

    val budgetProgress =
        if (totalBudgetLimit <= 0.0) {
            0f
        } else {
            (
                totalBudgetSpent /
                    totalBudgetLimit
                )
                .toFloat()
                .coerceIn(
                    0f,
                    1f
                )
        }

    val budgetCards =
        listOfNotNull(
            overallBudget
        ) + categoryBudgets

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Budget",
                style =
                    MaterialTheme
                        .typography
                        .headlineMedium,
                fontWeight =
                    FontWeight.Bold
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
                    periodViewModel
                        .previousPeriod()
                },
                onNext = {
                    periodViewModel
                        .nextPeriod()
                },
                onToday = {
                    periodViewModel
                        .resetToToday()
                },
                onPeriodTypeChanged = { type ->
                    periodViewModel
                        .updatePeriodType(type)
                }
            )
        }

        item {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                BudgetMiniCard(
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

                BudgetMiniCard(
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
                        if (
                            totalBudgetRemaining <
                            0.0
                        ) {
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
                        Arrangement.spacedBy(
                            10.dp
                        )
                ) {
                    Text(
                        text =
                            "Overall Budget Use",
                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,
                        fontWeight =
                            FontWeight.Bold
                    )

                    if (overallBudget == null) {
                        EmptyStateCard(
                            emoji = "🎯",
                            title =
                                "No budget set",
                            message =
                                "Create a budget to start tracking your spending."
                        )
                    } else {
                        Text(
                            text =
                                "${
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
                            text =
                                "Remaining: ${
                                    CurrencyFormatter.format(
                                        amount =
                                            totalBudgetRemaining,
                                        currencyCode =
                                            settings.currencyCode
                                    )
                                }",
                            color =
                                if (
                                    totalBudgetRemaining <
                                    0.0
                                ) {
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
            Box(
                modifier =
                    Modifier.fillMaxWidth(),
                contentAlignment =
                    Alignment.Center
            ) {
                LedgerlyPrimaryButton(
                    text =
                        if (
                            overallBudget == null
                        ) {
                            "Add Budget"
                        } else {
                            "Edit Budget Plan"
                        },
                    onClick =
                        onAddBudget
                )
            }
        }

        item {
            Text(
                text =
                    "Category Allocations",
                style =
                    MaterialTheme
                        .typography
                        .titleLarge,
                fontWeight =
                    FontWeight.Bold
            )
        }

        if (categoryBudgets.isEmpty()) {
            item {
                EmptyStateCard(
                    emoji = "📂",
                    title =
                        "No category allocations",
                    message =
                        "Break your budget down by category when you want more control."
                )
            }
        } else {
            items(
                items =
                    categoryBudgets,
                key = { budget ->
                    budget.id
                }
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
                    "Budget Progress",
                style =
                    MaterialTheme
                        .typography
                        .titleLarge,
                fontWeight =
                    FontWeight.Bold
            )
        }

        if (budgetCards.isEmpty()) {
            item {
                EmptyStateCard(
                    emoji = "🎯",
                    title =
                        "No budget configured",
                    message =
                        "Create a budget plan to track spending for this period."
                )
            }
        } else {
            items(
                items =
                    budgetCards,
                key = { budget ->
                    budget.id
                }
            ) { budget ->
                BudgetCard(
                    budget =
                        budget,
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
        (
            progress *
                100
            )
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

    val statusColour =
        when {
            progress >= 1.0 ->
                LedgerlyExpenseRed

            progress >= 0.8 ->
                MaterialTheme
                    .colorScheme
                    .primary

            else ->
                LedgerlyIncomeGreen
        }

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
                Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text =
                        category,
                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        status,
                    color =
                        statusColour,
                    fontWeight =
                        FontWeight.Bold
                )
            }

            LinearProgressIndicator(
                progress = {
                    progress
                        .toFloat()
                        .coerceIn(
                            0f,
                            1f
                        )
                },
                modifier =
                    Modifier.fillMaxWidth()
            )

            Text(
                text =
                    "$percentage% used"
            )
        }
    }
}

@Composable
private fun BudgetMiniCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColour:
        androidx.compose.ui.graphics.Color =
        MaterialTheme
            .colorScheme
            .onSurface
) {
    Card(
        modifier =
            modifier,
        shape =
            RoundedCornerShape(18.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme
                        .colorScheme
                        .surfaceVariant
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {
        Column(
            modifier =
                Modifier.padding(14.dp),
            verticalArrangement =
                Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text =
                    title,
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium
            )

            Text(
                text =
                    value,
                style =
                    MaterialTheme
                        .typography
                        .titleLarge,
                fontWeight =
                    FontWeight.Bold,
                color =
                    valueColour
            )
        }
    }
}