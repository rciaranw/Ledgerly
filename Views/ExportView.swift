import SwiftUI
import UIKit

struct ExportView: View {

    @EnvironmentObject var transactionVM: TransactionViewModel
    @EnvironmentObject var settingsVM: SettingsViewModel

    @State private var showShareSheet = false
    @State private var csvURL: URL?

    var body: some View {
        NavigationView {
            VStack(spacing: 20) {
                Text("Export your transactions as CSV")
                    .font(.headline)
                    .foregroundColor(AppColors.primaryText)
                    .padding()

                Button {
                    generateCSV()
                } label: {
                    Text("Generate CSV")
                        .foregroundColor(AppColors.buttonText)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(AppColors.buttonBackground)
                        .cornerRadius(10)
                        .overlay(
                            RoundedRectangle(cornerRadius: 10)
                                .stroke(AppColors.primaryText, lineWidth: 1)
                        )
                }

                if csvURL != nil {
                    Button {
                        showShareSheet = true
                    } label: {
                        Text("Share CSV")
                            .foregroundColor(AppColors.buttonText)
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(AppColors.buttonBackground)
                            .cornerRadius(10)
                            .overlay(
                                RoundedRectangle(cornerRadius: 10)
                                    .stroke(AppColors.primaryText, lineWidth: 1)
                            )
                    }
                }

                Spacer()
            }
            .padding()
            .background(AppColors.background)
            .navigationTitle("Export Data")
            .sheet(isPresented: $showShareSheet) {
                if let csvURL {
                    ShareSheet(activityItems: [csvURL])
                }
            }
        }
    }

    // MARK: - Generate CSV and save to temporary file
    private func generateCSV() {
        let csvString = CSVExporter.export(
            transactions: transactionVM.transactions,
            currencyCode: settingsVM.settings.currencyCode
        )

        let tempURL = FileManager.default.temporaryDirectory
            .appendingPathComponent("LedgerlyTransactions.csv")

        do {
            try csvString.write(to: tempURL, atomically: true, encoding: .utf8)
            csvURL = tempURL
        } catch {
            print("Error writing CSV: \(error)")
        }
    }
}

// MARK: - Share Sheet Wrapper
struct ShareSheet: UIViewControllerRepresentable {

    let activityItems: [Any]
    let applicationActivities: [UIActivity]? = nil

    func makeUIViewController(context: Context) -> UIActivityViewController {
        UIActivityViewController(
            activityItems: activityItems,
            applicationActivities: applicationActivities
        )
    }

    func updateUIViewController(
        _ uiViewController: UIActivityViewController,
        context: Context
    ) {}
}

struct ExportView_Previews: PreviewProvider {
    static var previews: some View {
        ExportView()
            .environmentObject(TransactionViewModel())
            .environmentObject(SettingsViewModel())
    }
}