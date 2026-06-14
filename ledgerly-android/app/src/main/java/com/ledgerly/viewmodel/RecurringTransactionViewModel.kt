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

        repository =
            RecurringTransactionRepository(
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
        val today =
            LocalDate.now()

        val generatedTransactions =
            mutableListOf<Transaction>()

        recurringTransactions.forEach { recurring ->
            val endDate =
                recurring.endDate

            if (
                endDate != null &&
                endDate.isBefore(recurring.startDate)
            ) {
                return@forEach
            }

            var nextDate =
                if (recurring.lastGeneratedDate == null) {
                    recurring.startDate
                } else {
                    nextOccurrenceDate(
                        currentDate =
                            recurring.lastGeneratedDate,
                        interval =
                            recurring.interval,
                        unit =
                            recurring.unit
                    )
                }

            var lastSuccessfullyGeneratedDate:
                LocalDate? = null

            while (
                !nextDate.isAfter(today) &&
                (
                    endDate == null ||
                        !nextDate.isAfter(endDate)
                    )
            ) {
                generatedTransactions.add(
                    Transaction(
                        id =
                            UUID.randomUUID().toString(),
                        title =
                            recurring.title,
                        notes =
                            recurring.notes,
                        amount =
                            recurring.amount,
                        date =
                            nextDate,
                        category =
                            recurring.category,
                        isIncome =
                            recurring.isIncome
                    )
                )

                lastSuccessfullyGeneratedDate =
                    nextDate

                nextDate =
                    nextOccurrenceDate(
                        currentDate =
                            nextDate,
                        interval =
                            recurring.interval,
                        unit =
                            recurring.unit
                    )
            }

            if (lastSuccessfullyGeneratedDate != null) {
                val updatedRecurring =
                    recurring.copy(
                        lastGeneratedDate =
                            lastSuccessfullyGeneratedDate
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
        val safeInterval =
            interval.coerceAtLeast(1)

        return when (unit) {
            "DAILY" ->
                currentDate.plusDays(
                    safeInterval.toLong()
                )

            "WEEKLY" ->
                currentDate.plusWeeks(
                    safeInterval.toLong()
                )

            "MONTHLY" ->
                currentDate.plusMonths(
                    safeInterval.toLong()
                )

            "YEARLY" ->
                currentDate.plusYears(
                    safeInterval.toLong()
                )

            else ->
                currentDate.plusMonths(
                    safeInterval.toLong()
                )
        }
    }
}