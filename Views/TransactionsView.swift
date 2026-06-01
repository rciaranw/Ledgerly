import SwiftUI

struct TransactionsView: View {

    @EnvironmentObject var transactionVM: TransactionViewModel
    @EnvironmentObject var settingsVM: SettingsViewModel

    @State private var showAddTransaction = false
    @State private var filterStartDate: Date = Calendar.current.startOfDay(for: Date())
    @State private var filterEndDate: Date = Calendar.current.startOfDay(for: Date())

    var body: some View {
        NavigationView {
            List {
                ForEach(filteredTransactions) { tx in
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
                }
                .onDelete(perform: transactionVM.delete)
            }
            .navigationTitle("Transactions")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: { showAddTransaction.toggle() }) {
                        Image(systemName: "plus")
                    }
                }
            }
            .sheet(isPresented: $showAddTransaction) {
                AddTransactionView()
                    .environmentObject(transactionVM)
                    .environmentObject(settingsVM)
            }
        }
    }

    // MARK: - Filtered Transactions
    private var filteredTransactions: [Transaction] {
        transactionVM.expandedTransactions(from: filterStartDate, to: filterEndDate)
    }
}

struct TransactionsView_Previews: PreviewProvider {
    static var previews: some View {
        TransactionsView()
            .environmentObject(TransactionViewModel())
            .environmentObject(SettingsViewModel())
    }
}
