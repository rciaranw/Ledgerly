import SwiftUI

struct HomeView: View {

    @EnvironmentObject var transactionVM: TransactionViewModel
    @EnvironmentObject var budgetVM: BudgetViewModel
    @EnvironmentObject var settingsVM: SettingsViewModel

    var body: some View {
        ScrollView {
            VStack(spacing: 20) {

                // MARK: - Budget Snapshot
                if !budgetVM.budgets.isEmpty {
                    BudgetSnapshotView(
                        budgets: budgetVM.budgets,
                        currencyCode: settingsVM.settings.currencyCode
                    )
                    .onAppear {
                        let range = budgetVM.currentPeriodRange()
                        budgetVM.updateBudgets(for: range.start, end: range.end)
                    }
                }

                // MARK: - Recent Transactions
                VStack(alignment: .leading) {
                    Text("Recent Transactions")
                        .font(.headline)
                        .padding(.horizontal)
                    
                    ForEach(transactionVM.expandedTransactions(from: Date().addingTimeInterval(-30*24*60*60), to: Date()).prefix(5)) { transaction in
                        TransactionRow(transaction: transaction,
                                       currencyCode: settingsVM.settings.currencyCode)
                            .padding(.horizontal)
                    }
                }
            }
        }
        .navigationTitle("Home")
    }
}


    // MARK: - Balance Card
    private var balanceCard: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text("Current Balance")
                .font(.headline)
            Text(CurrencyFormatter.format(
                amount: budgetVM.totalRemaining,
                currencyCode: settingsVM.settings.currencyCode
            ))
                .font(.largeTitle)
                .fontWeight(.bold)
                .foregroundColor(budgetVM.totalRemaining >= 0 ? AppColors.income : AppColors.expense)
        }
        .padding()
        .frame(maxWidth: .infinity)
        .background(AppColors.cardBackground)
        .cornerRadius(12)
        .shadow(radius: 2)
    }

    // MARK: - Budget Snapshot
    private var budgetSnapshot: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text("Budget Snapshot")
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

    // MARK: - Recent Transactions Section
    private var recentTransactionsSection: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text("Recent Transactions")
                .font(.headline)

            ForEach(recentTransactions) { tx in
                HStack {
                    Image(systemName: tx.category.systemIcon)
                        .foregroundColor(AppColors.accent)
                        .frame(width: 30)
                    VStack(alignment: .leading) {
                        Text(tx.title)
                            .fontWeight(.medium)
                        Text(DateHelper.formatDate(tx.date))
                            .font(.caption)
                            .foregroundColor(AppColors.secondaryText)
                    }
                    Spacer()
                    Text(CurrencyFormatter.format(amount: tx.amount,
                                                  currencyCode: settingsVM.settings.currencyCode))
                        .foregroundColor(tx.isIncome ? AppColors.income : AppColors.expense)
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

struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        HomeView()
            .environmentObject(TransactionViewModel())
            .environmentObject(BudgetViewModel())
            .environmentObject(SettingsViewModel())
    }
}
