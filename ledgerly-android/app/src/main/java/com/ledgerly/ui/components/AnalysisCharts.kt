package com.ledgerly.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.ui.theme.LedgerlyIncomeGreen
import com.ledgerly.utils.CategoryBreakdownItem
import com.ledgerly.utils.MonthlyTrendItem
import com.ledgerly.utils.CurrencyFormatter
import kotlin.math.max

private val chartColours = listOf(
    Color(0xFF00B8A9),
    Color(0xFF7E57C2),
    Color(0xFFFFA726),
    Color(0xFF42A5F5),
    Color(0xFFEC407A),
    Color(0xFF66BB6A),
    Color(0xFFAB47BC),
    Color(0xFFFF7043)
)

@Composable
fun IncomeExpenseChartCard(
    income: Double,
    expenses: Double,
    currencyCode: String
) {
    val maximumValue =
        max(income, expenses)
            .coerceAtLeast(1.0)

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
                Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Income vs Expenses",
                style =
                    MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            ComparisonBar(
                label = "Income",
                amount = income,
                maximumValue = maximumValue,
                colour = LedgerlyIncomeGreen,
                currencyCode = currencyCode
            )

            ComparisonBar(
                label = "Expenses",
                amount = expenses,
                maximumValue = maximumValue,
                colour = LedgerlyExpenseRed,
                currencyCode = currencyCode
            )
        }
    }
}

@Composable
private fun ComparisonBar(
    label: String,
    amount: Double,
    maximumValue: Double,
    colour: Color,
    currencyCode: String
) {
    val progress =
        (amount / maximumValue)
            .toFloat()
            .coerceIn(0f, 1f)

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
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = CurrencyFormatter.format(
                    amount = amount,
                    currencyCode = currencyCode
                ),
                fontWeight = FontWeight.Bold
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
        ) {
            drawRoundRect(
                color = colour.copy(alpha = 0.18f),
                size = size,
                cornerRadius =
                    androidx.compose.ui.geometry.CornerRadius(
                        x = size.height / 2f,
                        y = size.height / 2f
                    )
            )

            drawRoundRect(
                color = colour,
                size = Size(
                    width = size.width * progress,
                    height = size.height
                ),
                cornerRadius =
                    androidx.compose.ui.geometry.CornerRadius(
                        x = size.height / 2f,
                        y = size.height / 2f
                    )
            )
        }
    }
}

