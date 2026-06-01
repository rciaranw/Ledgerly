import Foundation

struct Transaction: Identifiable, Codable {

    // MARK: - Core Identity
    let id: UUID

    // MARK: - Transaction Details
    var title: String              // Merchant or description
    var notes: String
    var amount: Double
    var date: Date

    // MARK: - Classification
    var category: Category
    var isIncome: Bool

    // MARK: - Recurring
    var recurringRule: RecurringRule?

    // MARK: - Initialiser
    init(
        id: UUID = UUID(),
        title: String,
        notes: String = "",
        amount: Double,
        date: Date,
        category: Category,
        isIncome: Bool,
        recurringRule: RecurringRule? = nil
    ) {
        self.id = id
        self.title = title
        self.notes = notes
        self.amount = amount
        self.date = date
        self.category = category
        self.isIncome = isIncome
        self.recurringRule = recurringRule
    }
}
