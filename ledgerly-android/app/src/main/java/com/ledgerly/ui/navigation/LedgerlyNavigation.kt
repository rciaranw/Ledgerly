package com.ledgerly.ui.navigation

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ledgerly.ui.screens.AddBudgetScreen
import com.ledgerly.ui.screens.AddCategoryScreen
import com.ledgerly.ui.screens.AddRecurringTransactionScreen
import com.ledgerly.ui.screens.AddTransactionScreen
import com.ledgerly.ui.screens.AnalysisScreen
import com.ledgerly.ui.screens.EditBudgetScreen
import com.ledgerly.ui.screens.EditRecurringTransactionScreen
import com.ledgerly.ui.screens.EditTransactionScreen
import com.ledgerly.ui.screens.ExportScreen
import com.ledgerly.ui.screens.HomeScreen
import com.ledgerly.ui.screens.RecurringTransactionsScreen
import com.ledgerly.ui.screens.SettingsScreen
import com.ledgerly.ui.screens.TransactionsScreen
import com.ledgerly.viewmodel.BudgetViewModel
import com.ledgerly.viewmodel.CategoryViewModel
import com.ledgerly.viewmodel.RecurringTransactionViewModel
import com.ledgerly.viewmodel.SettingsViewModel
import com.ledgerly.viewmodel.TransactionViewModel

sealed class LedgerlyTab(
    val route: String,
    val label: String,
    val icon: ImageVector? = null
) {
    data object Home :
        LedgerlyTab(
            "home",
            "Home",
            Icons.Filled.Home
        )

    data object Transactions :
        LedgerlyTab(
            "transactions",
            "Transactions",
            Icons.Filled.ReceiptLong
        )

    data object Analysis :
        LedgerlyTab(
            "analysis",
            "Analysis",
            Icons.Filled.Analytics
        )

    data object Settings :
        LedgerlyTab(
            "settings",
            "Settings",
            Icons.Filled.Settings
        )

    data object AddTransaction :
        LedgerlyTab("add_transaction", "Add Transaction")

    data object AddBudget :
        LedgerlyTab("add_budget", "Add Budget")

    data object AddCategory :
        LedgerlyTab("add_category", "Add Category")

    data object Export :
        LedgerlyTab("export", "Export")

    data object AddRecurringTransaction :
        LedgerlyTab("add_recurring_transaction", "Add Recurring")

    data object RecurringTransactions :
        LedgerlyTab(
            "recurring_transactions",
            "Recurring Transactions"
        )

    data object EditRecurringTransaction :
        LedgerlyTab(
            "edit_recurring_transaction/{recurringId}",
            "Edit Recurring"
        ) {
        fun createRoute(
            recurringId: String
        ): String {
            return "edit_recurring_transaction/$recurringId"
        }
    }

    data object EditTransaction :
        LedgerlyTab(
            "edit_transaction/{transactionId}",
            "Edit Transaction"
        ) {
        fun createRoute(
            transactionId: String
        ): String {
            return "edit_transaction/$transactionId"
        }
    }

    data object EditBudget :
        LedgerlyTab(
            "edit_budget/{budgetId}",
            "Edit Budget"
        ) {
        fun createRoute(
            budgetId: String
        ): String {
            return "edit_budget/$budgetId"
        }
    }
}

