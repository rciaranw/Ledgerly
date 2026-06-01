import SwiftUI

struct SettingsView: View {

    @EnvironmentObject var settingsVM: SettingsViewModel

    var body: some View {
        NavigationView {
            Form {

                // MARK: - Currency Picker
                Section(header: Text("Currency")) {
                    Picker("Currency", selection: $settingsVM.settings.currencyCode) {
                        Text("GBP (£)").tag("GBP")
                        Text("USD ($)").tag("USD")
                        Text("EUR (€)").tag("EUR")
                    }
                    .pickerStyle(SegmentedPickerStyle())
                }

                // MARK: - Week Start Picker
                Section(header: Text("Week Start")) {
                    Picker("Start Day of Week", selection: $settingsVM.settings.weekStartDay) {
                        ForEach(1...7, id: \.self) { day in
                            Text(dayName(from: day)).tag(day)
                        }
                    }
                }

                // MARK: - Month Start / Payroll
                Section(header: Text("Month / Payroll Start")) {
                    Stepper("Start Day: \(settingsVM.settings.monthStartDay)",
                            value: $settingsVM.settings.monthStartDay,
                            in: 1...31)
                }

                // MARK: - Budget Carryover
                Section(header: Text("Budget")) {
                    Toggle("Carry Over Remaining Budget", isOn: $settingsVM.settings.carryOverEnabled)
                }

                // MARK: - Theme
Section(header: Text("Theme")) {
    Picker("Theme", selection: $settingsVM.settings.theme) {
        Text("Turquoise").tag("Turquoise")
        Text("Dark").tag("Dark")
        Text("Light").tag("Light")
    }
}

            }
            .navigationTitle("Settings")
        }
    }

    // MARK: - Helper to get day name
    private func dayName(from index: Int) -> String {
        let formatter = DateFormatter()
        return formatter.weekdaySymbols[(index - 1) % 7]
    }
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView()
            .environmentObject(SettingsViewModel())
    }
}
