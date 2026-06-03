package com.ledgerly.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringTransactionDao {

    @Query("SELECT * FROM recurring_transactions ORDER BY startDate DESC")
    fun getAllRecurringTransactions(): Flow<List<RecurringTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecurringTransaction(
        recurringTransaction: RecurringTransactionEntity
    )

    @Query("DELETE FROM recurring_transactions WHERE id = :recurringTransactionId")
    suspend fun deleteRecurringTransactionById(
        recurringTransactionId: String
    )
}