package com.ledgerly.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.data.models.Transaction
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.ui.theme.LedgerlyIncomeGreen
import com.ledgerly.utils.CurrencyFormatter
import com.ledgerly.utils.DateHelper

@Composable
fun TransactionCard(
    transaction: Transaction,
    currencyCode: String,
    dateFormat: String = "DD/MM/YYYY",
    onClick: () -> Unit
) {
    val amountText =
        CurrencyFormatter.format(
            amount =
                transaction.amount,
            currencyCode =
                currencyCode
        )

    val signedAmountText =
        if (transaction.isIncome) {
            "+$amountText"
        } else {
            "-$amountText"
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
            CategoryIconView(
                category =
                    transaction.category,
                isIncome =
                    transaction.isIncome
            )

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
                        transaction.title,
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        transaction.category.name,
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
                        DateHelper.formatDate(
                            date =
                                transaction.date,
                            format =
                                dateFormat
                        ),
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
                        transaction.isIncome
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