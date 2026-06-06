package com.ledgerly.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    val total =
        income + expenses

    val incomeProgress =
        if (total <= 0.0) {
            0f
        } else {
            (income / total).toFloat()
        }

    val expenseProgress =
        if (total <= 0.0) {
            0f
        } else {
            (expenses / total).toFloat()
        }

    ChartCard(
        title = "Income vs Expenses"
    ) {
        Column(
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            ChartProgressRow(
                label = "Income",
                value = CurrencyFormatter.format(
                    amount = income,
                    currencyCode = currencyCode
                ),
                progress = incomeProgress
            )

            ChartProgressRow(
                label = "Expenses",
                value = CurrencyFormatter.format(
                    amount = expenses,
                    currencyCode = currencyCode
                ),
                progress = expenseProgress
            )
        }
    }
}

@Composable
fun SpendingPieChartCard(
    data: List<ChartDataItem>,
    currencyCode: String
) {
    val total =
        data.sumOf {
            it.amount
        }

    ChartCard(
        title = "Spending Split"
    ) {
        if (data.isEmpty() || total <= 0.0) {
            Text(
                text = "No spending data available."
            )
        } else {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    val diameter =
                        size.minDimension * 0.75f

                    val topLeft =
                        Offset(
                            x = (size.width - diameter) / 2f,
                            y = (size.height - diameter) / 2f
                        )

                    var startAngle = -90f

                    data.forEachIndexed { index, item ->
                        val sweepAngle =
                            ((item.amount / total) * 360f)
                                .toFloat()

                        drawArc(
                            color = chartColours[
                                index % chartColours.size
                            ],
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            topLeft = topLeft,
                            size = Size(
                                diameter,
                                diameter
                            ),
                            style = Stroke(
                                width = 28.dp.toPx(),
                                cap = StrokeCap.Butt
                            )
                        )

                        startAngle += sweepAngle
                    }
                }

                data.take(5).forEachIndexed { index, item ->
                    ChartLegendRow(
                        label = item.label,
                        value = CurrencyFormatter.format(
                            amount = item.amount,
                            currencyCode = currencyCode
                        ),
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
    val maxAmount =
        data.maxOfOrNull {
            it.amount
        } ?: 0.0

    ChartCard(
        title = "Category Breakdown"
    ) {
        if (data.isEmpty() || maxAmount <= 0.0) {
            Text(
                text = "No category spending yet."
            )
        } else {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                data.take(5).forEach { item ->
                    ChartProgressRow(
                        label = item.label,
                        value = CurrencyFormatter.format(
                            amount = item.amount,
                            currencyCode = currencyCode
                        ),
                        progress =
                            (item.amount / maxAmount)
                                .toFloat()
                                .coerceIn(0f, 1f)
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
    val maxAmount =
        data.maxOfOrNull {
            it.amount
        } ?: 0.0

    ChartCard(
        title = "Monthly Trend"
    ) {
        if (data.isEmpty() || maxAmount <= 0.0) {
            Text(
                text = "No trend data available yet."
            )
        } else {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    val barSpacing =
                        10.dp.toPx()

                    val barCount =
                        data.size.coerceAtLeast(1)

                    val totalSpacing =
                        barSpacing * (barCount - 1)

                    val barWidth =
                        (size.width - totalSpacing) / barCount

                    data.forEachIndexed { index, item ->
                        val heightRatio =
                            (item.amount / maxAmount)
                                .toFloat()
                                .coerceIn(0f, 1f)

                        val barHeight =
                            size.height * heightRatio

                        val x =
                            index * (barWidth + barSpacing)

                        val y =
                            size.height - barHeight

                        drawRoundRect(
                            color = chartColours[
                                index % chartColours.size
                            ],
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
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.label,
                            style =
                                MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text =
                                CurrencyFormatter.format(
                                    amount = item.amount,
                                    currencyCode = currencyCode
                                ),
                            style =
                                MaterialTheme.typography.bodySmall,
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
    content: @Composable () -> Unit
) {
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
                Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            content()
        }
    }
}

@Composable
private fun ChartProgressRow(
    label: String,
    value: String,
    progress: Float
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = value,
                fontWeight = FontWeight.Bold
            )
        }

        LinearProgressIndicator(
            progress = {
                progress.coerceIn(0f, 1f)
            },
            modifier = Modifier.fillMaxWidth()
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
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            color =
                chartColours[
                    colourIndex % chartColours.size
                ]
        )
    }
}

private val chartColours =
    listOf(
        androidx.compose.ui.graphics.Color(0xFF22D3EE),
        androidx.compose.ui.graphics.Color(0xFF38BDF8),
        androidx.compose.ui.graphics.Color(0xFF818CF8),
        androidx.compose.ui.graphics.Color(0xFFA78BFA),
        androidx.compose.ui.graphics.Color(0xFFF472B6),
        androidx.compose.ui.graphics.Color(0xFFFB7185)
    )