package com.ledgerly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.data.models.PeriodType
import com.ledgerly.utils.DatePeriodHelper
import java.time.LocalDate

@Composable
fun PeriodSelector(
    periodType: PeriodType,
    anchorDate: LocalDate,
    weekStartDay: Int,
    monthStartDay: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onToday: () -> Unit,
    onPeriodTypeChanged: (PeriodType) -> Unit,
    modifier: Modifier = Modifier
) {
    val label =
        DatePeriodHelper.formatLabel(
            anchorDate = anchorDate,
            periodType = periodType,
            weekStartDay = weekStartDay,
            monthStartDay = monthStartDay
        )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation =
            CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
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
                IconButton(
                    onClick = onPrevious
                ) {
                    Icon(
                        imageVector =
                            Icons.Filled.ChevronLeft,
                        contentDescription =
                            "Previous period"
                    )
                }

                Column(
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {
                    Text(
                        text = label,
                        style =
                            MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    TextButton(
                        onClick = onToday
                    ) {
                        Text("Today")
                    }
                }

                IconButton(
                    onClick = onNext
                ) {
                    Icon(
                        imageVector =
                            Icons.Filled.ChevronRight,
                        contentDescription =
                            "Next period"
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {
                PeriodTypeOption(
                    label = "Day",
                    selected =
                        periodType == PeriodType.DAY,
                    onClick = {
                        onPeriodTypeChanged(
                            PeriodType.DAY
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                PeriodTypeOption(
                    label = "Week",
                    selected =
                        periodType == PeriodType.WEEK,
                    onClick = {
                        onPeriodTypeChanged(
                            PeriodType.WEEK
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                PeriodTypeOption(
                    label = "Month",
                    selected =
                        periodType == PeriodType.MONTH,
                    onClick = {
                        onPeriodTypeChanged(
                            PeriodType.MONTH
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                PeriodTypeOption(
                    label = "Year",
                    selected =
                        periodType == PeriodType.YEAR,
                    onClick = {
                        onPeriodTypeChanged(
                            PeriodType.YEAR
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PeriodTypeOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(50)
            )
            .background(
                if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.background
                }
            )
            .clickable {
                onClick()
            }
            .padding(
                vertical = 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            fontWeight =
                if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                }
        )
    }
}