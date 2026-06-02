import SwiftUI

struct AnalysisView: View {

    @EnvironmentObject var transactionVM: TransactionViewModel
    @EnvironmentObject var budgetVM: BudgetViewModel
    @EnvironmentObject var settingsVM: SettingsViewModel

    private var currentPeriodStart: Date {
        DateHelper.startOfMonth(
            for: Date(),
            monthStartDay: settingsVM.settings.monthStartDay
        )
    }

    private var currentPeriodEnd: Date {
        DateHelper.endOfMonth(
            for: Date(),
            monthStartDay: settingsVM.settings.monthStartDay
        )
    }

    private var currentPeriodTransactions: [Transaction] {
        transactionVM.expandedTransactions(
            from: currentPeriodStart,
            to: currentPeriodEnd
        )
    }

    private var totalIncome: Double {
        currentPeriodTransactions
            .filter { $0.isIncome }
            .reduce(0) { $0 + $1.amount }
    }

    private var totalExpenses: Double {
        currentPeriodTransactions
            .filter { !$0.isIncome }
            .reduce(0) { $0 + $1.amount }
    }

    private var netAmount: Double {
        totalIncome - totalExpenses
    }

    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: 20) {

                    incomeExpenseSummary

                    PieChartView(
                        budgets: budgetVM.budgets,
                        currencyCode: settingsVM.settings.currencyCode
                    )
                    .frame(height: 220)

                    categoryBudgetSection
                }
                .padding()
            }
            .background(AppColors.background)
            .navigationTitle("Analysis")
            .onAppear {
                refreshBudgets()
            }
        }
    }

    private var incomeExpenseSummary: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Income vs Expenses")
                .font(.headline)
                .foregroundColor(AppColors.primaryText)

            HStack {
                VStack(alignment: .leading) {
                    Text("Income")
                        .font(.caption)
                        .foregroundColor(AppColors.secondaryText)

                    Text(
                        CurrencyFormatter.format(
                            amount: totalIncome,
                            currencyCode: settingsVM.settings.currencyCode
                        )
                    )
                    .foregroundColor(AppColors.income)
                    .fontWeight(.semibold)
                }

                Spacer()

                VStack(alignment: .trailing) {
                    Text("Expenses")
                        .font(.caption)
                        .foregroundColor(AppColors.secondaryText)

                    Text(
                        CurrencyFormatter.format(
                            amount: totalExpenses,
                            currencyCode: settingsVM.settings.currencyCode
                        )
                    )
                    .foregroundColor(AppColors.expense)
                    .fontWeight(.semibold)
                }
            }

            Divider()

            HStack {
                Text("Net")
                    .fontWeight(.medium)

                Spacer()

                Text(
                    CurrencyFormatter.format(
                        amount: netAmount,
                        currencyCode: settingsVM.settings.currencyCode
                    )
                )
                .fontWeight(.bold)
                .foregroundColor(netAmount >= 0 ? AppColors.income : AppColors.expense)
            }
        }
        .padding()
        .background(AppColors.cardBackground)
        .cornerRadius(12)
        .shadow(radius: 2)
    }

    private var categoryBudgetSection: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Budget Progress")
                .font(.headline)
                .foregroundColor(AppColors.primaryText)

            if budgetVM.budgets.isEmpty {
                Text("No budgets set yet")
                    .font(.caption)
                    .foregroundColor(AppColors.secondaryText)
            } else {
                ForEach(budgetVM.budgets) { budget in
                    VStack(alignment: .leading, spacing: 6) {
                        HStack {
                            Image(systemName: budget.category.systemIcon)
                                .foregroundColor(AppColors.accent)

                            Text(budget.category.name)
                                .foregroundColor(AppColors.primaryText)

                            Spacer()

                            Text(
                                CurrencyFormatter.format(
                                    amount: budget.remaining,
                                    currencyCode: settingsVM.settings.currencyCode
                                )
                            )
                            .foregroundColor(budget.isOverBudget ? AppColors.expense : AppColors.income)
                        }

                        ProgressView(value: min(budget.progress, 1.0))
                            .accentColor(budget.isOverBudget ? AppColors.expense : AppColors.accent)
                    }
                    .padding(.vertical, 4)
                }
            }
        }
        .padding()
        .background(AppColors.cardBackground)
        .cornerRadius(12)
        .shadow(radius: 2)
    }

    private func refreshBudgets() {
        budgetVM.updateBudgets(
            transactions: currentPeriodTransactions,
            startDate: currentPeriodStart,
            endDate: currentPeriodEnd
        )
    }
}

struct AnalysisView_Previews: PreviewProvider {
    static var previews: some View {
        AnalysisView()
            .environmentObject(TransactionViewModel())
            .environmentObject(BudgetViewModel())
            .environmentObject(SettingsViewModel())
    }
}