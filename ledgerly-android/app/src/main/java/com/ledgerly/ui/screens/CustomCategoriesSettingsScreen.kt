package com.ledgerly.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.ledgerly.data.models.Category
import com.ledgerly.ui.theme.LedgerlyExpenseRed
import com.ledgerly.viewmodel.CategoryViewModel

@Composable
fun CustomCategoriesSettingsScreen(
    categoryViewModel: CategoryViewModel,
    onBack: () -> Unit,
    onAddCategory: () -> Unit
) {
    val customCategories =
        categoryViewModel
            .allCategories
            .filter { category ->
                !category.isDefault
            }

    var categoryToDelete by remember {
        mutableStateOf<Category?>(null)
    }

    categoryToDelete?.let { category ->
        AlertDialog(
            onDismissRequest = {
                categoryToDelete = null
            },
            title = {
                Text(
                    text = "Delete Category?"
                )
            },
            text = {
                Text(
                    text =
                        "This will delete ${category.name}. Existing transactions will keep their saved category information."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        categoryViewModel
                            .deleteCategory(
                                category
                            )

                        categoryToDelete = null
                    }
                ) {
                    Text(
                        text = "Delete",
                        color =
                            LedgerlyExpenseRed
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        categoryToDelete = null
                    }
                ) {
                    Text(
                        text = "Cancel",
                        color =
                            LedgerlyExpenseRed
                    )
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier =
                Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector =
                        Icons.Filled.ArrowBack,
                    contentDescription =
                        "Back"
                )
            }

            Text(
                text = "Custom Categories",
                style =
                    MaterialTheme.typography.headlineMedium,
                fontWeight =
                    FontWeight.Bold
            )
        }

        Text(
            text =
                "Create and manage categories beyond Ledgerly's defaults.",
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        Button(
            onClick =
                onAddCategory,
            modifier =
                Modifier.align(
                    Alignment.CenterHorizontally
                )
        ) {
            Text(
                text = "Add Category"
            )
        }

        if (customCategories.isEmpty()) {
            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(18.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme
                                .colorScheme
                                .surfaceVariant
                    )
            ) {
                Column(
                    modifier =
                        Modifier.padding(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text =
                            "No custom categories yet.",
                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            "Add a category to organise transactions in your own way.",
                        style =
                            MaterialTheme.typography.bodySmall
                    )
                }
            }
        } else {
            LazyColumn(
                modifier =
                    Modifier.fillMaxSize(),
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items =
                        customCategories,
                    key = { category ->
                        category.id
                    }
                ) { category ->
                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            RoundedCornerShape(18.dp),
                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    MaterialTheme
                                        .colorScheme
                                        .surfaceVariant
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement =
                                Arrangement.spacedBy(12.dp),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {
                            Column(
                                modifier =
                                    Modifier.weight(1f),
                                verticalArrangement =
                                    Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text =
                                        category.name,
                                    fontWeight =
                                        FontWeight.Bold
                                )

                                Text(
                                    text =
                                        "Custom category",
                                    style =
                                        MaterialTheme.typography.bodySmall,
                                    color =
                                        MaterialTheme
                                            .colorScheme
                                            .onSurfaceVariant
                                )
                            }

                            TextButton(
                                onClick = {
                                    categoryToDelete =
                                        category
                                }
                            ) {
                                Text(
                                    text = "Delete",
                                    color =
                                        LedgerlyExpenseRed
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}