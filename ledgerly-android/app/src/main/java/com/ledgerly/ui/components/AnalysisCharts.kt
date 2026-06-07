package com.ledgerly.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.ui.theme.LedgerlyIncomeGreen
import com.ledgerly.utils.CurrencyFormatter

data class ChartDataItem(
    val label: String,
    val amount: Double
)

@Composable
fun IncomeExpenseChartCard(
    income: Double,
    expenses: Double,
    currencyCode: String
) {
    val total = income + expenses

    val incomeProgress =
        if (total <= 0.0) 0f else (income / total).toFloat().coerceIn(0f, 1f)

    val expenseProgress =
        if (total <= 0.0) 0f else (expenses / total).toFloat().coerceIn(0f, 1f)

    ChartCard(
        title = "Income vs Expenses"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ChartProgressRow(
                label = "Income",
                value = CurrencyFormatter.format(
                    amount = income,
                    currencyCode = currencyCode
                ),
                progress = incomeProgress,
                progressColour = LedgerlyIncomeGreen
            )

            ChartProgressRow(
                label = "Expenses",
                value = CurrencyFormatter.format(
                    amount = expenses,
                    currencyCode = currencyCode
                ),
                progress = expenseProgress,
                progressColour = LedgerlyExpenseRed
            )
        }
    }
}

@Composable
fun SpendingPieChartCard(
    data: List<ChartDataItem>,
    currencyCode: String
) {
    val total = data.sumOf { it.amount }

    ChartCard(
        title = "Spending Split"
    ) {
        if (data.isEmpty() || total <= 0.0) {
            Text("No spending data available.")
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    val diameter = size.minDimension * 0.75f

                    val topLeft = Offset(
                        x = (size.width - diameter) / 2f,
                        y = (size.height - diameter) / 2f
                    )

                    var startAngle = -90f

                    data.forEachIndexed { index, item ->
                        val sweepAngle =
                            ((item.amount / total) * 360f).toFloat()

                        drawArc(
                            color = chartColours[index % chartColours.size],
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            topLeft = topLeft,
                            size = Size(
                                width = diameter,
                                height = diameter
                            ),
                            style = Stroke(
                                width = 30.dp.toPx(),
                                cap = StrokeCap.Butt
                            )
                        )

                        startAngle += sweepAngle
                    }
                }

                data.take(6).forEachIndexed { index, item ->
                    val percentage =
                        if (total <= 0.0) {
                            0
                        } else {
                            ((item.amount / total) * 100).toInt()
                        }

                    ChartLegendRow(
                        label = item.label,
                        value = "${
                            CurrencyFormatter.format(
                                amount = item.amount,
                                currencyCode = currencyCode
                            )
                        } · $percentage%",
                        colourIndex = index
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryBreakdownChartCard(
    data: List<ChartDataItem>,
    currencyCode: String
) {
    val total = data.sumOf { it.amount }

    ChartCard(
        title = "Category Breakdown"
    ) {
        if (data.isEmpty() || total <= 0.0) {
            Text("No category spending yet.")
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                data.take(6).forEachIndexed { index, item ->
                    val percentage =
                        ((item.amount / total) * 100).toInt()

                    CategoryPercentageRow(
                        label = item.label,
                        amount = CurrencyFormatter.format(
                            amount = item.amount,
                            currencyCode = currencyCode
                        ),
                        percentage = percentage,
                        colourIndex = index
                    )
                }
            }
        }
    }
}

@Composable
fun MonthlyTrendChartCard(
    data: List<ChartDataItem>,
    currencyCode: String
) {
    val maxAmount = data.maxOfOrNull { it.amount } ?: 0.0

    ChartCard(
        title = "Monthly Spending Trend",
        description = "Shows total expenses by month."
    ) {
        if (data.isEmpty() || maxAmount <= 0.0) {
            Text("No trend data available yet.")
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    val barSpacing = 10.dp.toPx()
                    val barCount = data.size.coerceAtLeast(1)
                    val totalSpacing = barSpacing * (barCount - 1)
                    val barWidth = (size.width - totalSpacing) / barCount

                    data.forEachIndexed { index, item ->
                        val heightRatio =
                            (item.amount / maxAmount)
                                .toFloat()
                                .coerceIn(0f, 1f)

                        val barHeight = size.height * heightRatio
                        val x = index * (barWidth + barSpacing)
                        val y = size.height - barHeight

                        drawRoundRect(
                            color = chartColours[index % chartColours.size],
                            topLeft = Offset(
                                x = x,
                                y = y
                            ),
                            size = Size(
                                width = barWidth,
                                height = barHeight
                            ),
                            cornerRadius =
                                androidx.compose.ui.geometry.CornerRadius(
                                    x = 8.dp.toPx(),
                                    y = 8.dp.toPx()
                                )
                        )
                    }
                }

                data.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = CurrencyFormatter.format(
                                amount = item.amount,
                                currencyCode = currencyCode
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChartCard(
    title: String,
    description: String? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (description != null) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            content()
        }
    }
}

@Composable
private fun ChartProgressRow(
    label: String,
    value: String,
    progress: Float,
    progressColour: androidx.compose.ui.graphics.Color
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Medium,
                color = progressColour
            )

            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                color = progressColour
            )
        }

        LinearProgressIndicator(
            progress = {
                progress.coerceIn(0f, 1f)
            },
            modifier = Modifier.fillMaxWidth(),
            color = progressColour,
            trackColor = ProgressIndicatorDefaults.linearTrackColor
        )
    }
}

@Composable
private fun ChartLegendRow(
    label: String,
    value: String,
    colourIndex: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = chartColours[colourIndex % chartColours.size],
                        shape = CircleShape
                    )
            )

            Text(
                text = label,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            text = value,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CategoryPercentageRow(
    label: String,
    amount: String,
    percentage: Int,
    colourIndex: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = chartColours[colourIndex % chartColours.size],
                        shape = CircleShape
                    )
            )

            Column {
                Text(
                    text = label,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = amount,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

private val chartColours =
    listOf(
        androidx.compose.ui.graphics.Color(0xFFE53935),
        androidx.compose.ui.graphics.Color(0xFF43A047),
        androidx.compose.ui.graphics.Color(0xFF1E88E5),
        androidx.compose.ui.graphics.Color(0xFFFDD835),
        androidx.compose.ui.graphics.Color(0xFF8E24AA),
        androidx.compose.ui.graphics.Color(0xFFFB8C00),
        androidx.compose.ui.graphics.Color(0xFF00ACC1),
        androidx.compose.ui.graphics.Color(0xFF6D4C41),
        androidx.compose.ui.graphics.Color(0xFFD81B60),
        androidx.compose.ui.graphics.Color(0xFF3949AB)
    )