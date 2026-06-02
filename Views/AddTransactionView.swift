import SwiftUI

struct AddTransactionView: View {

    @EnvironmentObject var transactionVM: TransactionViewModel
    @EnvironmentObject var settingsVM: SettingsViewModel
    @Environment(\.presentationMode) var presentationMode

    // MARK: - Transaction Inputs
    @State private var isIncome: Bool = true
    @State private var selectedCategory: Category = DefaultCategories.fallback
    @State private var title: String = ""
    @State private var notes: String = ""
    @State private var amount: String = ""
    @State private var date: Date = Date()

    // MARK: - Recurring
    @State private var isRecurring: Bool = false
    @State private var recurrenceInterval: Int = 1
    @State private var recurrenceUnit: RecurringUnit = .months
    @State private var recurrenceEndDate: Date = Date()

    private let categories: [Category] = DefaultCategories.all

    var body: some View {
        NavigationView {
            Form {

                // MARK: - Type Selector
                Picker("Type", selection: $isIncome) {
                    Text("Income").tag(true)
                    Text("Expense").tag(false)
                }
                .pickerStyle(SegmentedPickerStyle())

                // MARK: - Category Picker
                Picker("Category", selection: $selectedCategory) {
                    ForEach(categories) { category in
                        HStack {
                            Image(systemName: category.systemIcon)
                            Text(category.name)
                        }
                        .tag(category)
                    }
                }

                // MARK: - Title
                TextField("Title", text: $title)

                // MARK: - Notes
                TextField("Notes", text: $notes)

                // MARK: - Amount
                TextField("Amount", text: $amount)
                    .keyboardType(.decimalPad)

                // MARK: - Date
                DatePicker("Date", selection: $date, displayedComponents: [.date])

                // MARK: - Recurring Transaction
                Toggle("Recurring Transaction", isOn: $isRecurring)

                if isRecurring {
                    Stepper(
                        "Every \(recurrenceInterval) \(recurrenceUnit.displayName)",
                        value: $recurrenceInterval,
                        in: 1...31
                    )

                    Picker("Unit", selection: $recurrenceUnit) {
                        ForEach(RecurringUnit.allCases) { unit in
                            Text(unit.displayName).tag(unit)
                        }
                    }

                    DatePicker(
                        "End Date",
                        selection: $recurrenceEndDate,
                        displayedComponents: [.date]
                    )
                }
            }
            .navigationTitle("Add Transaction")
            .navigationBarItems(
                leading: Button("Cancel") {
                    presentationMode.wrappedValue.dismiss()
                },
                trailing: Button("Save") {
                    saveTransaction()
                }
            )
        }
    }

    // MARK: - Save Transaction
    private func saveTransaction() {
        guard let amountValue = Double(amount) else {
            return
        }

        let recurringRule = isRecurring
            ? RecurringRule(
                interval: recurrenceInterval,
                unit: recurrenceUnit,
                startDate: date,
                endDate: recurrenceEndDate
            )
            : nil

        let transaction = Transaction(
            title: title.isEmpty ? selectedCategory.name : title,
            notes: notes,
            amount: amountValue,
            date: date,
            category: selectedCategory,
            isIncome: isIncome,
            recurringRule: recurringRule
        )

        transactionVM.add(transaction)
        presentationMode.wrappedValue.dismiss()
    }
}

struct AddTransactionView_Previews: PreviewProvider {
    static var previews: some View {
        AddTransactionView()
            .environmentObject(TransactionViewModel())
            .environmentObject(SettingsViewModel())
    }
}