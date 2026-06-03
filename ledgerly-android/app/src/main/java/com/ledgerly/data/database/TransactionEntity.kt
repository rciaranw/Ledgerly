package com.ledgerly.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val notes: String,
    val amount: Double,
    val date: String,
    val categoryId: String,
    val categoryName: String,
    val categoryIcon: String,
    val isIncome: Boolean
)