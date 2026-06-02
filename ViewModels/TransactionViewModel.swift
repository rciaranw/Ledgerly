import Foundation
import SwiftUI

class TransactionViewModel: ObservableObject {

    @Published var transactions: [Transaction] {
        didSet {
            DataManager.saveTransactions(transactions)
        }
    }

    // MARK: - Initialisation
    init() {
        self.transactions = DataManager.loadTransactions()
        sortTransactions()
    }

    // MARK: - Add Transaction
    func add(_ transaction: Transaction) {
        transactions.append(transaction)
        sortTransactions()
    }

    func addTransaction(_ transaction: Transaction) {
        add(transaction)
    }

    // MARK: - Delete Transaction
    func delete(at offsets: IndexSet) {
        transactions.remove(atOffsets: offsets)
    }

    func deleteTransaction(_ transaction: Transaction) {
        transactions.removeAll { $0.id == transaction.id }
    }

    // MARK: - Update Transaction
    func update(_ transaction: Transaction) {
        guard let index = transactions.firstIndex(
            where: { $0.id == transaction.id }
        ) else {
            return
        }

        transactions[index] = transaction
        sortTransactions()
    }

    // MARK: - Date Filtering
    func transactions(
        from startDate: Date,
        to endDate: Date
    ) -> [Transaction] {

        transactions
            .filter {
                $0.date >= startDate &&
                $0.date <= endDate
            }
            .sorted {
                $0.date > $1.date
            }
    }

    // MARK: - Expanded Transactions
    func expandedTransactions(
        from startDate: Date,
        to endDate: Date
    ) -> [Transaction] {

        var expanded: [Transaction] = []

        for transaction in transactions {

            if let rule = transaction.recurringRule {

                expanded.append(
                    contentsOf: expandRecurringTransaction(
                        transaction,
                        rule: rule,
                        from: startDate,
                        to: endDate
                    )
                )

            } else if transaction.date >= startDate &&
                        transaction.date <= endDate {

                expanded.append(transaction)
            }
        }

        return expanded.sorted {
            $0.date > $1.date
        }
    }

    // MARK: - Private Helpers
    private func sortTransactions() {
        transactions.sort {
            $0.date > $1.date
        }
    }

    private func expandRecurringTransaction(
        _ transaction: Transaction,
        rule: RecurringRule,
        from startDate: Date,
        to endDate: Date
    ) -> [Transaction] {

        guard rule.interval > 0 else {
            return []
        }

        var occurrences: [Transaction] = []

        let calendar = Calendar.current
        var currentDate = rule.startDate

        while currentDate <= endDate {

            if currentDate >= startDate {

                if rule.endDate == nil ||
                    currentDate <= rule.endDate! {

                    let occurrence = Transaction(
                        id: UUID(),
                        title: transaction.title,
                        notes: transaction.notes,
                        amount: transaction.amount,
                        date: currentDate,
                        category: transaction.category,
                        isIncome: transaction.isIncome,
                        recurringRule: nil
                    )

                    occurrences.append(occurrence)
                }
            }

            guard let nextDate = calendar.date(
                byAdding: rule.unit.calendarComponent,
                value: rule.interval,
                to: currentDate
            ) else {
                break
            }

            currentDate = nextDate

            if let endDate = rule.endDate,
               currentDate > endDate {
                break
            }
        }

        return occurrences
    }
}