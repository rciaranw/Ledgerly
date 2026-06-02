import Foundation

struct Budget: Identifiable, Codable {

    // MARK: - Identity
    let id: UUID

    // MARK: - Category
    let category: Category

    // MARK: - Budget Values
    var limit: Double
    var spent: Double

    // MARK: - Initialiser
    init(
        id: UUID = UUID(),
        category: Category,
        limit: Double,
        spent: Double = 0
    ) {
        self.id = id
        self.category = category
        self.limit = limit
        self.spent = spent
    }

    // MARK: - Computed Values

    var remaining: Double {
        limit - spent
    }

    var progress: Double {
        guard limit > 0 else {
            return 0
        }

        return spent / limit
    }

    var isOverBudget: Bool {
        spent > limit
    }
}