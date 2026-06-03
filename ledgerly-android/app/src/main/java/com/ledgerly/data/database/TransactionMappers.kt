package com.ledgerly.data.database

import com.ledgerly.data.models.Category
import com.ledgerly.data.models.Transaction
import java.time.LocalDate

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = this.id,
        title = this.title,
        notes = this.notes,
        amount = this.amount,
        date = this.date.toString(),
        categoryId = this.category.id,
        categoryName = this.category.name,
        categoryIcon = this.category.systemIcon,
        isIncome = this.isIncome
    )
}

fun TransactionEntity.toModel(): Transaction {
    val category = Category(
        id = this.categoryId,
        name = this.categoryName,
        systemIcon = this.categoryIcon,
        isDefault = false
    )

    return Transaction(
        this.id,
        this.title,
        this.notes,
        this.amount,
        LocalDate.parse(this.date),
        category,
        this.isIncome,
        null
    )
}