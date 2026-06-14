package com.ledgerly.utils

import com.ledgerly.data.models.Transaction
import com.ledgerly.ui.components.ChartDataItem
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

object AnalysisDataHelper {

    fun monthlyTrendData(
        transactions: List<Transaction>
    ): List<ChartDataItem> {
        return transactions
            .filter { transaction ->
                !transaction.isIncome
            }
            .groupBy { transaction ->
                YearMonth.from(
                    transaction.date
                )
            }
            .toSortedMap()
            .map { entry ->
                ChartDataItem(
                    label =
                        entry.key.month
                            .getDisplayName(
                                TextStyle.SHORT,
                                Locale.UK
                            ),
                    amount =
                        entry.value.sumOf { transaction ->
                            transaction.amount
                        }
                )
            }
    }

    fun categoryBreakdownData(
        transactions: List<Transaction>
    ): List<ChartDataItem> {
        return transactions
            .filter { transaction ->
                !transaction.isIncome
            }
            .groupBy { transaction ->
                transaction.category.name
            }
            .map { entry ->
                ChartDataItem(
                    label =
                        entry.key,
                    amount =
                        entry.value.sumOf { transaction ->
                            transaction.amount
                        }
                )
            }
            .sortedByDescending { item ->
                item.amount
            }
    }
}