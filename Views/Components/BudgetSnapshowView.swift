import SwiftUI

struct BudgetSnapshotView: View {

    let budgets: [Budget]
    let currencyCode: String

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text("Budget Snapshot")
                .font(.headline)

            ForEach(budgets) { budget in
                VStack(alignment: .leading) {
                    HStack {
                        Image(systemName: budget.category.systemIcon)
                            .foregroundColor(AppColors.accent)
                        Text(budget.category.name)
                        Spacer()
                        Text(CurrencyFormatter.format(amount: budget.remaining,
                                                      currencyCode: currencyCode))
                            .foregroundColor(budget.isOverBudget ? AppColors.expense : AppColors.income)
                    }

                    ProgressView(value: budget.progress)
                        .accentColor(budget.isOverBudget ? AppColors.expense : AppColors.accent)
                }
                .padding(.vertical, 4)
            }
        }
        .padding()
        .background(AppColors.cardBackground)
        .cornerRadius(12)
        .shadow(radius: 2)
    }
}

struct BudgetSnapshotView_Previews: PreviewProvider {
    static var previews: some View {
        BudgetSnapshotView(
            budgets: [
                Budget(category: Category(name: "Shopping", systemIcon: "bag.fill", isDefault: true),
                       limit: 200, spent: 50),
                Budget(category: Category(name: "Travel", systemIcon: "airplane", isDefault: true),
                       limit: 150, spent: 180)
            ],
            currencyCode: "GBP"
        )
        .previewLayout(.sizeThatFits)
    }
}
