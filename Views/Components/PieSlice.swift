import SwiftUI

struct PieSlice: View {

    let value: Double
    let label: String
    let color: Color

    var body: some View {
        GeometryReader { geometry in
            ZStack {
                Circle()
                    .trim(from: 0, to: CGFloat(valueFraction))
                    .stroke(color, lineWidth: geometry.size.width / 2)
                    .rotationEffect(.degrees(-90))

                Text(label)
                    .font(.caption)
                    .foregroundColor(.primary)
                    .position(x: geometry.size.width / 2, y: geometry.size.height / 2)
            }
        }
    }

    // MARK: - Fraction of total
    private var valueFraction: Double {
        guard total > 0 else { return 0 }
        return value / total
    }

    // MARK: - Static total tracker (simplistic approach)
    private var total: Double {
        // For now, you can sum Income + Expense in AnalysisView
        1.0 // Placeholder, actual fraction calculated externally in AnalysisView
    }
}

struct PieSlice_Previews: PreviewProvider {
    static var previews: some View {
        PieSlice(value: 500, label: "Income", color: AppColors.income)
            .frame(width: 150, height: 150)
            .previewLayout(.sizeThatFits)
    }
}
