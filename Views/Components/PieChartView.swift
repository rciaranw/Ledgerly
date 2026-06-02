import SwiftUI

struct PieChartView: View {

    let budgets: [Budget]
    let currencyCode: String

    private var totalSpent: Double {
        budgets.reduce(0) { $0 + max($1.spent, 0) }
    }

    var body: some View {
        VStack(spacing: 12) {
            if totalSpent <= 0 {
                emptyState
            } else {
                chart
                legend
            }
        }
        .padding()
        .background(AppColors.cardBackground)
        .cornerRadius(12)
        .shadow(radius: 2)
    }

    private var chart: some View {
        GeometryReader { geometry in
            ZStack {
                ForEach(slices.indices, id: \.self) { index in
                    PieSlice(
                        startAngle: slices[index].startAngle,
                        endAngle: slices[index].endAngle,
                        color: slices[index].color
                    )
                }
            }
            .frame(
                width: min(geometry.size.width, geometry.size.height),
                height: min(geometry.size.width, geometry.size.height)
            )
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
        .frame(height: 180)
    }

    private var legend: some View {
        VStack(alignment: .leading, spacing: 8) {
            ForEach(slices.indices, id: \.self) { index in
                HStack {
                    Circle()
                        .fill(slices[index].color)
                        .frame(width: 10, height: 10)

                    Text(slices[index].label)
                        .font(.caption)
                        .foregroundColor(AppColors.primaryText)

                    Spacer()

                    Text(
                        CurrencyFormatter.format(
                            amount: slices[index].amount,
                            currencyCode: currencyCode
                        )
                    )
                    .font(.caption)
                    .foregroundColor(AppColors.secondaryText)
                }
            }
        }
    }

    private var emptyState: some View {
        VStack(spacing: 8) {
            Image(systemName: "chart.pie")
                .font(.title)
                .foregroundColor(AppColors.accent)

            Text("No spending data yet")
                .font(.caption)
                .foregroundColor(AppColors.secondaryText)
        }
        .frame(maxWidth: .infinity)
        .padding()
    }

    private var slices: [PieChartSlice] {
        var result: [PieChartSlice] = []
        var currentAngle = 0.0

        let spendingBudgets = budgets
            .filter { $0.spent > 0 }
            .sorted { $0.spent > $1.spent }

        for budget in spendingBudgets {
            let percentage = budget.spent / totalSpent
            let angle = percentage * 360
            let endAngle = currentAngle + angle

            result.append(
                PieChartSlice(
                    label: budget.category.name,
                    amount: budget.spent,
                    startAngle: currentAngle,
                    endAngle: endAngle,
                    color: budget.isOverBudget ? AppColors.expense : AppColors.accent
                )
            )

            currentAngle = endAngle
        }

        return result
    }
}

private struct PieChartSlice {
    let label: String
    let amount: Double
    let startAngle: Double
    let endAngle: Double
    let color: Color
}

struct PieChartView_Previews: PreviewProvider {
    static var previews: some View {
        PieChartView(
            budgets: [
                Budget(
                    category: Category(name: "Shopping", systemIcon: "bag.fill", isDefault: true),
                    limit: 200,
                    spent: 50
                ),
                Budget(
                    category: Category(name: "Travel", systemIcon: "airplane", isDefault: true),
                    limit: 150,
                    spent: 100
                ),
                Budget(
                    category: Category(name: "Restaurants", systemIcon: "fork.knife", isDefault: true),
                    limit: 100,
                    spent: 30
                )
            ],
            currencyCode: "GBP"
        )
        .frame(width: 300, height: 360)
        .previewLayout(.sizeThatFits)
    }
}