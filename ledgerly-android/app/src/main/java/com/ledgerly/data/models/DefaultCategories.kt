package com.ledgerly.data.models

object DefaultCategories {

    val all = listOf(
        Category(
            id = "cash",
            name = "Cash",
            systemIcon = "payments",
            isDefault = true
        ),
        Category(
            id = "cashback",
            name = "Cashback",
            systemIcon = "undo",
            isDefault = true
        ),
        Category(
            id = "charity",
            name = "Charity",
            systemIcon = "volunteer_activism",
            isDefault = true
        ),
        Category(
            id = "child_allowance",
            name = "Child Allowance",
            systemIcon = "child_care",
            isDefault = true
        ),
        Category(
            id = "credit",
            name = "Credit",
            systemIcon = "credit_card",
            isDefault = true
        ),
        Category(
            id = "entertainment",
            name = "Entertainment",
            systemIcon = "movie",
            isDefault = true
        ),
        Category(
            id = "gambling",
            name = "Gambling",
            systemIcon = "casino",
            isDefault = true
        ),
        Category(
            id = "general",
            name = "General",
            systemIcon = "category",
            isDefault = true
        ),
        Category(
            id = "gift",
            name = "Gift",
            systemIcon = "redeem",
            isDefault = true
        ),
        Category(
            id = "health",
            name = "Health",
            systemIcon = "medical_services",
            isDefault = true
        ),
        Category(
            id = "insurance",
            name = "Insurance",
            systemIcon = "shield",
            isDefault = true
        ),
        Category(
            id = "interest",
            name = "Interest",
            systemIcon = "percent",
            isDefault = true
        ),
        Category(
            id = "investment",
            name = "Investment",
            systemIcon = "trending_up",
            isDefault = true
        ),
        Category(
            id = "loan",
            name = "Loan",
            systemIcon = "account_balance",
            isDefault = true
        ),
        Category(
            id = "net_sales",
            name = "Net Sales",
            systemIcon = "point_of_sale",
            isDefault = true
        ),
        Category(
            id = "refund",
            name = "Refund",
            systemIcon = "assignment_return",
            isDefault = true
        ),
        Category(
            id = "remittances",
            name = "Remittances",
            systemIcon = "sync_alt",
            isDefault = true
        ),
        Category(
            id = "restaurants",
            name = "Restaurants",
            systemIcon = "restaurant",
            isDefault = true
        ),
        Category(
            id = "salary",
            name = "Salary",
            systemIcon = "work",
            isDefault = true
        ),
        Category(
            id = "savings",
            name = "Savings",
            systemIcon = "savings",
            isDefault = true
        ),
        Category(
            id = "shopping",
            name = "Shopping",
            systemIcon = "shopping_bag",
            isDefault = true
        ),
        Category(
            id = "top_ups",
            name = "Top Ups",
            systemIcon = "add_circle",
            isDefault = true
        ),
        Category(
            id = "transfers",
            name = "Transfers",
            systemIcon = "swap_horiz",
            isDefault = true
        ),
        Category(
            id = "travel",
            name = "Travel",
            systemIcon = "flight",
            isDefault = true
        ),
        Category(
            id = "utilities",
            name = "Utilities",
            systemIcon = "bolt",
            isDefault = true
        )
    )

    val fallback: Category = all.first { it.id == "general" }
}