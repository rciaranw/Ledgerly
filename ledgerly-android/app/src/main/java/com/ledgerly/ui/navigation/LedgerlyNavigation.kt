package com.ledgerly.ui.navigation

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
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
import com.ledgerly.ui.screens.AppInfoSettingsScreen
import com.ledgerly.ui.screens.BudgetScreen
import com.ledgerly.ui.screens.CurrencySettingsScreen
import com.ledgerly.ui.screens.CustomCategoriesSettingsScreen
import com.ledgerly.ui.screens.DataSettingsScreen
import com.ledgerly.ui.screens.DateFormatSettingsScreen
import com.ledgerly.ui.screens.DisplaySettingsScreen
import com.ledgerly.ui.screens.EditBudgetScreen
import com.ledgerly.ui.screens.EditRecurringTransactionScreen
import com.ledgerly.ui.screens.EditTransactionScreen
import com.ledgerly.ui.screens.ExportScreen
import com.ledgerly.ui.screens.HomeScreen
import com.ledgerly.ui.screens.LanguageSettingsScreen
import com.ledgerly.ui.screens.MonthStartDateSettingsScreen
import com.ledgerly.ui.screens.PreferencesSettingsScreen
import com.ledgerly.ui.screens.RecurringTransactionsScreen
import com.ledgerly.ui.screens.SettingsScreen
import com.ledgerly.ui.screens.SupportSettingsScreen
import com.ledgerly.ui.screens.TextSizeSettingsScreen
import com.ledgerly.ui.screens.ThemeSettingsScreen
import com.ledgerly.ui.screens.TransactionsScreen
import com.ledgerly.ui.screens.WeekStartDaySettingsScreen
import com.ledgerly.viewmodel.BudgetViewModel
import com.ledgerly.viewmodel.CategoryViewModel
import com.ledgerly.viewmodel.PeriodViewModel
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
            route = "home",
            label = "Home",
            icon = Icons.Filled.Home
        )

    data object Transactions :
        LedgerlyTab(
            route = "transactions",
            label = "Transactions",
            icon = Icons.Filled.ReceiptLong
        )

    data object Analysis :
        LedgerlyTab(
            route = "analysis",
            label = "Analysis",
            icon = Icons.Filled.Analytics
        )

    data object Budget :
        LedgerlyTab(
            route = "budget",
            label = "Budget",
            icon = Icons.Filled.AccountBalanceWallet
        )

    data object Settings :
        LedgerlyTab(
            route = "settings",
            label = "Settings",
            icon = Icons.Filled.Settings
        )

    data object PreferencesSettings :
        LedgerlyTab(
            route = "settings/preferences",
            label = "Preferences"
        )

    data object DisplaySettings :
        LedgerlyTab(
            route = "settings/display",
            label = "Display"
        )

    data object DataSettings :
        LedgerlyTab(
            route = "settings/data",
            label = "Data"
        )

    data object AppInfoSettings :
        LedgerlyTab(
            route = "settings/app_info",
            label = "App Info"
        )

    data object CurrencySettings :
        LedgerlyTab(
            route = "settings/preferences/currency",
            label = "Currency"
        )

    data object DateFormatSettings :
        LedgerlyTab(
            route = "settings/preferences/date_format",
            label = "Date Format"
        )

    data object WeekStartSettings :
        LedgerlyTab(
            route = "settings/preferences/week_start",
            label = "Week Start"
        )

    data object MonthStartSettings :
        LedgerlyTab(
            route = "settings/preferences/month_start",
            label = "Month Start"
        )

    data object ThemeSettings :
        LedgerlyTab(
            route = "settings/display/theme",
            label = "Theme"
        )

    data object LanguageSettings :
        LedgerlyTab(
            route = "settings/display/language",
            label = "Language"
        )

    data object TextSizeSettings :
        LedgerlyTab(
            route = "settings/display/text_size",
            label = "Text Size"
        )

    data object CustomCategoriesSettings :
        LedgerlyTab(
            route = "settings/data/custom_categories",
            label = "Custom Categories"
        )

    data object SupportSettings :
        LedgerlyTab(
            route = "settings/app_info/support",
            label = "Support"
        )

    data object AddTransaction :
        LedgerlyTab(
            route = "add_transaction",
            label = "Add Transaction"
        )

    data object AddBudget :
        LedgerlyTab(
            route = "add_budget",
            label = "Add Budget"
        )

    data object AddCategory :
        LedgerlyTab(
            route = "add_category",
            label = "Add Category"
        )

    data object Export :
        LedgerlyTab(
            route = "export",
            label = "Export"
        )

    data object AddRecurringTransaction :
        LedgerlyTab(
            route = "add_recurring_transaction",
            label = "Add Recurring"
        )

    data object RecurringTransactions :
        LedgerlyTab(
            route = "recurring_transactions",
            label = "Recurring Transactions"
        )

    data object EditRecurringTransaction :
        LedgerlyTab(
            route = "edit_recurring_transaction/{recurringId}",
            label = "Edit Recurring"
        ) {
        fun createRoute(
            recurringId: String
        ): String {
            return "edit_recurring_transaction/$recurringId"
        }
    }

    data object EditTransaction :
        LedgerlyTab(
            route = "edit_transaction/{transactionId}",
            label = "Edit Transaction"
        ) {
        fun createRoute(
            transactionId: String
        ): String {
            return "edit_transaction/$transactionId"
        }
    }

    data object EditBudget :
        LedgerlyTab(
            route = "edit_budget/{budgetId}",
            label = "Edit Budget"
        ) {
        fun createRoute(
            budgetId: String
        ): String {
            return "edit_budget/$budgetId"
        }
    }
}

