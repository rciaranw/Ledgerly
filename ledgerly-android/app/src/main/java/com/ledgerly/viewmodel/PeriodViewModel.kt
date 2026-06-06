package com.ledgerly.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ledgerly.data.models.PeriodType
import java.time.LocalDate

class PeriodViewModel : ViewModel() {

    var periodType by mutableStateOf(
        PeriodType.MONTH
    )
    private set

    var anchorDate by mutableStateOf(
        LocalDate.now()
    )
        private set

    fun updatePeriodType(
        type: PeriodType
    ) {
        periodType = type
    }

    fun previousPeriod() {
        anchorDate =
            when (periodType) {
                PeriodType.DAY ->
                    anchorDate.minusDays(1)

                PeriodType.WEEK ->
                    anchorDate.minusWeeks(1)

                PeriodType.MONTH ->
                    anchorDate.minusMonths(1)

                PeriodType.YEAR ->
                    anchorDate.minusYears(1)
            }
    }
    fun nextPeriod() {
        anchorDate =
            when (periodType) {
                PeriodType.DAY ->
                    anchorDate.plusDays(1)

                PeriodType.WEEK ->
                    anchorDate.plusWeeks(1)

                PeriodType.MONTH ->
                    anchorDate.plusMonths(1)

                PeriodType.YEAR ->
                    anchorDate.plusYears(1)
            }
    }

    fun resetToToday() {
        anchorDate = LocalDate.now()
    }
}