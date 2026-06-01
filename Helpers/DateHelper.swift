import Foundation

struct DateHelper {

    static let calendar = Calendar.current

    // MARK: - Start of Custom Week
    static func startOfWeek(for date: Date, weekStartDay: Int) -> Date {
        var components = calendar.dateComponents([.yearForWeekOfYear, .weekOfYear], from: date)
        let start = calendar.date(from: components)!
        let currentWeekday = calendar.component(.weekday, from: start)
        let offset = (weekStartDay - currentWeekday + 7) % 7
        return calendar.date(byAdding: .day, value: offset, to: start)!
    }

    // MARK: - End of Custom Week
    static func endOfWeek(for date: Date, weekStartDay: Int) -> Date {
        let start = startOfWeek(for: date, weekStartDay: weekStartDay)
        return calendar.date(byAdding: .day, value: 6, to: start)!
    }

    // MARK: - Start of Custom Month (payroll)
    static func startOfMonth(for date: Date, monthStartDay: Int) -> Date {
        let components = calendar.dateComponents([.year, .month], from: date)
        var day = monthStartDay
        var month = components.month!
        var year = components.year!

        // If the day is after today, go to previous month
        if let today = calendar.date(from: components),
           day > calendar.component(.day, from: date) {
            month -= 1
            if month < 1 {
                month = 12
                year -= 1
            }
        }

        var newComponents = DateComponents(year: year, month: month, day: day)
        return calendar.date(from: newComponents)!
    }

    // MARK: - End of Custom Month (payroll)
    static func endOfMonth(for date: Date, monthStartDay: Int) -> Date {
        let start = startOfMonth(for: date, monthStartDay: monthStartDay)
        return calendar.date(byAdding: .month, value: 1, to: start)!
            .addingTimeInterval(-1) // End of day
    }

    // MARK: - Format Date
    static func formatDate(_ date: Date, format: String = "dd/MM/yyyy") -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = format
        return formatter.string(from: date)
    }
}
