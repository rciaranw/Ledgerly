import SwiftUI

struct SettingsView: View {

    @EnvironmentObject var settingsVM: SettingsViewModel
    @EnvironmentObject var budgetVM: BudgetViewModel
    @EnvironmentObject var categoryVM: CategoryViewModel

    @State private var newCategoryName = ""

    var body: some View {
        NavigationView {
            Form {

                // MARK: - Currency
                Section(header: Text("Currency")) {
                    Picker("Currency", selection: $settingsVM.settings.currencyCode) {
                        Text("GBP (£)").tag("GBP")
                        Text("USD ($)").tag("USD")
                        Text("EUR (€)").tag("EUR")
                    }
                    .pickerStyle(SegmentedPickerStyle())
                }

                // MARK: - Week Start
                Section(header: Text("Week Start")) {
                    Picker("Start Day of Week", selection: $settingsVM.settings.weekStartDay) {
                        ForEach(1...7, id: \.self) { day in
                            Text(dayName(from: day)).tag(day)
                        }
                    }
                }

                // MARK: - Payroll Month
                Section(header: Text("Month / Payroll Start")) {
                    Stepper(
                        "Start Day: \(settingsVM.settings.monthStartDay)",
                        value: $settingsVM.settings.monthStartDay,
                        in: 1...31
                    )
                }

                // MARK: - Budget Carryover
                Section(header: Text("Budget")) {
                    Toggle(
                        "Carry Over Remaining Budget",
                        isOn: $settingsVM.settings.carryOverEnabled
                    )
                }

                // MARK: - Budget Limits
                Section(header: Text("Budget Limits")) {
                    ForEach($budgetVM.budgets) { $budget in
                        HStack {
                            Image(systemName: budget.category.systemIcon)
                                .foregroundColor(AppColors.accent)
                                .frame(width: 24)

                            Text(budget.category.name)

                            Spacer()

                            TextField(
                                CurrencyFormatter.format(
                                    amount: 0,
                                    currencyCode: settingsVM.settings.currencyCode
                                ),
                                value: $budget.limit,
                                format: .currency(
                                    code: settingsVM.settings.currencyCode
                                )
                            )
                            .keyboardType(.decimalPad)
                            .multilineTextAlignment(.trailing)
                            .frame(width: 110)
                        }
                    }
                }

                // MARK: - Custom Categories
                Section(header: Text("Custom Categories")) {

                    HStack {
                        TextField(
                            "New Category",
                            text: $newCategoryName
                        )

                        Button("Add") {
                            categoryVM.addUserCategory(
                                name: newCategoryName
                            )

                            newCategoryName = ""
                        }
                    }

                    if categoryVM.userCategories.isEmpty {

                        Text("No custom categories")
                            .foregroundColor(
                                AppColors.secondaryText
                            )

                    } else {

                        ForEach(categoryVM.userCategories) { category in

                            HStack {
                                Image(
                                    systemName: category.systemIcon
                                )
                                .foregroundColor(
                                    AppColors.accent
                                )

                                Text(category.name)
                            }
                        }
                        .onDelete(
                            perform: categoryVM.deleteUserCategory
                        )
                    }
                }

                // MARK: - Theme
                Section(header: Text("Theme")) {
                    Picker(
                        "Theme",
                        selection: $settingsVM.settings.theme
                    ) {
                        Text("Turquoise")
                            .tag("Turquoise")

                        Text("Dark")
                            .tag("Dark")

                        Text("Light")
                            .tag("Light")
                    }
                }
            }
            .navigationTitle("Settings")
        }
    }

    private func dayName(from index: Int) -> String {
        let formatter = DateFormatter()
        return formatter.weekdaySymbols[(index - 1) % 7]
    }
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView()
            .environmentObject(SettingsViewModel())
            .environmentObject(BudgetViewModel())
            .environmentObject(CategoryViewModel())
    }
}