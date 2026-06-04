package com.ledgerly.utils

import com.ledgerly.data.models.Transaction

object CSVExporter {

    fun exportTransactions(
        transactions: List<Transaction>
    ): String {
        val header =
            "id,title,notes,amount,date,category,isIncome"

        val rows = transactions.map { transaction ->
            listOf(
                transaction.id,
                transaction.title,
                transaction.notes,
                transaction.amount.toString(),
                transaction.date.toString(),
                transaction.category.name,
                transaction.isIncome.toString()
            ).joinToString(",") { value ->
                escapeCsv(value)
            }
        }

        return listOf(header)
            .plus(rows)
            .joinToString("\n")
    }

    private fun escapeCsv(
        value: String
    ): String {
        val escaped =
            value.replace("\"", "\"\"")

        return "\"$escaped\""
    }
}