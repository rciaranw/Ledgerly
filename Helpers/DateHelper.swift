import Foundation

struct DateHelper {

    static let calendar = Calendar.current

    // MARK: - Start of Custom Week
    static func startOfWeek(for date: Date, weekStartDay: Int) -> Date {
        let safeWeekStartDay = min(max(weekStartDay, 1), 7)

        let weekday = calendar.component(.weekday, from: date)
        let daysToSubtract = (weekday - safeWeekStartDay + 7) % 7

        return calendar.startOfDay(
            for: calendar.date(byAdding: .day, value: -daysToSubtract, to: date) ?? date
        )
    }

    // MARK: - End of Custom Week
    static func endOfWeek(for date: Date, weekStartDay: Int) -> Date {
        let start = startOfWeek(for: date, weekStartDay: weekStartDay)

        return calendar.date(
            byAdding: DateComponents(day: 7, second: -1),
            to: start
        ) ?? start
    }

    // MARK: - Start of Custom Month / Payroll Period
    static func startOfMonth(for date: Date, monthStartDay: Int) -> Date {
        let safeDay = min(max(monthStartDay, 1), 28)

        let currentDay = calendar.component(.day, from: date)

        var components = calendar.dateComponents([.year, .month], from: date)
        components.day = safeDay

        var start = calendar.date(from: components) ?? date

        if currentDay < safeDay {
            start = calendar.date(byAdding: .month, value: -1, to: start) ?? start
        }

        return calendar.startOfDay(for: start)
    }

    // MARK: - End of Custom Month / Payroll Period
    static func endOfMonth(for date: Date, monthStartDay: Int) -> Date {
        let start = startOfMonth(for: date, monthStartDay: monthStartDay)

        return calendar.date(
            byAdding: DateComponents(month: 1, second: -1),
            to: start
        ) ?? start
    }

    // MARK: - Format Date
    static func formatDate(_ date: Date, format: String = "dd/MM/yyyy") -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = format
        return formatter.string(from: date)
    }
}