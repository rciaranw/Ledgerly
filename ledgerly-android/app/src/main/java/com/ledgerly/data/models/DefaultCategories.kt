package com.ledgerly.data.models

object DefaultCategories {

    val all = listOf(
        Category(
            id = "general",
            name = "General",
            systemIcon = "category",
            isDefault = true
        ),
        Category(
            id = "salary",
            name = "Salary",
            systemIcon = "work",
            isDefault = true
        ),
        Category(
            id = "food_drink",
            name = "Food & Drink",
            systemIcon = "restaurant",
            isDefault = true
        ),
        Category(
            id = "shopping",
            name = "Shopping",
            systemIcon = "shopping_bag",
            isDefault = true
        ),
        Category(
            id = "bills",
            name = "Bills",
            systemIcon = "receipt_long",
            isDefault = true
        ),
        Category(
            id = "transport",
            name = "Transport",
            systemIcon = "directions_car",
            isDefault = true
        ),
        Category(
            id = "health",
            name = "Health",
            systemIcon = "medical_services",
            isDefault = true
        ),
        Category(
            id = "entertainment",
            name = "Entertainment",
            systemIcon = "movie",
            isDefault = true
        ),
        Category(
            id = "savings",
            name = "Savings",
            systemIcon = "savings",
            isDefault = true
        ),
        Category(
            id = "transfers",
            name = "Transfers",
            systemIcon = "swap_horiz",
            isDefault = true
        ),
        Category(
            id = "cash",
            name = "Cash",
            systemIcon = "payments",
            isDefault = true
        )
    )

    val fallback: Category =
        all.first {
            it.id == "general"
        }
}