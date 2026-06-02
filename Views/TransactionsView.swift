import SwiftUI

struct TransactionsView: View {

    @EnvironmentObject var transactionVM: TransactionViewModel
    @EnvironmentObject var settingsVM: SettingsViewModel

    @State private var showAddTransaction = false

    var body: some View {
        NavigationView {
            List {
                if filteredTransactions.isEmpty {

                    Text("No transactions found")
                        .foregroundColor(AppColors.secondaryText)

                } else {

                    ForEach(filteredTransactions) { transaction in

                        TransactionRow(
                            transaction: transaction,
                            currencyCode: settingsVM.settings.currencyCode
                        )
                    }
                    .onDelete(perform: transactionVM.delete)
                }
            }
            .navigationTitle("Transactions")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button {
                        showAddTransaction.toggle()
                    } label: {
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
    }
}