import SwiftUI

struct TransactionRow: View {

    let transaction: Transaction
    let currencyCode: String

    var body: some View {
        HStack {
            Image(systemName: transaction.category.systemIcon)
                .foregroundColor(AppColors.accent)
                .frame(width: 30)

            VStack(alignment: .leading) {
                Text(transaction.title)
                    .fontWeight(.medium)
                Text(DateHelper.formatDate(transaction.date))
                    .font(.caption)
                    .foregroundColor(AppColors.secondaryText)
            }

            Spacer()

            Text(CurrencyFormatter.format(amount: transaction.amount,
                                          currencyCode: currencyCode))
                .foregroundColor(transaction.isIncome ? AppColors.income : AppColors.expense)
        }
        .padding(.vertical, 4)
    }
}

struct TransactionRow_Previews: PreviewProvider {
    static var previews: some View {
        TransactionRow(
            transaction: Transaction(
                title: "Tesco",
                notes: "Groceries",
                amount: 35.50,
                date: Date(),
                category: Category(name: "Shopping", systemIcon: "bag.fill", isDefault: true),
                isIncome: false,
                recurringRule: nil
            ),
            currencyCode: "GBP"
        )
        .previewLayout(.sizeThatFits)
    }
}
