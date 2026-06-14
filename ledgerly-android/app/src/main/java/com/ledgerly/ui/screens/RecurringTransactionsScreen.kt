package com.ledgerly.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.data.models.RecurringTransaction
import com.ledgerly.ui.components.EmptyStateCard
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.ui.theme.LedgerlyIncomeGreen
import com.ledgerly.utils.CurrencyFormatter
import com.ledgerly.utils.DateHelper
import com.ledgerly.viewmodel.RecurringTransactionViewModel
import com.ledgerly.viewmodel.SettingsViewModel

@Composable
fun RecurringTransactionsScreen(
    recurringTransactionViewModel:
        RecurringTransactionViewModel,
    settingsViewModel:
        SettingsViewModel,
    onBack: () -> Unit,
    onAddRecurring: () -> Unit,
    onEditRecurring: (String) -> Unit
) {
    val recurringTransactions =
        recurringTransactionViewModel
            .recurringTransactions

    val settings =
        settingsViewModel.settings

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier =
                Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            IconButton(
                onClick =
                    onBack
            ) {
                Icon(
                    imageVector =
                        Icons.Filled.ArrowBack,
                    contentDescription =
                        "Back"
                )
            }

            Text(
                text =
                    "Recurring Transactions",
                style =
                    MaterialTheme
                        .typography
                        .headlineMedium,
                fontWeight =
                    FontWeight.Bold
            )
        }

        Button(
            onClick =
                onAddRecurring,
            modifier =
                Modifier.align(
                    Alignment.CenterHorizontally
                )
        ) {
            Text(
                text =
                    "Add Recurring"
            )
        }

        if (
            recurringTransactions.isEmpty()
        ) {
            EmptyStateCard(
                emoji = "🔁",
                title =
                    "No recurring transactions",
                message =
                    "Add repeating income, bills or subscriptions."
            )
        } else {
            LazyColumn(
                modifier =
                    Modifier.fillMaxSize(),
                verticalArrangement =
                    Arrangement.spacedBy(
                        10.dp
                    )
            ) {
                items(
                    items =
                        recurringTransactions,
                    key = { recurring ->
                        recurring.id
                    }
                ) { recurring ->
                    RecurringTransactionCard(
                        recurring =
                            recurring,
                        currencyCode =
                            settings.currencyCode,
                        dateFormat =
                            settings.dateFormat,
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

@Composable
private fun RecurringTransactionCard(
    recurring: RecurringTransaction,
    currencyCode: String,
    dateFormat: String,
    onClick: () -> Unit
) {
    val amountText =
        CurrencyFormatter.format(
            amount =
                recurring.amount,
            currencyCode =
                currencyCode
        )

    val signedAmountText =
        if (recurring.isIncome) {
            "+$amountText"
        } else {
            "-$amountText"
        }

    val frequencyText =
        when (recurring.unit) {
            "DAILY" ->
                if (recurring.interval == 1) {
                    "Daily"
                } else {
                    "Every ${recurring.interval} days"
                }

            "WEEKLY" ->
                if (recurring.interval == 1) {
                    "Weekly"
                } else {
                    "Every ${recurring.interval} weeks"
                }

            "MONTHLY" ->
                if (recurring.interval == 1) {
                    "Monthly"
                } else {
                    "Every ${recurring.interval} months"
                }

            "YEARLY" ->
                if (recurring.interval == 1) {
                    "Yearly"
                } else {
                    "Every ${recurring.interval} years"
                }

            else ->
                "Recurring"
        }

    val dateRangeText =
        buildString {
            append(
                DateHelper.formatDate(
                    date =
                        recurring.startDate,
                    format =
                        dateFormat
                )
            )

            if (
                recurring.endDate != null
            ) {
                append(" to ")

                append(
                    DateHelper.formatDate(
                        date =
                            recurring.endDate,
                        format =
                            dateFormat
                    )
                )
            } else {
                append(" onwards")
            }
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
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
                defaultElevation =
                    2.dp
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement =
                Arrangement.spacedBy(
                    12.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Column(
                modifier =
                    Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(
                        4.dp
                    )
            ) {
                Text(
                    text =
                        recurring.title,
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        recurring.category.name,
                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )

                Text(
                    text =
                        frequencyText,
                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Text(
                    text =
                        dateRangeText,
                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,
                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )
            }

            Text(
                text =
                    signedAmountText,
                color =
                    if (
                        recurring.isIncome
                    ) {
                        LedgerlyIncomeGreen
                    } else {
                        LedgerlyExpenseRed
                    },
                style =
                    MaterialTheme
                        .typography
                        .titleMedium,
                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}