# Ledgerly

Ledgerly is a modern personal finance and budgeting app built with SwiftUI.

Designed to be simple, fast, and intuitive, Ledgerly helps users track income and expenses, manage budgets, monitor spending habits, and gain meaningful financial insights through powerful analysis tools.

## Features

### Transactions

* Add income and expense transactions
* Custom transaction titles and notes
* Category-based organisation
* Date tracking
* Recurring transaction support
* Full transaction history

### Budgeting

* Create budgets by category
* Track spending against limits
* Budget progress indicators
* Remaining budget calculations
* Budget carryover support
* Budget snapshot dashboard

### Analysis

* Spending breakdown by category
* Budget utilisation tracking
* Income vs expense summaries
* Visual spending charts
* Financial trend monitoring

### Settings

* Multiple currency support

  * GBP (£)
  * USD ($)
  * EUR (€)
* Custom week start day
* Custom payroll month support
* Budget carryover settings
* Theme settings

### Exporting

* Export transactions to CSV
* Share exported data directly from the app
* Compatible with Excel, Google Sheets, and other spreadsheet software

## Default Categories

Ledgerly includes a comprehensive set of built-in categories:

* Cash
* Cashback
* Charity
* Child Allowance
* Credit
* Entertainment
* Gambling
* General
* Gift
* Health
* Insurance
* Interest
* Investment
* Loan
* Net Sales
* Refund
* Remittances
* Restaurants
* Salary
* Savings
* Shopping
* Top Ups
* Transfers
* Travel
* Utilities

## Technology Stack

* Swift
* SwiftUI
* MVVM Architecture
* Codable
* CSV Exporting
* SF Symbols

## Project Structure

```text
Ledgerly
│
├── Assets.xcassets
│   └── AppIcon.appiconset
│
├── Helpers
│   ├── CSVExporter.swift
│   ├── CurrencyFormatter.swift
│   ├── DateHelper.swift
│   └── DefaultCategories.swift
│
├── Models
│   ├── Budget.swift
│   ├── Category.swift
│   ├── RecurringRule.swift
│   ├── SettingsModel.swift
│   └── Transaction.swift
│
├── Theme
│   └── AppColors.swift
│
├── ViewModels
│   ├── BudgetViewModel.swift
│   ├── SettingsViewModel.swift
│   └── TransactionViewModel.swift
│
├── Views
│   ├── AddTransactionView.swift
│   ├── AnalysisView.swift
│   ├── ExportView.swift
│   ├── HomeView.swift
│   ├── RootTabView.swift
│   ├── SettingsView.swift
│   ├── TransactionsView.swift
│   │
│   └── Components
│       ├── BudgetSnapshotView.swift
│       ├── CategoryIconView.swift
│       ├── PieChartView.swift
│       ├── PieSlice.swift
│       └── TransactionRow.swift
│
└── LedgerlyApp.swift
```

## Roadmap

### In Progress

* Persistent data storage
* User-created categories
* Enhanced budgeting tools
* Budget alerts
* Improved recurring transaction engine

### Planned

* Face ID protection
* Advanced analytics
* Custom themes
* Financial goals tracking
* Cloud backup and syncing
* Additional export formats

## License

This project is currently provided for personal and educational use.

---

Created by Ciaran Robertson.