@Composable
fun SpendingPieChartCard(
    data: List<CategoryBreakdownItem>,
    currencyCode: String
) {
    val total =
        data.sumOf { item ->
            item.amount
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
                Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Spending Split",
                style =
                    MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "How your expenses are divided by category.",
                style =
                    MaterialTheme.typography.bodyMedium
            )

            if (data.isEmpty() || total <= 0.0) {
                Text(
                    text = "No spending data available."
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(18.dp),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Canvas(
                        modifier = Modifier.size(140.dp)
                    ) {
                        var startAngle = -90f

                        data.forEachIndexed { index, item ->
                            val sweepAngle =
                                (
                                    item.amount /
                                        total *
                                        360.0
                                    ).toFloat()

                            drawArc(
                                color =
                                    chartColours[
                                        index %
                                            chartColours.size
                                    ],
                                startAngle =
                                    startAngle,
                                sweepAngle =
                                    sweepAngle,
                                useCenter = false,
                                style = Stroke(
                                    width =
                                        size.minDimension *
                                            0.24f,
                                    cap =
                                        StrokeCap.Butt
                                ),
                                topLeft = Offset(
                                    x =
                                        size.width *
                                            0.12f,
                                    y =
                                        size.height *
                                            0.12f
                                ),
                                size = Size(
                                    width =
                                        size.width *
                                            0.76f,
                                    height =
                                        size.height *
                                            0.76f
                                )
                            )

                            startAngle +=
                                sweepAngle
                        }
                    }

                    Column(
                        modifier =
                            Modifier.weight(1f),
                        verticalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        data.forEachIndexed { index, item ->
                            val percentage =
                                if (total <= 0.0) {
                                    0
                                } else {
                                    (
                                        item.amount /
                                            total *
                                            100.0
                                        ).toInt()
                                }

                            ChartLegendRow(
                                colour =
                                    chartColours[
                                        index %
                                            chartColours.size
                                    ],
                                label =
                                    item.label,
                                value =
                                    "$percentage%"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryBreakdownChartCard(
    data: List<CategoryBreakdownItem>,
    currencyCode: String
) {
    val maximumValue =
        data.maxOfOrNull { item ->
            item.amount
        }?.coerceAtLeast(1.0)
            ?: 1.0

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
                Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Category Breakdown",
                style =
                    MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Your highest spending categories for this period.",
                style =
                    MaterialTheme.typography.bodyMedium
            )

            if (data.isEmpty()) {
                Text(
                    text = "No category spending available."
                )
            } else {
                data.forEachIndexed { index, item ->
                    CategoryBreakdownRow(
                        item = item,
                        colour =
                            chartColours[
                                index %
                                    chartColours.size
                            ],
                        maximumValue =
                            maximumValue,
                        currencyCode =
                            currencyCode
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryBreakdownRow(
    item: CategoryBreakdownItem,
    colour: Color,
    maximumValue: Double,
    currencyCode: String
) {
    val progress =
        (item.amount / maximumValue)
            .toFloat()
            .coerceIn(0f, 1f)

    Column(
        verticalArrangement =
            Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .padding(0.dp)
                ) {
                    Canvas(
                        modifier =
                            Modifier.matchParentSize()
                    ) {
                        drawCircle(
                            color = colour
                        )
                    }
                }

                Text(
                    text = item.label,
                    fontWeight =
                        FontWeight.SemiBold
                )
            }

            Text(
                text =
                    CurrencyFormatter.format(
                        amount = item.amount,
                        currencyCode =
                            currencyCode
                    ),
                fontWeight =
                    FontWeight.Bold
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        ) {
            drawRoundRect(
                color =
                    colour.copy(alpha = 0.18f),
                size = size,
                cornerRadius =
                    androidx.compose.ui.geometry.CornerRadius(
                        x = size.height / 2f,
                        y = size.height / 2f
                    )
            )

            drawRoundRect(
                color = colour,
                size = Size(
                    width =
                        size.width * progress,
                    height =
                        size.height
                ),
                cornerRadius =
                    androidx.compose.ui.geometry.CornerRadius(
                        x = size.height / 2f,
                        y = size.height / 2f
                    )
            )
        }
    }
}

@Composable
fun MonthlyTrendChartCard(
    data: List<MonthlyTrendItem>,
    currencyCode: String
) {
    val maximumValue =
        data.maxOfOrNull { item ->
            item.amount
        }?.coerceAtLeast(1.0)
            ?: 1.0

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
                Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Monthly Spending Trend",
                style =
                    MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "A comparison of your spending over the last six months.",
                style =
                    MaterialTheme.typography.bodyMedium
            )

            if (data.isEmpty()) {
                Text(
                    text = "No trend data available."
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp),
                    horizontalArrangement =
                        Arrangement.spacedBy(10.dp),
                    verticalAlignment =
                        Alignment.Bottom
                ) {
                    data.forEach { item ->
                        val ratio =
                            (item.amount /
                                maximumValue)
                                .toFloat()
                                .coerceIn(
                                    0f,
                                    1f
                                )

                        Column(
                            modifier =
                                Modifier.weight(1f),
                            horizontalAlignment =
                                Alignment.CenterHorizontally,
                            verticalArrangement =
                                Arrangement.Bottom
                        ) {
                            Text(
                                text =
                                    CurrencyFormatter.format(
                                        amount =
                                            item.amount,
                                        currencyCode =
                                            currencyCode
                                    ),
                                style =
                                    MaterialTheme
                                        .typography
                                        .labelSmall,
                                maxLines = 1
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(6.dp)
                            )

                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(
                                        (
                                            120f *
                                                ratio
                                            )
                                            .coerceAtLeast(
                                                8f
                                            )
                                            .dp
                                    )
                            ) {
                                drawRoundRect(
                                    color =
                                        MaterialTheme
                                            .colorScheme
                                            .primary,
                                    size = size,
                                    cornerRadius =
                                        androidx.compose.ui.geometry.CornerRadius(
                                            x =
                                                size.width /
                                                    4f,
                                            y =
                                                size.width /
                                                    4f
                                        )
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(6.dp)
                            )

                            Text(
                                text = item.label,
                                style =
                                    MaterialTheme
                                        .typography
                                        .labelSmall,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChartLegendRow(
    colour: Color,
    label: String,
    value: String
) {
    Row(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween,
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement =
                Arrangement.spacedBy(8.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Canvas(
                modifier =
                    Modifier.size(10.dp)
            ) {
                drawCircle(
                    color = colour
                )
            }

            Text(
                text = label,
                style =
                    MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = value,
            style =
                MaterialTheme.typography.bodySmall,
            fontWeight =
                FontWeight.Bold
        )
    }
}