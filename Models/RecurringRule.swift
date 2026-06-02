import Foundation

// MARK: - Recurrence Units
enum RecurringUnit: String, Codable, CaseIterable, Identifiable {
    case days
    case weeks
    case months
    case years

    var id: String {
        rawValue
    }

    var displayName: String {
        switch self {
        case .days:
            return "Days"
        case .weeks:
            return "Weeks"
        case .months:
            return "Months"
        case .years:
            return "Years"
        }
    }

    var calendarComponent: Calendar.Component {
        switch self {
        case .days:
            return .day
        case .weeks:
            return .weekOfYear
        case .months:
            return .month
        case .years:
            return .year
        }
    }
}

// MARK: - Recurring Rule Model
struct RecurringRule: Codable, Identifiable {

    // MARK: - Identity
    let id: UUID

    // MARK: - Rule
    var interval: Int
    var unit: RecurringUnit
    var startDate: Date
    var endDate: Date?

    // MARK: - Initialiser
    init(
        id: UUID = UUID(),
        interval: Int,
        unit: RecurringUnit,
        startDate: Date,
        endDate: Date? = nil
    ) {
        self.id = id
        self.interval = interval
        self.unit = unit
        self.startDate = startDate
        self.endDate = endDate
    }

    // MARK: - Helpers
    func occurs(on date: Date) -> Bool {
        guard interval > 0 else { return false }
        guard date >= startDate else { return false }

        if let endDate, date > endDate {
            return false
        }

        let calendar = Calendar.current

        switch unit {
        case .days:
            let difference = calendar.dateComponents([.day], from: startDate, to: date).day ?? 0
            return difference % interval == 0

        case .weeks:
            let difference = calendar.dateComponents([.weekOfYear], from: startDate, to: date).weekOfYear ?? 0
            return difference % interval == 0

        case .months:
            let difference = calendar.dateComponents([.month], from: startDate, to: date).month ?? 0
            return difference % interval == 0

        case .years:
            let difference = calendar.dateComponents([.year], from: startDate, to: date).year ?? 0
            return difference % interval == 0
        }
    }
}