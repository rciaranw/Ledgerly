package com.ledgerly.data.database

import com.ledgerly.data.models.Category

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = this.id,
        name = this.name,
        systemIcon = this.systemIcon,
        isDefault = this.isDefault
    )
}

fun CategoryEntity.toModel(): Category {
    return Category(
        id = this.id,
        name = this.name,
        systemIcon = this.systemIcon,
        isDefault = this.isDefault
    )
}