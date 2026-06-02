import SwiftUI

struct AddTransactionView: View {

    @EnvironmentObject var transactionVM: TransactionViewModel
    @EnvironmentObject var settingsVM: SettingsViewModel
    @EnvironmentObject var categoryVM: CategoryViewModel
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

    // MARK: - Validation
    @State private var showValidationAlert = false
    @State private var validationMessage = ""

    var body: some View {
        NavigationView {
            Form {

                Picker("Type", selection: $isIncome) {
                    Text("Income").tag(true)
                    Text("Expense").tag(false)
                }
                .pickerStyle(SegmentedPickerStyle())

                Picker("Category", selection: $selectedCategory) {
                    ForEach(categoryVM.allCategories) { category in
                        HStack {
                            Image(systemName: category.systemIcon)
                            Text(category.name)
                        }
                        .tag(category)
                    }
                }

                TextField("Title", text: $title)

                TextField("Notes", text: $notes)

                TextField("Amount", text: $amount)
                    .keyboardType(.decimalPad)

                DatePicker("Date", selection: $date, displayedComponents: [.date])

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
            .alert(
                "Unable to Save",
                isPresented: $showValidationAlert
            ) {
                Button("OK") { }
            } message: {
                Text(validationMessage)
            }
        }
    }

    private func saveTransaction() {
        let trimmedTitle = title.trimmingCharacters(
            in: .whitespacesAndNewlines
        )

        guard let amountValue = Double(amount) else {
            validationMessage = "Please enter a valid amount."
            showValidationAlert = true
            return
        }

        guard amountValue > 0 else {
            validationMessage = "Amount must be greater than zero."
            showValidationAlert = true
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
            title: trimmedTitle.isEmpty
                ? selectedCategory.name
                : trimmedTitle,
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
            .environmentObject(CategoryViewModel())
    }
}