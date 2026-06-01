import Foundation
import SwiftUI

class BudgetViewModel: ObservableObject {

    @Published var budgets: [Budget] = []

    @EnvironmentObject var transactionVM: TransactionViewModel
    @EnvironmentObject var settingsVM: SettingsViewModel

    private let calendar = Calendar.current

    // MARK: - Add new budget
    func addBudget(_ budget: Budget) {
        budgets.append(budget)
    }

    // MARK: - Reset budgets at month/payroll start
    func resetBudgets() {
        for index in budgets.indices {

            let budget = budgets[index]

            var newSpent: Double = 0
            var newRemaining: Double = budget.limit

            if settingsVM.settings.carryOverEnabled {
                // Carry over remaining from last period
                let remaining = budget.limit - budget.spent
                newRemaining += max(0, remaining)
            }

            budgets[index].spent = newSpent
            budgets[index].remaining = newRemaining
            budgets[index].progress = 0
        }
    }

    // MARK: - Update budgets based on transactions
    func updateBudgets(for startDate: Date, endDate: Date) {
        for index in budgets.indices {
            let budget = budgets[index]

            let categoryTransactions = transactionVM.expandedTransactions(from: startDate, to: endDate)
                .filter { $0.category.name == budget.category.name && !$0.isIncome }

            let spentAmount = categoryTransactions.reduce(0) { $0 + $1.amount }
            budgets[index].spent = spentAmount
            budgets[index].remaining = max(0, budget.limit - spentAmount)
            budgets[index].progress = min(1.0, spentAmount / budget.limit)
        }
    }

    // MARK: - Helper: Get current payroll month date range
    func currentPeriodRange() -> (start: Date, end: Date) {
        guard let settings = settingsVM.settings else {
            let now = Date()
            return (now, now)
        }

        let monthStartDay = settings.monthStartDay

        let start = DateHelper.startOfMonth(for: Date(), monthStartDay: monthStartDay)
        let end = DateHelper.endOfMonth(for: Date(), monthStartDay: monthStartDay)

        return (start, end)
    }
}
