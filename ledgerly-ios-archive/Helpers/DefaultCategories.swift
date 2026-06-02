import Foundation

struct DefaultCategories {

    static let all: [Category] = [
        Category(name: "Cash", systemIcon: "banknote.fill", isDefault: true),
        Category(name: "Cashback", systemIcon: "arrow.uturn.left.circle.fill", isDefault: true),
        Category(name: "Charity", systemIcon: "heart.fill", isDefault: true),
        Category(name: "Child Allowance", systemIcon: "figure.and.child.holdinghands", isDefault: true),
        Category(name: "Credit", systemIcon: "creditcard.fill", isDefault: true),
        Category(name: "Entertainment", systemIcon: "film.fill", isDefault: true),
        Category(name: "Gambling", systemIcon: "dice.fill", isDefault: true),
        Category(name: "General", systemIcon: "square.grid.2x2.fill", isDefault: true),
        Category(name: "Gift", systemIcon: "gift.fill", isDefault: true),
        Category(name: "Health", systemIcon: "cross.case.fill", isDefault: true),
        Category(name: "Insurance", systemIcon: "shield.fill", isDefault: true),
        Category(name: "Interest", systemIcon: "percent", isDefault: true),
        Category(name: "Investment", systemIcon: "chart.line.uptrend.xyaxis", isDefault: true),
        Category(name: "Loan", systemIcon: "hand.wave.fill", isDefault: true),
        Category(name: "Net Sales", systemIcon: "cart.fill", isDefault: true),
        Category(name: "Refund", systemIcon: "arrow.counterclockwise.circle.fill", isDefault: true),
        Category(name: "Remittances", systemIcon: "arrow.left.arrow.right.circle.fill", isDefault: true),
        Category(name: "Restaurants", systemIcon: "fork.knife", isDefault: true),
        Category(name: "Salary", systemIcon: "briefcase.fill", isDefault: true),
        Category(name: "Savings", systemIcon: "tray.full.fill", isDefault: true),
        Category(name: "Shopping", systemIcon: "bag.fill", isDefault: true),
        Category(name: "Top Ups", systemIcon: "plus.circle.fill", isDefault: true),
        Category(name: "Transfers", systemIcon: "arrow.right.arrow.left.circle.fill", isDefault: true),
        Category(name: "Travel", systemIcon: "airplane", isDefault: true),
        Category(name: "Utilities", systemIcon: "bolt.fill", isDefault: true)
    ]

    static var fallback: Category {
        Category(
            name: "General",
            systemIcon: "square.grid.2x2.fill",
            isDefault: true
        )
    }
}