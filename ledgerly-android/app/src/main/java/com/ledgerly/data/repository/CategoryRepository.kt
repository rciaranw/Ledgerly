package com.ledgerly.data.repository

import com.ledgerly.data.database.CategoryDao
import com.ledgerly.data.database.CategoryEntity
import kotlinx.coroutines.flow.Flow

class CategoryRepository(
    private val categoryDao: CategoryDao
) {

    val categories: Flow<List<CategoryEntity>> =
        categoryDao.getUserCategories()

    suspend fun saveCategory(
        category: CategoryEntity
    ) {
        categoryDao.insertCategory(category)
    }

    suspend fun deleteCategoryById(
        categoryId: String
    ) {
        categoryDao.deleteCategoryById(
            categoryId
        )
    }
}