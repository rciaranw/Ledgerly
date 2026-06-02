import SwiftUI

struct AddTransactionView: View {

    @EnvironmentObject var transactionVM: TransactionViewModel
    @EnvironmentObject var settingsVM: SettingsViewModel
    @EnvironmentObject var categoryVM: CategoryViewModel
    @Environment(\.presentationMode) var presentationMode

    // MARK: - Edit Support
    let existingTransaction: Transaction?

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
    @State private var recurrenceHasEndDate: Bool = false
    @State private var recurrenceEndDate: Date = Date()

    // MARK: - Validation
    @State private var showValidationAlert = false
    @State private var validationMessage = ""

    init(existingTransaction: Transaction? = nil) {
        self.existingTransaction = existingTransaction
    }

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

                DatePicker(
                    "Date",
                    selection: $date,
                    displayedComponents: [.date]
                )

                Toggle(
                    "Recurring Transaction",
                    isOn: $isRecurring
                )

                if isRecurring {

                    Stepper(
                        "Every \(recurrenceInterval) \(recurrenceUnit.displayName)",
                        value: $recurrenceInterval,
                        in: 1...31
                    )

                    Picker(
                        "Unit",
                        selection: $recurrenceUnit
                    ) {
                        ForEach(RecurringUnit.allCases) { unit in
                            Text(unit.displayName)
                                .tag(unit)
                        }
                    }

                    Toggle(
                        "End Recurrence",
                        isOn: $recurrenceHasEndDate
                    )

                    if recurrenceHasEndDate {
                        DatePicker(
                            "End Date",
                            selection: $recurrenceEndDate,
                            displayedComponents: [.date]
                        )
                    }
                }
            }
            .navigationTitle(
                existingTransaction == nil
                ? "Add Transaction"
                : "Edit Transaction"
            )
            .navigationBarItems(
                leading: Button("Cancel") {
                    presentationMode.wrappedValue.dismiss()
                },
                trailing: Button(
                    existingTransaction == nil
                    ? "Save"
                    : "Update"
                ) {
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
            .onAppear {
                populateExistingTransaction()
            }
        }
    }

    private func populateExistingTransaction() {

        guard let transaction = existingTransaction else {
            return
        }

        title = transaction.title
        notes = transaction.notes
        amount = String(transaction.amount)
        date = transaction.date
        selectedCategory = transaction.category
        isIncome = transaction.isIncome

        if let recurringRule = transaction.recurringRule {

            isRecurring = true
            recurrenceInterval = recurringRule.interval
            recurrenceUnit = recurringRule.unit

            if let endDate = recurringRule.endDate {
                recurrenceHasEndDate = true
                recurrenceEndDate = endDate
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
                endDate: recurrenceHasEndDate
                    ? recurrenceEndDate
                    : nil
            )
            : nil

        if let existingTransaction {

            let updatedTransaction = Transaction(
                id: existingTransaction.id,
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

            transactionVM.update(updatedTransaction)

        } else {

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
        }

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