package com.ledgerly.utils

import com.ledgerly.data.models.PeriodType
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DatePeriodHelper {

    fun startDate(
        anchorDate: LocalDate,
        periodType: PeriodType,
        weekStartDay: Int,
        monthStartDay: Int
    ): LocalDate {

        return when (periodType) {

            PeriodType.DAY -> anchorDate

            PeriodType.WEEK -> {
                val startDay =
                    DayOfWeek.of(weekStartDay)

                var current = anchorDate

                while (current.dayOfWeek != startDay) {
                    current = current.minusDays(1)
                }

                current
            }

            PeriodType.MONTH -> {

                val day =
                    monthStartDay
                        .coerceIn(1, 28)

                if (anchorDate.dayOfMonth >= day) {
                    anchorDate.withDayOfMonth(day)
                } else {
                    anchorDate
                        .minusMonths(1)
                        .withDayOfMonth(day)
                }
            }

            PeriodType.YEAR -> {
                LocalDate.of(
                    anchorDate.year,
                    1,
                    1
                )
            }
        }
    }

    fun endDate(
        anchorDate: LocalDate,
        periodType: PeriodType,
        weekStartDay: Int,
        monthStartDay: Int
    ): LocalDate {

        val start =
            startDate(
                anchorDate,
                periodType,
                weekStartDay,
                monthStartDay
            )

        return when (periodType) {

            PeriodType.DAY ->
                start

            PeriodType.WEEK ->
                start.plusDays(6)

            PeriodType.MONTH ->
                start.plusMonths(1)
                    .minusDays(1)

            PeriodType.YEAR ->
                LocalDate.of(
                    start.year,
                    12,
                    31
                )
        }
    }

    fun formatLabel(
        anchorDate: LocalDate,
        periodType: PeriodType,
        weekStartDay: Int,
        monthStartDay: Int
    ): String {

        return when (periodType) {

            PeriodType.DAY ->
                anchorDate.format(
                    DateTimeFormatter.ofPattern(
                        "dd MMM yyyy"
                    )
                )

            PeriodType.WEEK -> {

                val start =
                    startDate(
                        anchorDate,
                        periodType,
                        weekStartDay,
                        monthStartDay
                    )

                val end =
                    endDate(
                        anchorDate,
                        periodType,
                        weekStartDay,
                        monthStartDay
                    )

                "${start.format(DateTimeFormatter.ofPattern("dd/MM/yy"))} - ${
                    end.format(DateTimeFormatter.ofPattern("dd/MM/yy"))
                }"
            }

            PeriodType.MONTH -> {

                val start =
                    startDate(
                        anchorDate,
                        periodType,
                        weekStartDay,
                        monthStartDay
                    )

                start.format(
                    DateTimeFormatter.ofPattern(
                        "MMMM yyyy"
                    )
                )
            }

            PeriodType.YEAR ->
                anchorDate.year.toString()
        }
    }
}