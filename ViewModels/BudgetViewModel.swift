import Foundation
import SwiftUI

class BudgetViewModel: ObservableObject {

    @Published var budgets: [Budget] = DefaultCategories.all.map {
        Budget(
            category: $0,
            limit: 0,
            spent: 0
        )
    }

    // MARK: - Add Budget
    func addBudget(_ budget: Budget) {
        budgets.append(budget)
    }

    // MARK: - Set or Update Budget
    func setBudget(for category: Category, limit: Double) {
        if let index = budgets.firstIndex(where: { $0.category.id == category.id }) {
            budgets[index].limit = limit
        } else {
            let newBudget = Budget(
                category: category,
                limit: limit,
                spent: 0
            )
            budgets.append(newBudget)
        }
    }

    // MARK: - Budget Lookup
    func budget(for category: Category) -> Budget? {
        budgets.first { $0.category.id == category.id }
    }

    // MARK: - Update Budgets From Transactions
    func updateBudgets(
        transactions: [Transaction],
        startDate: Date,
        endDate: Date
    ) {
        for index in budgets.indices {
            let category = budgets[index].category

            let spentAmount = transactions
                .filter {
                    !$0.isIncome &&
                    $0.category.id == category.id &&
                    $0.date >= startDate &&
                    $0.date <= endDate
                }
                .reduce(0) { $0 + $1.amount }

            budgets[index].spent = spentAmount
        }
    }

    // MARK: - Reset Budgets
    func resetBudgets(carryOverEnabled: Bool) {
        for index in budgets.indices {
            if carryOverEnabled {
                let remaining = budgets[index].remaining
                budgets[index].spent = remaining > 0 ? -remaining : 0
            } else {
                budgets[index].spent = 0
            }
        }
    }

    // MARK: - Totals
    var totalLimit: Double {
        budgets.reduce(0) { $0 + $1.limit }
    }

    var totalSpent: Double {
        budgets.reduce(0) { $0 + $1.spent }
    }

    var totalRemaining: Double {
        budgets.reduce(0) { $0 + $1.remaining }
    }
}