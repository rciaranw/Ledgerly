package com.ledgerly.data.models

import java.time.LocalDate

data class RecurringRule(
    val interval: Int,
    val unit: RecurringUnit,
    val startDate: LocalDate,
    val endDate: LocalDate? = null
)