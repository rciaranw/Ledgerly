package com.ledgerly.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateHelper {

    fun formatDate(
        date: LocalDate,
        format: String = "DD/MM/YYYY"
    ): String {
        val formatter =
            DateTimeFormatter.ofPattern(
                patternFor(format),
                Locale.UK
            )

        return date.format(
            formatter
        )
    }

    fun startOfMonth(
        date: LocalDate
    ): LocalDate {
        return date.withDayOfMonth(1)
    }

    fun endOfMonth(
        date: LocalDate
    ): LocalDate {
        return date.withDayOfMonth(
            date.lengthOfMonth()
        )
    }

    private fun patternFor(
        format: String
    ): String {
        return when (format) {
            "MM/DD/YYYY" ->
                "MM/dd/yyyy"

            "YYYY-MM-DD" ->
                "yyyy-MM-dd"

            "DD MMM YYYY" ->
                "dd MMM yyyy"

            "MMM DD, YYYY" ->
                "MMM dd, yyyy"

            else ->
                "dd/MM/yyyy"
        }
    }
}