package com.ledgerly.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.data.models.Category
import com.ledgerly.ui.components.CategoryIcon
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.utils.CurrencyFormatter
import com.ledgerly.viewmodel.BudgetViewModel
import com.ledgerly.viewmodel.CategoryViewModel

private data class BudgetAllocationInput(
    val category: Category,
    val amountText: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBudgetScreen(
    budgetViewModel: BudgetViewModel,
    categoryViewModel: CategoryViewModel,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val categories =
        categoryViewModel.allCategories

    var overallBudgetText by remember {
        mutableStateOf(
            budgetViewModel.overallBudget
                ?.limit
                ?.takeIf { it > 0.0 }
                ?.toString()
                ?: ""
        )
    }

    val allocations =
        remember {
            mutableStateListOf<BudgetAllocationInput>().apply {
                budgetViewModel.categoryBudgets.forEach { budget ->
                    add(
                        BudgetAllocationInput(
                            category = budget.category,
                            amountText = budget.limit.toString()
                        )
                    )
                }
            }
        }

    var selectedCategory by remember {
        mutableStateOf<Category?>(
            categories.firstOrNull()
        )
    }

    var allocationAmountText by remember {
        mutableStateOf("")
    }

    var categoryExpanded by remember {
        mutableStateOf(false)
    }

    var validationMessage by remember {
        mutableStateOf<String?>(null)
    }

    val overallLimit =
        overallBudgetText.toDoubleOrNull() ?: 0.0

    val allocatedTotal =
        allocations.sumOf {
            it.amountText.toDoubleOrNull() ?: 0.0
        }

    val unallocated =
        overallLimit - allocatedTotal

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Budget",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation =
                CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Overall Budget",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Set the total amount you want to spend during the selected period.",
                    style = MaterialTheme.typography.bodySmall
                )

                OutlinedTextField(
                    value = overallBudgetText,
                    onValueChange = {
                        overallBudgetText = it
                        validationMessage = null
                    },
                    label = {
                        Text("Overall Budget")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Allocated: ${
                        CurrencyFormatter.format(
                            amount = allocatedTotal,
                            currencyCode = "GBP"
                        )
                    }",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Unallocated: ${
                        CurrencyFormatter.format(
                            amount = unallocated,
                            currencyCode = "GBP"
                        )
                    }",
                    color = if (unallocated < 0.0) {
                        LedgerlyExpenseRed
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation =
                CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Category Breakdown",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Optional. Break your overall budget into categories if you want more control.",
                    style = MaterialTheme.typography.bodySmall
                )

                if (allocations.isEmpty()) {
                    Text(
                        text = "No category breakdown added yet.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    allocations.forEachIndexed { index, allocation ->
                        AllocationRow(
                            allocation = allocation,
                            currencyCode = "GBP",
                            onRemove = {
                                allocations.removeAt(index)
                            }
                        )
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = {
                        categoryExpanded =
                            !categoryExpanded
                    }
                ) {
                    OutlinedTextField(
                        value =
                            selectedCategory?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Category")
                        },
                        leadingIcon = {
                            selectedCategory?.let { category ->
                                CategoryIcon(
                                    iconName =
                                        category.systemIcon,
                                    contentDescription =
                                        category.name,
                                    modifier =
                                        Modifier.size(22.dp)
                                )
                            }
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults
                                .TrailingIcon(
                                    expanded =
                                        categoryExpanded
                                )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = {
                            categoryExpanded = false
                        }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment =
                                            Alignment.CenterVertically,
                                        horizontalArrangement =
                                            Arrangement.spacedBy(10.dp)
                                    ) {
                                        Surface(
                                            modifier =
                                                Modifier.size(32.dp),
                                            shape = CircleShape,
                                            color =
                                                MaterialTheme
                                                    .colorScheme
                                                    .background
                                        ) {
                                            Row(
                                                horizontalArrangement =
                                                    Arrangement.Center,
                                                verticalAlignment =
                                                    Alignment.CenterVertically
                                            ) {
                                                CategoryIcon(
                                                    iconName =
                                                        category.systemIcon,
                                                    contentDescription =
                                                        category.name,
                                                    modifier =
                                                        Modifier.size(20.dp)
                                                )
                                            }
                                        }

                                        Text(category.name)
                                    }
                                },
                                onClick = {
                                    selectedCategory = category
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = allocationAmountText,
                    onValueChange = {
                        allocationAmountText = it
                        validationMessage = null
                    },
                    label = {
                        Text("Allocation Amount")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedButton(
                    onClick = {
                        val category = selectedCategory
                        val amount =
                            allocationAmountText.toDoubleOrNull()

                        if (category == null) {
                            validationMessage =
                                "Select a category."
                            return@OutlinedButton
                        }

                        if (amount == null || amount <= 0.0) {
                            validationMessage =
                                "Enter a valid allocation amount."
                            return@OutlinedButton
                        }

                        val alreadyExists =
                            allocations.any {
                                it.category.id == category.id
                            }

                        if (alreadyExists) {
                            validationMessage =
                                "This category already has an allocation."
                            return@OutlinedButton
                        }

                        allocations.add(
                            BudgetAllocationInput(
                                category = category,
                                amountText = allocationAmountText
                            )
                        )

                        allocationAmountText = ""
                        validationMessage = null
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Allocation")
                }

                validationMessage?.let { message ->
                    Text(
                        text = message,
                        color =
                            MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Button(
            onClick = {
                val overall =
                    overallBudgetText.toDoubleOrNull()

                if (overall == null || overall <= 0.0) {
                    validationMessage =
                        "Enter a valid overall budget."
                    return@Button
                }

                val validAllocations =
                    allocations.mapNotNull { allocation ->
                        val amount =
                            allocation.amountText.toDoubleOrNull()

                        if (amount == null || amount <= 0.0) {
                            null
                        } else {
                            allocation.category to amount
                        }
                    }

                val allocationTotal =
                    validAllocations.sumOf {
                        it.second
                    }

                if (allocationTotal > overall) {
                    validationMessage =
                        "Category allocations cannot exceed the overall budget."
                    return@Button
                }

                budgetViewModel.saveBudgetPlan(
                    overallLimit = overall,
                    allocations = validAllocations
                )

                onSaved()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Budget")
        }

        TextButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Cancel",
                color = LedgerlyExpenseRed
            )
        }
    }
}

@Composable
private fun AllocationRow(
    allocation: BudgetAllocationInput,
    currencyCode: String,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment =
                    Alignment.CenterVertically,
                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier.size(34.dp),
                    shape = CircleShape,
                    color =
                        MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        horizontalArrangement =
                            Arrangement.Center,
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        CategoryIcon(
                            iconName =
                                allocation.category.systemIcon,
                            contentDescription =
                                allocation.category.name,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Column {
                    Text(
                        text = allocation.category.name,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = CurrencyFormatter.format(
                            amount =
                                allocation.amountText
                                    .toDoubleOrNull()
                                    ?: 0.0,
                            currencyCode = currencyCode
                        ),
                        style =
                            MaterialTheme.typography.bodySmall
                    )
                }
            }

            TextButton(
                onClick = onRemove
            ) {
                Text(
                    text = "Remove",
                    color = LedgerlyExpenseRed
                )
            }
        }
    }
}