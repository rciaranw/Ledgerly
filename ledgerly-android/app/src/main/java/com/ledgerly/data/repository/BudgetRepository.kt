package com.ledgerly.data.repository

import com.ledgerly.data.database.BudgetDao
import com.ledgerly.data.database.BudgetEntity
import kotlinx.coroutines.flow.Flow

class BudgetRepository(
    private val budgetDao: BudgetDao
) {

    val budgets: Flow<List<BudgetEntity>> =
        budgetDao.getAllBudgets()

    suspend fun saveBudget(
        budget: BudgetEntity
    ) {
        budgetDao.insertBudget(budget)
    }

    suspend fun deleteBudgetById(
        budgetId: String
    ) {
        budgetDao.deleteBudgetById(
            budgetId
        )
    }
}