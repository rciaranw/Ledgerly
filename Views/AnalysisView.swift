import SwiftUI
import Chart

struct AnalysisView: View {

    @EnvironmentObject var budgetVM: BudgetViewModel
    @EnvironmentObject var settingsVM: SettingsViewModel

    var body: some View {
        ScrollView {
            VStack(spacing: 20) {
                Text("Income vs Expenses")
                    .font(.headline)

                // Pie chart using PieSlice component
                PieChartView(budgets: budgetVM.budgets,
                             currencyCode: settingsVM.settings.currencyCode)
                    .frame(height: 200)
                    .padding()

                // Category budgets bar chart
                VStack(alignment: .leading, spacing: 10) {
                    ForEach(budgetVM.budgets) { budget in
                        HStack {
                            Text(budget.category.name)
                                .frame(width: 100, alignment: .leading)

                            ProgressView(value: budget.progress)
                                .accentColor(budget.isOverBudget ? AppColors.expense : AppColors.accent)

                            Text(CurrencyFormatter.format(amount: budget.remaining,
                                                          currencyCode: settingsVM.settings.currencyCode))
                                .foregroundColor(budget.isOverBudget ? AppColors.expense : AppColors.income)
                                .frame(width: 60, alignment: .trailing)
                        }
                    }
                }
                .padding()
            }
        }
        .onAppear {
            let range = budgetVM.currentPeriodRange()
            budgetVM.updateBudgets(for: range.start, end: range.end)
        }
        .navigationTitle("Analysis")
    }
}


    // MARK: - Income vs Expense Pie Chart
    private var incomeExpenseChart: some View {
        VStack(alignment: .leading) {
            Text("Income vs Expense")
                .font(.headline)

            Chart {
                PieSlice(value: totalIncome, label: "Income", color: AppColors.income)
                PieSlice(value: totalExpense, label: "Expense", color: AppColors.expense)
            }
            .frame(height: 200)
        }
        .padding()
        .background(AppColors.cardBackground)
        .cornerRadius(12)
        .shadow(radius: 2)
    }

    // MARK: - Category Budget Chart
    private var categoryBudgetChart: some View {
        VStack(alignment: .leading) {
            Text("Budget Progress by Category")
                .font(.headline)

            ForEach(budgetVM.budgets) { budget in
                VStack(alignment: .leading) {
                    HStack {
                        Image(systemName: budget.category.systemIcon)
                            .foregroundColor(AppColors.accent)
                        Text(budget.category.name)
                        Spacer()
                        Text(CurrencyFormatter.format(amount: budget.remaining,
                                                      currencyCode: settingsVM.settings.currencyCode))
                            .foregroundColor(budget.isOverBudget ? AppColors.expense : AppColors.income)
                    }

                    ProgressView(value: budget.progress)
                        .accentColor(budget.isOverBudget ? AppColors.expense : AppColors.accent)
                }
                .padding(.vertical, 4)
            }
        }
        .padding()
        .background(AppColors.cardBackground)
        .cornerRadius(12)
        .shadow(radius: 2)
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