@Composable
fun LedgerlyNavigation(
    settingsViewModel: SettingsViewModel
) {
    val navController =
        rememberNavController()

    val application =
        LocalContext.current.applicationContext as Application

    val transactionViewModel:
        TransactionViewModel =
        viewModel(
            factory =
                ViewModelProvider
                    .AndroidViewModelFactory
                    .getInstance(application)
        )

    val budgetViewModel:
        BudgetViewModel =
        viewModel(
            factory =
                ViewModelProvider
                    .AndroidViewModelFactory
                    .getInstance(application)
        )

    val categoryViewModel:
        CategoryViewModel =
        viewModel(
            factory =
                ViewModelProvider
                    .AndroidViewModelFactory
                    .getInstance(application)
        )

    val recurringTransactionViewModel:
        RecurringTransactionViewModel =
        viewModel(
            factory =
                ViewModelProvider
                    .AndroidViewModelFactory
                    .getInstance(application)
        )

    val periodViewModel:
        PeriodViewModel =
        viewModel()

    var recurringProcessed by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(
        recurringTransactionViewModel
            .recurringTransactions
            .size
    ) {
        if (!recurringProcessed) {
            val generatedTransactions =
                recurringTransactionViewModel
                    .generateDueTransactions()

            if (generatedTransactions.isNotEmpty()) {
                transactionViewModel.addTransactions(
                    generatedTransactions
                )
            }

            recurringProcessed = true
        }
    }

    val tabs =
        remember {
            listOf(
                LedgerlyTab.Home,
                LedgerlyTab.Transactions,
                LedgerlyTab.Analysis,
                LedgerlyTab.Budget,
                LedgerlyTab.Settings
            )
        }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by
                    navController
                        .currentBackStackEntryAsState()

                val currentDestination =
                    navBackStackEntry?.destination

                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected =
                            currentDestination
                                ?.hierarchy
                                ?.any {
                                    it.route ==
                                        tab.route
                                } == true,
                        onClick = {
                            navController.navigate(
                                tab.route
                            ) {
                                popUpTo(
                                    navController
                                        .graph
                                        .startDestinationId
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
                                    contentDescription =
                                        tab.label
                                )
                            }
                        },
                        label = {
                            Text(
                                text = tab.label
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination =
                LedgerlyTab.Home.route,
            modifier =
                Modifier.padding(innerPadding)
        ) {
            composable(
                LedgerlyTab.Home.route
            ) {
                HomeScreen(
                    transactionViewModel =
                        transactionViewModel,
                    budgetViewModel =
                        budgetViewModel,
                    settingsViewModel =
                        settingsViewModel,
                    recurringTransactionViewModel =
                        recurringTransactionViewModel,
                    periodViewModel =
                        periodViewModel
                )
            }

            composable(
                LedgerlyTab.Transactions.route
            ) {
                TransactionsScreen(
                    transactionViewModel =
                        transactionViewModel,
                    settingsViewModel =
                        settingsViewModel,
                    categoryViewModel =
                        categoryViewModel,
                    onAddTransaction = {
                        navController.navigate(
                            LedgerlyTab
                                .AddTransaction
                                .route
                        )
                    },
                    onAddRecurringTransaction = {
                        navController.navigate(
                            LedgerlyTab
                                .AddRecurringTransaction
                                .route
                        )
                    },
                    onEditTransaction = { transactionId ->
                        navController.navigate(
                            LedgerlyTab
                                .EditTransaction
                                .createRoute(
                                    transactionId
                                )
                        )
                    }
                )
            }

            composable(
                LedgerlyTab.Analysis.route
            ) {
                AnalysisScreen(
                    transactionViewModel =
                        transactionViewModel,
                    settingsViewModel =
                        settingsViewModel,
                    periodViewModel =
                        periodViewModel
                )
            }

            composable(
                LedgerlyTab.Budget.route
            ) {
                BudgetScreen(
                    transactionViewModel =
                        transactionViewModel,
                    budgetViewModel =
                        budgetViewModel,
                    settingsViewModel =
                        settingsViewModel,
                    periodViewModel =
                        periodViewModel,
                    onAddBudget = {
                        navController.navigate(
                            LedgerlyTab
                                .AddBudget
                                .route
                        )
                    },
                    onEditBudget = { budgetId ->
                        navController.navigate(
                            LedgerlyTab
                                .EditBudget
                                .createRoute(
                                    budgetId
                                )
                        )
                    }
                )
            }

            composable(
                LedgerlyTab.Settings.route
            ) {
                SettingsScreen(
                    budgetViewModel =
                        budgetViewModel,
                    settingsViewModel =
                        settingsViewModel,
                    categoryViewModel =
                        categoryViewModel,
                    onOpenPreferences = {
                        navController.navigate(
                            LedgerlyTab
                                .PreferencesSettings
                                .route
                        )
                    },
                    onOpenDisplay = {
                        navController.navigate(
                            LedgerlyTab
                                .DisplaySettings
                                .route
                        )
                    },
                    onOpenData = {
                        navController.navigate(
                            LedgerlyTab
                                .DataSettings
                                .route
                        )
                    },
                    onOpenAppInfo = {
                        navController.navigate(
                            LedgerlyTab
                                .AppInfoSettings
                                .route
                        )
                    }
                )
            }

            composable(
                LedgerlyTab.PreferencesSettings.route
            ) {
                PreferencesSettingsScreen(
                    settingsViewModel =
                        settingsViewModel,
                    onBack = {
                        navController.popBackStack()
                    },
                    onOpenCurrency = {
                        navController.navigate(
                            LedgerlyTab
                                .CurrencySettings
                                .route
                        )
                    },
                    onOpenDateFormat = {
                        navController.navigate(
                            LedgerlyTab
                                .DateFormatSettings
                                .route
                        )
                    },
                    onOpenWeekStartDay = {
                        navController.navigate(
                            LedgerlyTab
                                .WeekStartSettings
                                .route
                        )
                    },
                    onOpenMonthStartDate = {
                        navController.navigate(
                            LedgerlyTab
                                .MonthStartSettings
                                .route
                        )
                    }
                )
            }

            composable(
                LedgerlyTab.DisplaySettings.route
            ) {
                DisplaySettingsScreen(
                    settingsViewModel =
                        settingsViewModel,
                    onBack = {
                        navController.popBackStack()
                    },
                    onOpenTheme = {
                        navController.navigate(
                            LedgerlyTab
                                .ThemeSettings
                                .route
                        )
                    },
                    onOpenLanguage = {
                        navController.navigate(
                            LedgerlyTab
                                .LanguageSettings
                                .route
                        )
                    },
                    onOpenTextSize = {
                        navController.navigate(
                            LedgerlyTab
                                .TextSizeSettings
                                .route
                        )
                    }
                )
            }

            composable(
                LedgerlyTab.DataSettings.route
            ) {
                DataSettingsScreen(
                    settingsViewModel =
                        settingsViewModel,
                    onBack = {
                        navController.popBackStack()
                    },
                    onOpenCustomCategories = {
                        navController.navigate(
                            LedgerlyTab
                                .CustomCategoriesSettings
                                .route
                        )
                    },
                    onOpenRecurringTransactions = {
                        navController.navigate(
                            LedgerlyTab
                                .RecurringTransactions
                                .route
                        )
                    },
                    onOpenExportData = {
                        navController.navigate(
                            LedgerlyTab.Export.route
                        )
                    }
                )
            }

            composable(
                LedgerlyTab.AppInfoSettings.route
            ) {
                AppInfoSettingsScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onOpenSupport = {
                        navController.navigate(
                            LedgerlyTab
                                .SupportSettings
                                .route
                        )
                    }
                )
            }

            composable(
                LedgerlyTab.CurrencySettings.route
            ) {
                CurrencySettingsScreen(
                    settingsViewModel =
                        settingsViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                LedgerlyTab.DateFormatSettings.route
            ) {
                DateFormatSettingsScreen(
                    settingsViewModel =
                        settingsViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                LedgerlyTab.WeekStartSettings.route
            ) {
                WeekStartDaySettingsScreen(
                    settingsViewModel =
                        settingsViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                LedgerlyTab.MonthStartSettings.route
            ) {
                MonthStartDateSettingsScreen(
                    settingsViewModel =
                        settingsViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                LedgerlyTab.ThemeSettings.route
            ) {
                ThemeSettingsScreen(
                    settingsViewModel =
                        settingsViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                LedgerlyTab.LanguageSettings.route
            ) {
                LanguageSettingsScreen(
                    settingsViewModel =
                        settingsViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                LedgerlyTab.TextSizeSettings.route
            ) {
                TextSizeSettingsScreen(
                    settingsViewModel =
                        settingsViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                LedgerlyTab.CustomCategoriesSettings.route
            ) {
                CustomCategoriesSettingsScreen(
                    categoryViewModel =
                        categoryViewModel,
                    onBack = {
                        navController.popBackStack()
                    },
                    onAddCategory = {
                        navController.navigate(
                            LedgerlyTab
                                .AddCategory
                                .route
                        )
                    }
                )
            }

            composable(
                LedgerlyTab.SupportSettings.route
            ) {
                SupportSettingsScreen(
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                LedgerlyTab.Export.route
            ) {
                ExportScreen(
                    transactionViewModel =
                        transactionViewModel
                )
            }

            composable(
                LedgerlyTab.AddTransaction.route
            ) {
                AddTransactionScreen(
                    transactionViewModel =
                        transactionViewModel,
                    budgetViewModel =
                        budgetViewModel,
                    categoryViewModel =
                        categoryViewModel,
                    onSaved = {
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                LedgerlyTab.AddBudget.route
            ) {
                AddBudgetScreen(
                    budgetViewModel =
                        budgetViewModel,
                    categoryViewModel =
                        categoryViewModel,
                    onSaved = {
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                LedgerlyTab.AddCategory.route
            ) {
                AddCategoryScreen(
                    categoryViewModel =
                        categoryViewModel,
                    onSaved = {
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                LedgerlyTab.AddRecurringTransaction.route
            ) {
                AddRecurringTransactionScreen(
                    recurringTransactionViewModel =
                        recurringTransactionViewModel,
                    categoryViewModel =
                        categoryViewModel,
                    onSaved = {
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                LedgerlyTab.RecurringTransactions.route
            ) {
                RecurringTransactionsScreen(
                    recurringTransactionViewModel =
                        recurringTransactionViewModel,
                    onAddRecurring = {
                        navController.navigate(
                            LedgerlyTab
                                .AddRecurringTransaction
                                .route
                        )
                    },
                    onEditRecurring = { recurringId ->
                        navController.navigate(
                            LedgerlyTab
                                .EditRecurringTransaction
                                .createRoute(
                                    recurringId
                                )
                        )
                    }
                )
            }

            composable(
                route =
                    LedgerlyTab
                        .EditRecurringTransaction
                        .route,
                arguments =
                    listOf(
                        navArgument(
                            "recurringId"
                        ) {
                            type =
                                NavType.StringType
                        }
                    )
            ) { backStackEntry ->
                val recurringId =
                    backStackEntry
                        .arguments
                        ?.getString(
                            "recurringId"
                        )
                        ?: ""

                EditRecurringTransactionScreen(
                    recurringId =
                        recurringId,
                    recurringTransactionViewModel =
                        recurringTransactionViewModel,
                    categoryViewModel =
                        categoryViewModel,
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
                route =
                    LedgerlyTab
                        .EditTransaction
                        .route,
                arguments =
                    listOf(
                        navArgument(
                            "transactionId"
                        ) {
                            type =
                                NavType.StringType
                        }
                    )
            ) { backStackEntry ->
                val transactionId =
                    backStackEntry
                        .arguments
                        ?.getString(
                            "transactionId"
                        )
                        ?: ""

                EditTransactionScreen(
                    transactionId =
                        transactionId,
                    transactionViewModel =
                        transactionViewModel,
                    budgetViewModel =
                        budgetViewModel,
                    categoryViewModel =
                        categoryViewModel,
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
                route =
                    LedgerlyTab
                        .EditBudget
                        .route,
                arguments =
                    listOf(
                        navArgument(
                            "budgetId"
                        ) {
                            type =
                                NavType.StringType
                        }
                    )
            ) { backStackEntry ->
                val budgetId =
                    backStackEntry
                        .arguments
                        ?.getString(
                            "budgetId"
                        )
                        ?: ""

                EditBudgetScreen(
                    budgetId =
                        budgetId,
                    budgetViewModel =
                        budgetViewModel,
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