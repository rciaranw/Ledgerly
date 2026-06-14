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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.ui.components.CategoryBreakdownChartCard
import com.ledgerly.ui.components.IncomeExpenseChartCard
import com.ledgerly.ui.components.MonthlyTrendChartCard
import com.ledgerly.ui.components.PeriodSelector
import com.ledgerly.ui.components.SpendingPieChartCard
import com.ledgerly.utils.AnalysisDataHelper
import com.ledgerly.utils.CurrencyFormatter
import com.ledgerly.utils.DatePeriodHelper
import com.ledgerly.viewmodel.PeriodViewModel
import com.ledgerly.viewmodel.SettingsViewModel
import com.ledgerly.viewmodel.TransactionViewModel

@Composable
fun AnalysisScreen(
    transactionViewModel: TransactionViewModel,
    settingsViewModel: SettingsViewModel,
    periodViewModel: PeriodViewModel
) {
    val settings =
        settingsViewModel.settings

    val transactions =
        transactionViewModel.transactions

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

    val periodTransactions =
        transactions.filter { transaction ->
            !transaction.date.isBefore(startDate) &&
                !transaction.date.isAfter(endDate)
        }

    val totalIncome =
        periodTransactions
            .filter { transaction ->
                transaction.isIncome
            }
            .sumOf { transaction ->
                transaction.amount
            }

    val totalExpenses =
        periodTransactions
            .filter { transaction ->
                !transaction.isIncome
            }
            .sumOf { transaction ->
                transaction.amount
            }

    val balance =
        totalIncome - totalExpenses

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
                    periodViewModel.previousPeriod()
                },
                onNext = {
                    periodViewModel.nextPeriod()
                },
                onToday = {
                    periodViewModel.resetToToday()
                },
                onPeriodTypeChanged = { type ->
                    periodViewModel.updatePeriodType(type)
                }
            )
        }

        item {
            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(20.dp),
                colors =
                    CardDefaults.cardColors(
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
                            MaterialTheme.colorScheme.onPrimary,
                        style =
                            MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text =
                            CurrencyFormatter.format(
                                amount = balance,
                                currencyCode =
                                    settings.currencyCode
                            ),
                        color =
                            MaterialTheme.colorScheme.onPrimary,
                        style =
                            MaterialTheme.typography.headlineLarge,
                        fontWeight =
                            FontWeight.Bold
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
                                    MaterialTheme.colorScheme.onPrimary
                            )

                            Text(
                                text =
                                    CurrencyFormatter.format(
                                        amount = totalIncome,
                                        currencyCode =
                                            settings.currencyCode
                                    ),
                                color =
                                    MaterialTheme.colorScheme.onPrimary,
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
                                    MaterialTheme.colorScheme.onPrimary
                            )

                            Text(
                                text =
                                    CurrencyFormatter.format(
                                        amount = totalExpenses,
                                        currencyCode =
                                            settings.currencyCode
                                    ),
                                color =
                                    MaterialTheme.colorScheme.onPrimary,
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
                currencyCode =
                    settings.currencyCode
            )
        }

        item {
            SpendingPieChartCard(
                data = categoryBreakdown,
                currencyCode =
                    settings.currencyCode
            )
        }

        item {
            CategoryBreakdownChartCard(
                data = categoryBreakdown,
                currencyCode =
                    settings.currencyCode
            )
        }

        item {
            MonthlyTrendChartCard(
                data = monthlyTrendData,
                currencyCode =
                    settings.currencyCode
            )
        }

        item {
            Text(
                text = "Top Spending Categories",
                style =
                    MaterialTheme.typography.titleLarge,
                fontWeight =
                    FontWeight.Bold
            )
        }

        if (categoryBreakdown.isEmpty()) {
            item {
                Card(
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(18.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                ) {
                    Text(
                        text = "No spending data available.",
                        modifier =
                            Modifier.padding(16.dp)
                    )
                }
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
                                MaterialTheme.colorScheme.surfaceVariant
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
                                CurrencyFormatter.format(
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
    }
}