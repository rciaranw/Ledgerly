package com.ledgerly.utils

import com.ledgerly.data.models.Transaction
import com.ledgerly.ui.components.ChartDataItem
import java.time.format.TextStyle
import java.util.Locale

object AnalysisDataHelper {

    fun monthlyTrendData(
        transactions: List<Transaction>
    ): List<ChartDataItem> {

        return transactions
            .filter { !it.isIncome }
            .groupBy {
                it.date.month
            }
            .map { entry ->

                ChartDataItem(
                    label =
                        entry.key.getDisplayName(
                            TextStyle.SHORT,
                            Locale.UK
                        ),
                    amount =
                        entry.value.sumOf {
                            it.amount
                        }
                )
            }
            .sortedBy {
                monthOrder(it.label)
            }
    }

    fun categoryBreakdownData(
        transactions: List<Transaction>
    ): List<ChartDataItem> {

        return transactions
            .filter { !it.isIncome }
            .groupBy {
                it.category.name
            }
            .map { entry ->

                ChartDataItem(
                    label = entry.key,
                    amount =
                        entry.value.sumOf {
                            it.amount
                        }
                )
            }
            .sortedByDescending {
                it.amount
            }
    }

    private fun monthOrder(
        month: String
    ): Int {

        return when (month) {

            "Jan" -> 1
            "Feb" -> 2
            "Mar" -> 3
            "Apr" -> 4
            "May" -> 5
            "Jun" -> 6
            "Jul" -> 7
            "Aug" -> 8
            "Sep" -> 9
            "Oct" -> 10
            "Nov" -> 11
            "Dec" -> 12

            else -> 99
        }
    }
}