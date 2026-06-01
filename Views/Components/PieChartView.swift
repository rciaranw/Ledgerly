import SwiftUI

struct PieChartView: View {

    let budgets: [Budget]        // Use budgets to calculate slices
    let currencyCode: String

    var body: some View {
        GeometryReader { geometry in
            ZStack {
                ForEach(sliceData.indices, id: \.self) { index in
                    PieSlice(
                        value: sliceData[index].value,
                        label: sliceData[index].label,
                        color: sliceData[index].color
                    )
                }
            }
            .frame(width: geometry.size.width, height: geometry.size.width)
        }
    }

    // MARK: - Slice data
    private var sliceData: [(value: Double, label: String, color: Color)] {
        let totalSpent = budgets.reduce(0) { $0 + $1.spent }
        guard totalSpent > 0 else {
            return [(1.0, "No Data", AppColors.accent)]
        }

        return budgets.map { budget in
            let fraction = budget.spent / totalSpent
            return (fraction, budget.category.name, budget.isOverBudget ? AppColors.expense : AppColors.accent)
        }
    }
}

struct PieChartView_Previews: PreviewProvider {
    static var previews: some View {
        PieChartView(
            budgets: [
                Budget(category: Category(name: "Shopping", systemIcon: "bag.fill", isDefault: true), limit: 200, spent: 50),
                Budget(category: Category(name: "Travel", systemIcon: "airplane", isDefault: true), limit: 150, spent: 100),
                Budget(category: Category(name: "Food", systemIcon: "fork.knife", isDefault: true), limit: 100, spent: 30)
            ],
            currencyCode: "GBP"
        )
        .frame(width: 200, height: 200)
        .previewLayout(.sizeThatFits)
    }
}
