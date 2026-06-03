package com.ledgerly.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ledgerly.data.database.DatabaseProvider
import com.ledgerly.data.database.toEntity
import com.ledgerly.data.database.toModel
import com.ledgerly.data.models.Category
import com.ledgerly.data.models.RecurringTransaction
import com.ledgerly.data.models.Transaction
import com.ledgerly.data.repository.RecurringTransactionRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class RecurringTransactionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: RecurringTransactionRepository

    private val _recurringTransactions =
        mutableStateListOf<RecurringTransaction>()

    val recurringTransactions: List<RecurringTransaction>
        get() = _recurringTransactions

    init {
        val database =
            DatabaseProvider.getDatabase(application)

        repository = RecurringTransactionRepository(
            recurringTransactionDao =
                database.recurringTransactionDao()
        )

        viewModelScope.launch {
            repository.recurringTransactions.collect { entities ->
                _recurringTransactions.clear()

                _recurringTransactions.addAll(
                    entities.map { entity ->
                        entity.toModel()
                    }
                )
            }
        }
    }

    fun addRecurringTransaction(
        title: String,
        notes: String,
        amount: Double,
        category: Category,
        isIncome: Boolean,
        interval: Int,
        unit: String,
        startDate: LocalDate,
        endDate: LocalDate?
    ) {
        val recurringTransaction =
            RecurringTransaction(
                id = UUID.randomUUID().toString(),
                title = title,
                notes = notes,
                amount = amount,
                category = category,
                isIncome = isIncome,
                interval = interval,
                unit = unit,
                startDate = startDate,
                endDate = endDate,
                lastGeneratedDate = null
            )

        saveRecurringTransaction(
            recurringTransaction
        )
    }

    fun saveRecurringTransaction(
        recurringTransaction: RecurringTransaction
    ) {
        viewModelScope.launch {
            repository.saveRecurringTransaction(
                recurringTransaction.toEntity()
            )
        }
    }

    fun deleteRecurringTransaction(
        recurringTransaction: RecurringTransaction
    ) {
        viewModelScope.launch {
            repository.deleteRecurringTransactionById(
                recurringTransaction.id
            )
        }
    }

    fun generateDueTransactions(): List<Transaction> {
        val today = LocalDate.now()

        val generatedTransactions =
            mutableListOf<Transaction>()

        recurringTransactions.forEach { recurring ->

            var generatedForCurrentRecurring = false

            var nextDate =
                recurring.lastGeneratedDate
                    ?: recurring.startDate.minusDays(1)

            while (true) {
                nextDate = nextOccurrenceDate(
                    currentDate = nextDate,
                    interval = recurring.interval,
                    unit = recurring.unit
                )

                if (nextDate.isAfter(today)) {
                    break
                }

                val endDate = recurring.endDate

                if (endDate != null && nextDate.isAfter(endDate)) {
                    break
                }

                generatedTransactions.add(
                    Transaction(
                        id = UUID.randomUUID().toString(),
                        title = recurring.title,
                        notes = recurring.notes,
                        amount = recurring.amount,
                        date = nextDate,
                        category = recurring.category,
                        isIncome = recurring.isIncome
                    )
                )

                generatedForCurrentRecurring = true
            }

            if (generatedForCurrentRecurring) {
                val updatedRecurring =
                    recurring.copy(
                        lastGeneratedDate = nextDate
                    )

                saveRecurringTransaction(
                    updatedRecurring
                )
            }
        }

        return generatedTransactions
    }

    private fun nextOccurrenceDate(
        currentDate: LocalDate,
        interval: Int,
        unit: String
    ): LocalDate {
        return when (unit) {
            "DAILY" -> currentDate.plusDays(
                interval.toLong()
            )

            "WEEKLY" -> currentDate.plusWeeks(
                interval.toLong()
            )

            "MONTHLY" -> currentDate.plusMonths(
                interval.toLong()
            )

            "YEARLY" -> currentDate.plusYears(
                interval.toLong()
            )

            else -> currentDate.plusMonths(
                interval.toLong()
            )
        }
    }
}