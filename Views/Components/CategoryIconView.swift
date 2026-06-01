import SwiftUI

struct CategoryIconView: View {

    let category: Category
    let size: CGFloat

    var body: some View {
        Image(systemName: category.systemIcon)
            .resizable()
            .scaledToFit()
            .frame(width: size, height: size)
            .padding(6)
            .background(AppColors.iconBackground)
            .foregroundColor(AppColors.accent)
            .cornerRadius(size / 4)
    }
}

struct CategoryIconView_Previews: PreviewProvider {
    static var previews: some View {
        CategoryIconView(
            category: Category(name: "Shopping", systemIcon: "bag.fill", isDefault: true),
            size: 40
        )
        .previewLayout(.sizeThatFits)
    }
}
