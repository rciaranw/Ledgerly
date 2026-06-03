package com.ledgerly.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey
    val id: String,
    val categoryId: String,
    val categoryName: String,
    val categoryIcon: String,
    val limit: Double,
    val spent: Double
)