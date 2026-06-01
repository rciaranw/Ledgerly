import Foundation
import SwiftUI

class TransactionViewModel: ObservableObject {

    @Published var transactions: [Transaction] = []

    // MARK: - Add new transaction
    func addTransaction(_ transaction: Transaction) {
        transactions.append(transaction)
    }

    // MARK: - Delete transaction
    func deleteTransaction(_ transaction: Transaction) {
        transactions.removeAll { $0.id == transaction.id }
    }

    // MARK: - Expanded transactions
    /// Returns all transactions, expanding recurring ones within the given date range
    func expandedTransactions(from startDate: Date, to endDate: Date) -> [Transaction] {
        var expanded: [Transaction] = []

        for transaction in transactions {
            if let rule = transaction.recurringRule {
                // Expand recurring transaction
                expanded.append(contentsOf: expand(transaction: transaction, rule: rule, from: startDate, to: endDate))
            } else {
                // Non-recurring
                if transaction.date >= startDate && transaction.date <= endDate {
                    expanded.append(transaction)
                }
            }
        }

        // Sort by date descending
        return expanded.sorted(by: { $0.date > $1.date })
    }

    // MARK: - Recurring expansion helper
    private func expand(transaction: Transaction, rule: RecurringRule, from startDate: Date, to endDate: Date) -> [Transaction] {
        var occurrences: [Transaction] = []

        // Determine first occurrence after startDate
        var currentDate = max(transaction.date, startDate)

        let calendar = Calendar.current
        while currentDate <= endDate {

            // Check if this occurrence is after original transaction date
            if currentDate >= transaction.date {

                // Create a new Transaction instance for this occurrence
                let newTransaction = Transaction(
                    title: transaction.title,
                    notes: transaction.notes,
                    amount: transaction.amount,
                    date: currentDate,
                    category: transaction.category,
                    isIncome: transaction.isIncome,
                    recurringRule: rule
                )

                occurrences.append(newTransaction)
            }

            // Increment date based on recurring frequency
            switch rule.frequencyUnit {
            case .day:
                currentDate = calendar.date(byAdding: .day, value: rule.interval, to: currentDate)!
            case .week:
                currentDate = calendar.date(byAdding: .weekOfYear, value: rule.interval, to: currentDate)!
            case .month:
                currentDate = calendar.date(byAdding: .month, value: rule.interval, to: currentDate)!
            case .year:
                currentDate = calendar.date(byAdding: .year, value: rule.interval, to: currentDate)!
            }
        }

        return occurrences
    }
}
