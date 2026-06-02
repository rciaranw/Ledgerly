import Foundation

struct CSVExporter {

    /// Exports transactions to CSV format
    static func export(
        transactions: [Transaction],
        currencyCode: String = "GBP"
    ) -> String {

        var csv =
        "Title,Notes,Amount,Currency,Type,Category,Date,Recurring\n"

        let formatter = DateFormatter()
        formatter.dateFormat = "dd/MM/yyyy"

        for transaction in transactions {

            let title = escapeCSVField(transaction.title)
            let notes = escapeCSVField(transaction.notes)

            let amount = String(format: "%.2f", transaction.amount)

            let type = transaction.isIncome
                ? "Income"
                : "Expense"

            let category = escapeCSVField(transaction.category.name)

            let date = formatter.string(from: transaction.date)

            let recurring = transaction.recurringRule == nil
                ? "No"
                : "Yes"

            csv.append(
                "\(title)," +
                "\(notes)," +
                "\(amount)," +
                "\(currencyCode)," +
                "\(type)," +
                "\(category)," +
                "\(date)," +
                "\(recurring)\n"
            )
        }

        return csv
    }

    private static func escapeCSVField(_ field: String) -> String {

        var escaped = field.replacingOccurrences(
            of: "\"",
            with: "\"\""
        )

        if escaped.contains(",")
            || escaped.contains("\n")
            || escaped.contains("\"") {

            escaped = "\"\(escaped)\""
        }

        return escaped
    }
}