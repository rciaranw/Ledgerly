package com.ledgerly.data.models

import java.time.LocalDate

data class Transaction(
    val id: String,
    val title: String,
    val notes: String = "",
    val amount: Double,
    val date: LocalDate,
    val category: Category,
    val isIncome: Boolean,
    val recurringRule: RecurringRule? = null
)