package com.ledgerly.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ledgerly.data.database.DatabaseProvider
import com.ledgerly.data.database.toEntity
import com.ledgerly.data.database.toModel
import com.ledgerly.data.models.Transaction
import com.ledgerly.data.repository.TransactionRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class TransactionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: TransactionRepository

    private val _transactions =
        mutableStateListOf<Transaction>()

    val transactions: List<Transaction>
        get() = _transactions

    init {
        val database =
            DatabaseProvider.getDatabase(application)

        repository =
            TransactionRepository(
                transactionDao =
                    database.transactionDao()
            )

        viewModelScope.launch {
            repository.transactions.collect { entities ->
                val updatedTransactions =
                    entities
                        .map { entity ->
                            entity.toModel()
                        }
                        .sortedWith(
                            compareByDescending<Transaction> {
                                it.date
                            }.thenByDescending {
                                it.id
                            }
                        )

                _transactions.clear()
                _transactions.addAll(
                    updatedTransactions
                )
            }
        }
    }

    fun addTransaction(
        transaction: Transaction
    ) {
        viewModelScope.launch {
            repository.saveTransaction(
                transaction.toEntity()
            )
        }
    }

    fun addTransactions(
        transactions: List<Transaction>
    ) {
        if (transactions.isEmpty()) {
            return
        }

        viewModelScope.launch {
            transactions.forEach { transaction ->
                repository.saveTransaction(
                    transaction.toEntity()
                )
            }
        }
    }

    fun deleteTransaction(
        transaction: Transaction
    ) {
        viewModelScope.launch {
            repository.deleteTransactionById(
                transaction.id
            )
        }
    }

    fun updateTransaction(
        transaction: Transaction
    ) {
        viewModelScope.launch {
            repository.saveTransaction(
                transaction.toEntity()
            )
        }
    }

    fun transactionsBetween(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Transaction> {
        return transactions
            .filter { transaction ->
                !transaction.date.isBefore(
                    startDate
                ) &&
                    !transaction.date.isAfter(
                        endDate
                    )
            }
            .sortedWith(
                compareByDescending<Transaction> {
                    it.date
                }.thenByDescending {
                    it.id
                }
            )
    }
}