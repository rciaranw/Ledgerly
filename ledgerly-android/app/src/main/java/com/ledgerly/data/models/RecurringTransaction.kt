package com.ledgerly.data.models

import java.time.LocalDate

data class RecurringTransaction(
    val id: String,
    val title: String,
    val notes: String,
    val amount: Double,
    val category: Category,
    val isIncome: Boolean,
    val interval: Int,
    val unit: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val lastGeneratedDate: LocalDate?
)