package com.ledgerly.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ledgerly.data.database.DatabaseProvider
import com.ledgerly.data.database.toEntity
import com.ledgerly.data.database.toModel
import com.ledgerly.data.models.Budget
import com.ledgerly.data.models.Category
import com.ledgerly.data.models.Transaction
import com.ledgerly.data.repository.BudgetRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class BudgetViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: BudgetRepository

    private val _budgets = mutableStateListOf<Budget>()

    val budgets: List<Budget>
        get() = _budgets

    val activeBudgets: List<Budget>
        get() = _budgets.filter {
            it.limit > 0.0
        }

    val overallBudget: Budget?
        get() = _budgets.firstOrNull {
            it.id == OVERALL_BUDGET_ID
        }

    val categoryBudgets: List<Budget>
        get() = _budgets.filter {
            it.id != OVERALL_BUDGET_ID &&
                it.limit > 0.0
        }

    val hasOverallBudget: Boolean
        get() = overallBudget != null &&
            (overallBudget?.limit ?: 0.0) > 0.0

    val allocatedTotal: Double
        get() = categoryBudgets.sumOf {
            it.limit
        }

    val unallocatedAmount: Double
        get() = (overallBudget?.limit ?: 0.0) - allocatedTotal

    init {
        val database = DatabaseProvider.getDatabase(application)

        repository = BudgetRepository(
            budgetDao = database.budgetDao()
        )

        viewModelScope.launch {
            repository.budgets.collect { entities ->
                _budgets.clear()
                _budgets.addAll(
                    entities.map { entity ->
                        entity.toModel()
                    }
                )
            }
        }
    }

    fun addBudget(
        category: Category,
        limit: Double
    ) {
        setBudget(
            category = category,
            limit = limit
        )
    }

    fun setBudget(
        category: Category,
        limit: Double
    ) {
        val existingBudget = _budgets.firstOrNull {
            it.category.id == category.id
        }

        val budget = if (existingBudget == null) {
            Budget(
                id = category.id,
                category = category,
                limit = limit,
                spent = 0.0
            )
        } else {
            existingBudget.copy(
                limit = limit
            )
        }

        viewModelScope.launch {
            repository.saveBudget(
                budget.toEntity()
            )
        }
    }

    fun setOverallBudget(
        limit: Double
    ) {
        val existingBudget = overallBudget

        val budget = if (existingBudget == null) {
            Budget(
                id = OVERALL_BUDGET_ID,
                category = overallBudgetCategory,
                limit = limit,
                spent = 0.0
            )
        } else {
            existingBudget.copy(
                limit = limit
            )
        }

        viewModelScope.launch {
            repository.saveBudget(
                budget.toEntity()
            )
        }
    }

    fun saveBudgetPlan(
        overallLimit: Double,
        allocations: List<Pair<Category, Double>>
    ) {
        setOverallBudget(
            limit = overallLimit
        )

        allocations.forEach { allocation ->
            val category = allocation.first
            val limit = allocation.second

            if (limit > 0.0) {
                setBudget(
                    category = category,
                    limit = limit
                )
            }
        }
    }

    fun refreshCurrentMonthBudgets(
        transactions: List<Transaction>
    ) {
        val today = LocalDate.now()
        val startDate = today.withDayOfMonth(1)
        val endDate = today.withDayOfMonth(
            today.lengthOfMonth()
        )

        updateBudgets(
            transactions = transactions,
            startDate = startDate,
            endDate = endDate
        )
    }

    fun updateBudgets(
        transactions: List<Transaction>,
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        _budgets.indices.forEach { index ->
            val budget = _budgets[index]

            val spent = if (budget.id == OVERALL_BUDGET_ID) {
                transactions
                    .filter {
                        !it.isIncome &&
                            !it.date.isBefore(startDate) &&
                            !it.date.isAfter(endDate)
                    }
                    .sumOf {
                        it.amount
                    }
            } else {
                transactions
                    .filter {
                        !it.isIncome &&
                            it.category.id == budget.category.id &&
                            !it.date.isBefore(startDate) &&
                            !it.date.isAfter(endDate)
                    }
                    .sumOf {
                        it.amount
                    }
            }

            val updatedBudget = budget.copy(
                spent = spent
            )

            _budgets[index] = updatedBudget

            viewModelScope.launch {
                repository.saveBudget(
                    updatedBudget.toEntity()
                )
            }
        }
    }

    fun budgetFor(
        category: Category
    ): Budget? {
        return _budgets.firstOrNull {
            it.category.id == category.id
        }
    }

    fun deleteBudget(
        budget: Budget
    ) {
        viewModelScope.launch {
            repository.deleteBudgetById(
                budget.id
            )
        }
    }

    fun deleteOverallBudget() {
        viewModelScope.launch {
            repository.deleteBudgetById(
                OVERALL_BUDGET_ID
            )
        }
    }

    val totalLimit: Double
        get() = overallBudget?.limit
            ?: categoryBudgets.sumOf {
                it.limit
            }

    val totalSpent: Double
        get() = overallBudget?.spent
            ?: categoryBudgets.sumOf {
                it.spent
            }

    val totalRemaining: Double
        get() = totalLimit - totalSpent

    companion object {
        const val OVERALL_BUDGET_ID = "__overall__"

        val overallBudgetCategory = Category(
            id = OVERALL_BUDGET_ID,
            name = "Overall Budget",
            systemIcon = "wallet",
            isDefault = true
        )
    }
}