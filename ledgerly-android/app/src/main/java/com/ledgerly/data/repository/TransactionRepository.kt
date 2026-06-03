package com.ledgerly.data.repository

import com.ledgerly.data.database.TransactionDao
import com.ledgerly.data.database.TransactionEntity
import kotlinx.coroutines.flow.Flow

class TransactionRepository(
    private val transactionDao: TransactionDao
) {
    val transactions: Flow<List<TransactionEntity>> =
        transactionDao.getAllTransactions()

    suspend fun saveTransaction(
        transaction: TransactionEntity
    ) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun deleteTransaction(
        transaction: TransactionEntity
    ) {
        transactionDao.deleteTransaction(transaction)
    }

    suspend fun deleteTransactionById(
        transactionId: String
    ) {
        transactionDao.deleteTransactionById(transactionId)
    }
}