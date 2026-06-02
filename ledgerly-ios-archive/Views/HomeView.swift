import SwiftUI

struct HomeView: View {

    @EnvironmentObject var transactionVM: TransactionViewModel
    @EnvironmentObject var budgetVM: BudgetViewModel
    @EnvironmentObject var settingsVM: SettingsViewModel

    private var recentTransactions: [Transaction] {
        Array(
            transactionVM.transactions
                .sorted { $0.date > $1.date }
                .prefix(5)
        )
    }

    private var currentBalance: Double {
        transactionVM.transactions.reduce(0) { total, transaction in
            transaction.isIncome
            ? total + transaction.amount
            : total - transaction.amount
        }
    }

    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: 20) {

                    balanceCard

                    BudgetSnapshotView(
                        budgets: budgetVM.budgets,
                        currencyCode: settingsVM.settings.currencyCode
                    )

                    recentTransactionsSection
                }
                .padding()
            }
            .background(AppColors.background)
            .navigationTitle("Home")
            .onAppear {
                refreshBudgets()
            }
        }
    }

    private var balanceCard: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Current Balance")
                .font(.headline)
                .foregroundColor(AppColors.primaryText)

            Text(
                CurrencyFormatter.format(
                    amount: currentBalance,
                    currencyCode: settingsVM.settings.currencyCode
                )
            )
            .font(.largeTitle)
            .fontWeight(.bold)
            .foregroundColor(currentBalance >= 0 ? AppColors.income : AppColors.expense)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .background(AppColors.cardBackground)
        .cornerRadius(12)
        .shadow(radius: 2)
    }

    private var recentTransactionsSection: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Recent Transactions")
                .font(.headline)
                .foregroundColor(AppColors.primaryText)

            if recentTransactions.isEmpty {
                Text("No transactions yet")
                    .font(.caption)
                    .foregroundColor(AppColors.secondaryText)
            } else {
                ForEach(recentTransactions) { transaction in
                    TransactionRow(
                        transaction: transaction,
                        currencyCode: settingsVM.settings.currencyCode
                    )
                }
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .background(AppColors.cardBackground)
        .cornerRadius(12)
        .shadow(radius: 2)
    }

    private func refreshBudgets() {
        let start = DateHelper.startOfMonth(
            for: Date(),
            monthStartDay: settingsVM.settings.monthStartDay
        )

        let end = DateHelper.endOfMonth(
            for: Date(),
            monthStartDay: settingsVM.settings.monthStartDay
        )

        budgetVM.updateBudgets(
            transactions: transactionVM.expandedTransactions(from: start, to: end),
            startDate: start,
            endDate: end
        )
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