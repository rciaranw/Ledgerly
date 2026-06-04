package com.ledgerly.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recurring_transactions")
data class RecurringTransactionEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val notes: String,
    val amount: Double,
    val categoryId: String,
    val categoryName: String,
    val categoryIcon: String,
    val isIncome: Boolean,
    val interval: Int,
    val unit: String,
    val startDate: String,
    val endDate: String?,
    val lastGeneratedDate: String?
)