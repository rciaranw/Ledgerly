package com.ledgerly.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ledgerly.data.database.DatabaseProvider
import com.ledgerly.data.database.toEntity
import com.ledgerly.data.database.toModel
import com.ledgerly.data.models.Category
import com.ledgerly.data.models.DefaultCategories
import com.ledgerly.data.repository.CategoryRepository
import kotlinx.coroutines.launch
import java.util.UUID

class CategoryViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: CategoryRepository

    private val _userCategories =
        mutableStateListOf<Category>()

    val userCategories: List<Category>
        get() = _userCategories

    val allCategories: List<Category>
        get() = DefaultCategories.all + _userCategories

    init {
        val database =
            DatabaseProvider.getDatabase(application)

        repository = CategoryRepository(
            categoryDao = database.categoryDao()
        )

        viewModelScope.launch {
            repository.categories.collect { entities ->
                _userCategories.clear()

                _userCategories.addAll(
                    entities.map {
                        it.toModel()
                    }
                )
            }
        }
    }

    fun addCategory(
        name: String,
        icon: String = "label"
    ) {
        val trimmedName = name.trim()

        if (trimmedName.isBlank()) {
            return
        }

        val exists = allCategories.any {
            it.name.equals(
                trimmedName,
                ignoreCase = true
            )
        }

        if (exists) {
            return
        }

        val category = Category(
            id = UUID.randomUUID().toString(),
            name = trimmedName,
            systemIcon = icon,
            isDefault = false
        )

        viewModelScope.launch {
            repository.saveCategory(
                category.toEntity()
            )
        }
    }

    fun deleteCategory(
        category: Category
    ) {
        if (category.isDefault) {
            return
        }

        viewModelScope.launch {
            repository.deleteCategoryById(
                category.id
            )
        }
    }
}