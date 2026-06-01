import Foundation

struct CSVExporter {

    /// Exports transactions to CSV format
    /// - Parameters:
    ///   - transactions: Array of Transaction
    ///   - currencyCode: User-selected currency code
    /// - Returns: CSV string
    static func export(transactions: [Transaction], currencyCode: String = "GBP") -> String {
        var csv = "Title,Notes,Amount,Type,Category,Date\n"

        let formatter = DateFormatter()
        formatter.dateFormat = "dd/MM/yyyy"

        for tx in transactions {
            let title = escapeCSVField(tx.title)
            let notes = escapeCSVField(tx.notes)
            let amount = CurrencyFormatter.format(amount: tx.amount, currencyCode: currencyCode)
            let type = tx.isIncome ? "Income" : "Expense"
            let category = escapeCSVField(tx.category.name)
            let date = formatter.string(from: tx.date)

            let row = "\(title),\(notes),\(amount),\(type),\(category),\(date)\n"
            csv.append(row)
        }

        return csv
    }

    /// Escape commas and quotes for CSV compatibility
    private static func escapeCSVField(_ field: String) -> String {
        var escaped = field.replacingOccurrences(of: "\"", with: "\"\"")
        if escaped.contains(",") || escaped.contains("\n") || escaped.contains("\"") {
            escaped = "\"\(escaped)\""
        }
        return escaped
    }
}
