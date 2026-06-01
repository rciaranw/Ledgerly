import Foundation

struct SettingsModel: Codable {

    // MARK: - Currency
    var currencyCode: String = "GBP" // default, can switch to EUR or USD

    // MARK: - Week Start
    var weekStartDay: Int = 1        // 1 = Monday, 7 = Sunday

    // MARK: - Month Start
    var monthStartDay: Int = 1       // 1 = first of month by default
                                     // Used for custom payroll month views

    // MARK: - Theme
    // Future feature: dark mode, light mode, custom colors
    var theme: String = "Default"    

    // MARK: - Budget Carryover
    var carryOverEnabled: Bool = false
}
