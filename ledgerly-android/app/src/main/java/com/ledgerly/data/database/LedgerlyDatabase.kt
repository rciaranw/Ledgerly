package com.ledgerly.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        TransactionEntity::class,
        BudgetEntity::class,
        CategoryEntity::class,
        SettingsEntity::class,
        RecurringTransactionEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class LedgerlyDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    abstract fun budgetDao(): BudgetDao

    abstract fun categoryDao(): CategoryDao

    abstract fun settingsDao(): SettingsDao

    abstract fun recurringTransactionDao(): RecurringTransactionDao
}