import Foundation

// MARK: - Recurrence Units
enum RecurringUnit: String, Codable, CaseIterable {
    case days
    case weeks
    case months
    case years
}

// MARK: - Recurring Rule Model
struct RecurringRule: Codable, Identifiable {
    
    // Unique ID for each recurring setup
    let id: UUID
    
    // Interval of recurrence, e.g., every 2 weeks
    var interval: Int
    
    // Unit of recurrence
    var unit: RecurringUnit
    
    // Start date for recurrence
    var startDate: Date
    
    // Optional end date (nil = indefinite)
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
    
    // MARK: - Helper
    
    /// Checks if a given date is included in this recurring schedule
    func occurs(on date: Date) -> Bool {
        guard date >= startDate else { return false }
        
        let calendar = Calendar.current
        switch unit {
        case .days:
            let daysDiff = calendar.dateComponents([.day], from: startDate, to: date).day ?? 0
            return daysDiff % interval == 0
        case .weeks:
            let weeksDiff = calendar.dateComponents([.weekOfYear], from: startDate, to: date).weekOfYear ?? 0
            return weeksDiff % interval == 0
        case .months:
            let monthsDiff = calendar.dateComponents([.month], from: startDate, to: date).month ?? 0
            return monthsDiff % interval == 0
        case .years:
            let yearsDiff = calendar.dateComponents([.year], from: startDate, to: date).year ?? 0
            return yearsDiff % interval == 0
        }
    }
}
