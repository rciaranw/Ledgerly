import SwiftUI

struct PieSlice: View {

    let startAngle: Double
    let endAngle: Double
    let color: Color

    var body: some View {
        GeometryReader { geometry in
            Path { path in
                let rect = geometry.frame(in: .local)
                let centre = CGPoint(x: rect.midX, y: rect.midY)
                let radius = min(rect.width, rect.height) / 2

                path.move(to: centre)
                path.addArc(
                    center: centre,
                    radius: radius,
                    startAngle: .degrees(startAngle - 90),
                    endAngle: .degrees(endAngle - 90),
                    clockwise: false
                )
                path.closeSubpath()
            }
            .fill(color)
        }
    }
}

struct PieSlice_Previews: PreviewProvider {
    static var previews: some View {
        PieSlice(
            startAngle: 0,
            endAngle: 120,
            color: AppColors.income
        )
        .frame(width: 150, height: 150)
        .previewLayout(.sizeThatFits)
    }
}