@Composable
fun LedgerlyNavigation() {
    val navController = rememberNavController()

    val application =
        LocalContext.current.applicationContext as Application

    val transactionViewModel: TransactionViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory
            .getInstance(application)
    )

    val budgetViewModel: BudgetViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory
            .getInstance(application)
    )

    val settingsViewModel: SettingsViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory
            .getInstance(application)
    )

    val categoryViewModel: CategoryViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory
            .getInstance(application)
    )

    val recurringTransactionViewModel:
        RecurringTransactionViewModel =
        viewModel(
            factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(application)
        )

    var recurringProcessed by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(
        recurringTransactionViewModel.recurringTransactions.size
    ) {
        if (!recurringProcessed) {
            val generatedTransactions =
                recurringTransactionViewModel
                    .generateDueTransactions()

            if (generatedTransactions.isNotEmpty()) {
                transactionViewModel.addTransactions(
                    generatedTransactions
                )

                budgetViewModel.refreshCurrentMonthBudgets(
                    transactionViewModel.transactions
                )
            }

            recurringProcessed = true
        }
    }

    val tabs = remember {
        listOf(
            LedgerlyTab.Home,
            LedgerlyTab.Transactions,
            LedgerlyTab.Analysis,
            LedgerlyTab.Settings
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by
                    navController.currentBackStackEntryAsState()

                val currentDestination =
                    navBackStackEntry?.destination

                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentDestination
                            ?.hierarchy
                            ?.any {
                                it.route == tab.route
                            } == true,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(
                                    navController.graph.startDestinationId
                                ) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            tab.icon?.let { icon ->
                                Icon(
                                    imageVector = icon,
                                    contentDescription = tab.label
                                )
                            }
                        },
                        label = {
                            Text(tab.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = LedgerlyTab.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(LedgerlyTab.Home.route) {
                HomeScreen(
                    transactionViewModel = transactionViewModel,
                    budgetViewModel = budgetViewModel,
                    settingsViewModel = settingsViewModel,
                    recurringTransactionViewModel =
                        recurringTransactionViewModel
                )
            }

            composable(LedgerlyTab.Transactions.route) {
                TransactionsScreen(
                    transactionViewModel = transactionViewModel,
                    settingsViewModel = settingsViewModel,
                    categoryViewModel = categoryViewModel,
                    onAddTransaction = {
                        navController.navigate(
                            LedgerlyTab.AddTransaction.route
                        )
                    },
                    onAddRecurringTransaction = {
                        navController.navigate(
                            LedgerlyTab.AddRecurringTransaction.route
                        )
                    },
                    onEditTransaction = { transactionId ->
                        navController.navigate(
                            LedgerlyTab.EditTransaction
                                .createRoute(transactionId)
                        )
                    }
                )
            }

            composable(LedgerlyTab.Analysis.route) {
                AnalysisScreen(
                    transactionViewModel = transactionViewModel,
                    budgetViewModel = budgetViewModel,
                    settingsViewModel = settingsViewModel,
                    onAddBudget = {
                        navController.navigate(
                            LedgerlyTab.AddBudget.route
                        )
                    },
                    onEditBudget = { budgetId ->
                        navController.navigate(
                            LedgerlyTab.EditBudget
                                .createRoute(budgetId)
                        )
                    }
                )
            }

            composable(LedgerlyTab.Settings.route) {
                SettingsScreen(
                    budgetViewModel = budgetViewModel,
                    settingsViewModel = settingsViewModel,
                    categoryViewModel = categoryViewModel,
                    onAddCategory = {
                        navController.navigate(
                            LedgerlyTab.AddCategory.route
                        )
                    },
                    onViewRecurringTransactions = {
                        navController.navigate(
                            LedgerlyTab.RecurringTransactions.route
                        )
                    },
                    onExport = {
                        navController.navigate(
                            LedgerlyTab.Export.route
                        )
                    }
                )
            }

            composable(LedgerlyTab.Export.route) {
                ExportScreen(
                    transactionViewModel = transactionViewModel
                )
            }

            composable(LedgerlyTab.AddTransaction.route) {
                AddTransactionScreen(
                    transactionViewModel = transactionViewModel,
                    budgetViewModel = budgetViewModel,
                    categoryViewModel = categoryViewModel,
                    onSaved = {
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }

            composable(LedgerlyTab.AddBudget.route) {
                AddBudgetScreen(
                    budgetViewModel = budgetViewModel,
                    categoryViewModel = categoryViewModel,
                    onSaved = {
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }

            composable(LedgerlyTab.AddCategory.route) {
                AddCategoryScreen(
                    categoryViewModel = categoryViewModel,
                    onSaved = {
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }

            composable(LedgerlyTab.AddRecurringTransaction.route) {
                AddRecurringTransactionScreen(
                    recurringTransactionViewModel =
                        recurringTransactionViewModel,
                    categoryViewModel = categoryViewModel,
                    onSaved = {
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }

            composable(LedgerlyTab.RecurringTransactions.route) {
                RecurringTransactionsScreen(
                    recurringTransactionViewModel =
                        recurringTransactionViewModel,
                    onAddRecurring = {
                        navController.navigate(
                            LedgerlyTab.AddRecurringTransaction.route
                        )
                    },
                    onEditRecurring = { recurringId ->
                        navController.navigate(
                            LedgerlyTab.EditRecurringTransaction
                                .createRoute(recurringId)
                        )
                    }
                )
            }

            composable(
                route = LedgerlyTab.EditRecurringTransaction.route,
                arguments = listOf(
                    navArgument("recurringId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val recurringId =
                    backStackEntry.arguments
                        ?.getString("recurringId")
                        ?: ""

                EditRecurringTransactionScreen(
                    recurringId = recurringId,
                    recurringTransactionViewModel =
                        recurringTransactionViewModel,
                    categoryViewModel = categoryViewModel,
                    onSaved = {
                        navController.popBackStack()
                    },
                    onDeleted = {
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = LedgerlyTab.EditTransaction.route,
                arguments = listOf(
                    navArgument("transactionId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val transactionId =
                    backStackEntry.arguments
                        ?.getString("transactionId")
                        ?: ""

                EditTransactionScreen(
                    transactionId = transactionId,
                    transactionViewModel = transactionViewModel,
                    budgetViewModel = budgetViewModel,
                    categoryViewModel = categoryViewModel,
                    onSaved = {
                        navController.popBackStack()
                    },
                    onDeleted = {
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = LedgerlyTab.EditBudget.route,
                arguments = listOf(
                    navArgument("budgetId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val budgetId =
                    backStackEntry.arguments
                        ?.getString("budgetId")
                        ?: ""

                EditBudgetScreen(
                    budgetId = budgetId,
                    budgetViewModel = budgetViewModel,
                    onSaved = {
                        navController.popBackStack()
                    },
                    onDeleted = {
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}