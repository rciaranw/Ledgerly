package com.ledgerly.data.database

import com.ledgerly.data.models.Category
import com.ledgerly.data.models.RecurringTransaction
import java.time.LocalDate

fun RecurringTransaction.toEntity(): RecurringTransactionEntity {
    return RecurringTransactionEntity(
        id = this.id,
        title = this.title,
        notes = this.notes,
        amount = this.amount,
        categoryId = this.category.id,
        categoryName = this.category.name,
        categoryIcon = this.category.systemIcon,
        isIncome = this.isIncome,
        interval = this.interval,
        unit = this.unit,
        startDate = this.startDate.toString(),
        endDate = this.endDate?.toString(),
        lastGeneratedDate = this.lastGeneratedDate?.toString()
    )
}

fun RecurringTransactionEntity.toModel(): RecurringTransaction {
    return RecurringTransaction(
        id = this.id,
        title = this.title,
        notes = this.notes,
        amount = this.amount,
        category = Category(
            id = this.categoryId,
            name = this.categoryName,
            systemIcon = this.categoryIcon,
            isDefault = false
        ),
        isIncome = this.isIncome,
        interval = this.interval,
        unit = this.unit,
        startDate = LocalDate.parse(this.startDate),
        endDate = this.endDate?.let {
            LocalDate.parse(it)
        },
        lastGeneratedDate = this.lastGeneratedDate?.let {
            LocalDate.parse(it)
        }
    )
}