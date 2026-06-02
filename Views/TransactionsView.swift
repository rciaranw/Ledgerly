import SwiftUI

struct TransactionsView: View {

    @EnvironmentObject var transactionVM: TransactionViewModel
    @EnvironmentObject var settingsVM: SettingsViewModel
    @EnvironmentObject var categoryVM: CategoryViewModel

    @State private var showAddTransaction = false
    @State private var transactionToEdit: Transaction?

    var body: some View {
        NavigationView {
            List {
                if filteredTransactions.isEmpty {

                    Text("No transactions found")
                        .foregroundColor(AppColors.secondaryText)

                } else {

                    ForEach(filteredTransactions) { transaction in
                        Button {
                            transactionToEdit = transaction
                        } label: {
                            TransactionRow(
                                transaction: transaction,
                                currencyCode: settingsVM.settings.currencyCode
                            )
                        }
                        .buttonStyle(PlainButtonStyle())
                    }
                    .onDelete(perform: transactionVM.delete)
                }
            }
            .navigationTitle("Transactions")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button {
                        showAddTransaction = true
                    } label: {
                        Image(systemName: "plus")
                    }
                }
            }
            .sheet(isPresented: $showAddTransaction) {
                AddTransactionView()
                    .environmentObject(transactionVM)
                    .environmentObject(settingsVM)
                    .environmentObject(categoryVM)
            }
            .sheet(item: $transactionToEdit) { transaction in
                AddTransactionView(existingTransaction: transaction)
                    .environmentObject(transactionVM)
                    .environmentObject(settingsVM)
                    .environmentObject(categoryVM)
            }
        }
    }

    private var filteredTransactions: [Transaction] {
        transactionVM.transactions
            .sorted { $0.date > $1.date }
    }
}

struct TransactionsView_Previews: PreviewProvider {
    static var previews: some View {
        TransactionsView()
            .environmentObject(TransactionViewModel())
            .environmentObject(SettingsViewModel())
            .environmentObject(CategoryViewModel())
    }
}