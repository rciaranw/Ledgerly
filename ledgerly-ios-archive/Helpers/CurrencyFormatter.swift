import Foundation

struct CurrencyFormatter {

    /// Format a number as currency
    /// - Parameters:
    ///   - amount: The numeric amount
    ///   - currencyCode: "GBP", "USD", "EUR"
    /// - Returns: Localized currency string
    static func format(amount: Double, currencyCode: String = "GBP") -> String {
        let formatter = NumberFormatter()
        formatter.numberStyle = .currency
        formatter.currencyCode = currencyCode
        formatter.maximumFractionDigits = 2
        formatter.minimumFractionDigits = 2
        formatter.locale = locale(for: currencyCode)
        return formatter.string(from: NSNumber(value: amount)) ?? "\(amount)"
    }

    /// Returns locale for given currency code
    private static func locale(for currencyCode: String) -> Locale {
        switch currencyCode {
        case "GBP":
            return Locale(identifier: "en_GB")
        case "USD":
            return Locale(identifier: "en_US")
        case "EUR":
            return Locale(identifier: "fr_FR") // Default European formatting
        default:
            return Locale.current
        }
    }
}
