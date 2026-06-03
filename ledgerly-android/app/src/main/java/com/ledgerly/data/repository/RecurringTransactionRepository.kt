package com.ledgerly.data.repository

import com.ledgerly.data.database.RecurringTransactionDao
import com.ledgerly.data.database.RecurringTransactionEntity
import kotlinx.coroutines.flow.Flow

class RecurringTransactionRepository(
    private val recurringTransactionDao: RecurringTransactionDao
) {
    val recurringTransactions: Flow<List<RecurringTransactionEntity>> =
        recurringTransactionDao.getAllRecurringTransactions()

    suspend fun saveRecurringTransaction(
        recurringTransaction: RecurringTransactionEntity
    ) {
        recurringTransactionDao.insertRecurringTransaction(
            recurringTransaction
        )
    }

    suspend fun deleteRecurringTransactionById(
        recurringTransactionId: String
    ) {
        recurringTransactionDao.deleteRecurringTransactionById(
            recurringTransactionId
        )
    }
}