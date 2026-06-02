import Foundation

struct DataManager {

    // MARK: - Keys
    private static let transactionsKey = "ledgerly.transactions"
    private static let budgetsKey = "ledgerly.budgets"
    private static let settingsKey = "ledgerly.settings"
    private static let userCategoriesKey = "ledgerly.userCategories"

    // MARK: - Save
    static func saveTransactions(_ transactions: [Transaction]) {
        save(transactions, forKey: transactionsKey)
    }

    static func saveBudgets(_ budgets: [Budget]) {
        save(budgets, forKey: budgetsKey)
    }

    static func saveSettings(_ settings: SettingsModel) {
        save(settings, forKey: settingsKey)
    }

    static func saveUserCategories(_ categories: [Category]) {
        save(categories, forKey: userCategoriesKey)
    }

    // MARK: - Load
    static func loadTransactions() -> [Transaction] {
        load([Transaction].self, forKey: transactionsKey) ?? []
    }

    static func loadBudgets() -> [Budget] {
        load([Budget].self, forKey: budgetsKey) ?? []
    }

    static func loadSettings() -> SettingsModel {
        load(SettingsModel.self, forKey: settingsKey) ?? SettingsModel()
    }

    static func loadUserCategories() -> [Category] {
        load([Category].self, forKey: userCategoriesKey) ?? []
    }

    // MARK: - Private Helpers
    private static func save<T: Encodable>(_ value: T, forKey key: String) {
        do {
            let data = try JSONEncoder().encode(value)
            UserDefaults.standard.set(data, forKey: key)
        } catch {
            print("Failed to save \(key): \(error)")
        }
    }

    private static func load<T: Decodable>(_ type: T.Type, forKey key: String) -> T? {
        guard let data = UserDefaults.standard.data(forKey: key) else {
            return nil
        }

        do {
            return try JSONDecoder().decode(type, from: data)
        } catch {
            print("Failed to load \(key): \(error)")
            return nil
        }
    }
}