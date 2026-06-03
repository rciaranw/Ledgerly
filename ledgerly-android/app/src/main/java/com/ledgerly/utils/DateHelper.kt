package com.ledgerly.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateHelper {

    private val formatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun formatDate(
        date: LocalDate
    ): String {

        return date.format(formatter)
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
}