package com.ledgerly.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledgerly.ui.components.CategoryIcon
import com.ledgerly.viewmodel.CategoryViewModel

@Composable
fun AddCategoryScreen(
    categoryViewModel: CategoryViewModel,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    var name by remember {
        mutableStateOf("")
    }

    var icon by remember {
        mutableStateOf("label")
    }

    var validationMessage by remember {
        mutableStateOf<String?>(null)
    }

    val availableIcons = listOf(
        "label" to "General",
        "salary" to "Salary",
        "wallet" to "Wallet",
        "food" to "Food",
        "restaurant" to "Restaurant",
        "coffee" to "Coffee",
        "shopping" to "Shopping",
        "gift" to "Gift",
        "travel" to "Travel",
        "car" to "Car",
        "transport" to "Transport",
        "home" to "Home",
        "bills" to "Bills",
        "health" to "Health",
        "entertainment" to "Entertainment",
        "pets" to "Pets",
        "savings" to "Savings"
    )

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
            text = "Add Category",
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
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        validationMessage = null
                    },
                    label = {
                        Text("Category Name")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Icon",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                FlowRow(
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {
                    availableIcons.forEach { option ->
                        val iconName = option.first
                        val label = option.second

                        FilterChip(
                            selected = icon == iconName,
                            onClick = {
                                icon = iconName
                            },
                            label = {
                                Row(
                                    verticalAlignment =
                                        Alignment.CenterVertically,
                                    horizontalArrangement =
                                        Arrangement.spacedBy(6.dp)
                                ) {
                                    CategoryIcon(
                                        iconName = iconName,
                                        contentDescription = label,
                                        modifier = Modifier.size(18.dp)
                                    )

                                    Text(label)
                                }
                            }
                        )
                    }
                }

                validationMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(
                    onClick = {
                        val cleanName = name.trim()

                        if (cleanName.isBlank()) {
                            validationMessage =
                                "Enter a category name."
                            return@Button
                        }

                        categoryViewModel.addCategory(
                            name = cleanName,
                            icon = icon
                        )

                        onSaved()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Category")
                }

                TextButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}