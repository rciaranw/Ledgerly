import Foundation
import SwiftUI

class CategoryViewModel: ObservableObject {

    // MARK: - Default Categories
    let defaultCategories: [Category] = DefaultCategories.all

    // MARK: - User Categories
    @Published var userCategories: [Category] = []

    // MARK: - Combined Categories
    var allCategories: [Category] {
        defaultCategories + userCategories
    }

    // MARK: - Add Custom Category
    func addUserCategory(name: String, systemIcon: String = "tag.fill") {
        let trimmedName = name.trimmingCharacters(in: .whitespacesAndNewlines)

        guard !trimmedName.isEmpty else {
            return
        }

        let alreadyExists = allCategories.contains {
            $0.name.lowercased() == trimmedName.lowercased()
        }

        guard !alreadyExists else {
            return
        }

        let category = Category(
            name: trimmedName,
            systemIcon: systemIcon,
            isDefault: false
        )

        userCategories.append(category)
    }

    // MARK: - Delete Custom Category
    func deleteUserCategory(at offsets: IndexSet) {
        userCategories.remove(atOffsets: offsets)
    }
}