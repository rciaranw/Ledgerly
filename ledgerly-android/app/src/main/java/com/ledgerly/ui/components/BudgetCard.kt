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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.data.models.Budget
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.ui.theme.LedgerlyIncomeGreen
import com.ledgerly.utils.CurrencyFormatter

@Composable
fun BudgetCard(
    budget: Budget,
    currencyCode: String,
    onClick: (() -> Unit)? = null
) {
    val progressPercent =
        (budget.progress * 100)
            .toInt()
            .coerceAtLeast(0)

    val remainingColour =
        if (budget.isOverBudget) {
            LedgerlyExpenseRed
        } else {
            LedgerlyIncomeGreen
        }

    val clickableModifier =
        if (onClick == null) {
            Modifier
        } else {
            Modifier.clickable {
                onClick()
            }
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(clickableModifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = budget.category.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "$progressPercent% used",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = CurrencyFormatter.format(
                        amount = budget.remaining,
                        currencyCode = currencyCode
                    ),
                    fontWeight = FontWeight.Bold,
                    color = remainingColour
                )
            }

            LinearProgressIndicator(
                progress = {
                    budget.progress.toFloat()
                        .coerceIn(0f, 1f)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Spent: ${
                        CurrencyFormatter.format(
                            amount = budget.spent,
                            currencyCode = currencyCode
                        )
                    }",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Limit: ${
                        CurrencyFormatter.format(
                            amount = budget.limit,
                            currencyCode = currencyCode
                        )
                    }",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}