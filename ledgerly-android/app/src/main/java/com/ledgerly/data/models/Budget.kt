package com.ledgerly.data.models

data class Budget(
    val id: String,
    val category: Category,
    val limit: Double,
    val spent: Double = 0.0
) {
    val remaining: Double
        get() = limit - spent

    val progress: Double
        get() = if (limit <= 0.0) 0.0 else spent / limit

    val isOverBudget: Boolean
        get() = spent > limit
}