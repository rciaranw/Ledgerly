import Foundation
import SwiftUI

class SettingsViewModel: ObservableObject {

    // MARK: - Settings State
    @Published var settings = SettingsModel()

    // MARK: - Currency
    func setCurrency(_ currencyCode: String) {
        settings.currencyCode = currencyCode
    }

    // MARK: - Week Start
    func setWeekStartDay(_ day: Int) {
        guard day >= 1 && day <= 7 else { return }
        settings.weekStartDay = day
    }

    // MARK: - Month / Payroll Start
    func setMonthStartDay(_ day: Int) {
        guard day >= 1 && day <= 31 else { return }
        settings.monthStartDay = day
    }

    // MARK: - Carryover
    func setCarryOverEnabled(_ enabled: Bool) {
        settings.carryOverEnabled = enabled
    }

    func toggleCarryOver() {
        settings.carryOverEnabled.toggle()
    }

    // MARK: - Theme
    func setTheme(_ theme: String) {
        settings.theme = theme
    }
}