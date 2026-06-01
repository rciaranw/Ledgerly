import Foundation

struct SettingsModel: Codable {
    var currencyCode: String = "GBP"          // Default currency
    var weekStartDay: Int = 1                 // 1 = Monday
    var monthStartDay: Int = 1                // 1 = default start of month, can be payroll start
    var carryOverEnabled: Bool = false        // Carry over remaining budget
    var theme: String = "Turquoise"           // Default theme
}
