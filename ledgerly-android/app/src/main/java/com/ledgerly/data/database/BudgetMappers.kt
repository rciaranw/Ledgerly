package com.ledgerly.data.database

import com.ledgerly.data.models.Budget
import com.ledgerly.data.models.Category

fun Budget.toEntity(): BudgetEntity {
    return BudgetEntity(
        id = this.id,
        categoryId = this.category.id,
        categoryName = this.category.name,
        categoryIcon = this.category.systemIcon,
        limit = this.limit,
        spent = this.spent
    )
}

fun BudgetEntity.toModel(): Budget {
    val category = Category(
        id = this.categoryId,
        name = this.categoryName,
        systemIcon = this.categoryIcon,
        isDefault = false
    )

    return Budget(
        id = this.id,
        category = category,
        limit = this.limit,
        spent = this.spent
    )
}