import Foundation

struct Category: Identifiable, Hashable, Codable {

    // MARK: - Identity
    let id: UUID

    // MARK: - Display
    var name: String
    var systemIcon: String   // SF Symbol name

    // MARK: - Behaviour
    let isDefault: Bool      // Default categories cannot be edited/deleted

    // MARK: - Initialiser
    init(
        id: UUID = UUID(),
        name: String,
        systemIcon: String,
        isDefault: Bool = false
    ) {
        self.id = id
        self.name = name
        self.systemIcon = systemIcon
        self.isDefault = isDefault
    }
}
