package com.ledgerly.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.data.models.Budget
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.ui.theme.LedgerlyIncomeGreen
import com.ledgerly.utils.CurrencyFormatter
import com.ledgerly.viewmodel.BudgetViewModel

@Composable
fun BudgetCard(
    budget: Budget,
    currencyCode: String,
    onClick: (() -> Unit)? = null
) {
    val isOverallBudget =
        budget.id == BudgetViewModel.OVERALL_BUDGET_ID

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
        shape = RoundedCornerShape(
            if (isOverallBudget) {
                22.dp
            } else {
                16.dp
            }
        ),
        colors = CardDefaults.cardColors(
            containerColor =
                if (isOverallBudget) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
        ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    if (isOverallBudget) {
                        4.dp
                    } else {
                        2.dp
                    }
            )
    ) {
        Column(
            modifier = Modifier.padding(
                if (isOverallBudget) {
                    20.dp
                } else {
                    16.dp
                }
            ),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(
                            if (isOverallBudget) {
                                48.dp
                            } else {
                                42.dp
                            }
                        ),
                        shape = CircleShape,
                        color =
                            if (isOverallBudget) {
                                MaterialTheme.colorScheme.onPrimary
                                    .copy(alpha = 0.18f)
                            } else {
                                MaterialTheme.colorScheme.background
                            }
                    ) {
                        Row(
                            horizontalArrangement =
                                Arrangement.Center,
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {
                            CategoryIcon(
                                iconName =
                                    budget.category.systemIcon,
                                contentDescription =
                                    budget.category.name,
                                modifier = Modifier.size(
                                    if (isOverallBudget) {
                                        26.dp
                                    } else {
                                        22.dp
                                    }
                                )
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Column {
                        Text(
                            text = budget.category.name,
                            style =
                                if (isOverallBudget) {
                                    MaterialTheme.typography.titleLarge
                                } else {
                                    MaterialTheme.typography.titleMedium
                                },
                            fontWeight = FontWeight.Bold,
                            color =
                                if (isOverallBudget) {
                                    MaterialTheme.colorScheme.onPrimary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                        )

                        Text(
                            text = "$progressPercent% used",
                            style = MaterialTheme.typography.bodySmall,
                            color =
                                if (isOverallBudget) {
                                    MaterialTheme.colorScheme.onPrimary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                        )
                    }
                }

                Text(
                    text = CurrencyFormatter.format(
                        amount = budget.remaining,
                        currencyCode = currencyCode
                    ),
                    style =
                        if (isOverallBudget) {
                            MaterialTheme.typography.titleLarge
                        } else {
                            MaterialTheme.typography.titleMedium
                        },
                    fontWeight = FontWeight.Bold,
                    color =
                        if (isOverallBudget) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            remainingColour
                        }
                )
            }

            LinearProgressIndicator(
                progress = {
                    budget.progress
                        .toFloat()
                        .coerceIn(0f, 1f)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Spent: ${
                        CurrencyFormatter.format(
                            amount = budget.spent,
                            currencyCode = currencyCode
                        )
                    }",
                    style =
                        MaterialTheme.typography.bodySmall,
                    color =
                        if (isOverallBudget) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                )

                Text(
                    text = "Limit: ${
                        CurrencyFormatter.format(
                            amount = budget.limit,
                            currencyCode = currencyCode
                        )
                    }",
                    style =
                        MaterialTheme.typography.bodySmall,
                    color =
                        if (isOverallBudget) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                )
            }
        }
    }
}