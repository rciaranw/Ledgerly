import SwiftUI

struct ExportView: View {

    @EnvironmentObject var transactionVM: TransactionViewModel
    @EnvironmentObject var settingsVM: SettingsViewModel

    @State private var showShareSheet = false
    @State private var csvData: URL?

    var body: some View {
        NavigationView {
            VStack(spacing: 20) {
                Text("Export your transactions as CSV")
                    .font(.headline)
                    .padding()

                Button(action: { generateCSV() }) {
                    Text("Generate CSV")
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(AppColors.accent)
                        .cornerRadius(10)
                }

                if csvData != nil {
                    Button(action: { showShareSheet = true }) {
                        Text("Share CSV")
                            .foregroundColor(.white)
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(AppColors.accent)
                            .cornerRadius(10)
                    }
                    .sheet(isPresented: $showShareSheet) {
                        if let csvData = csvData {
                            ShareSheet(activityItems: [csvData])
                        }
                    }
                }

                Spacer()
            }
            .padding()
            .navigationTitle("Export Data")
        }
    }

    // MARK: - Generate CSV and save to temporary file
    private func generateCSV() {
        let csvString = CSVExporter.export(transactions: transactionVM.transactions,
                                           currencyCode: settingsVM.settings.currencyCode)

        let tempURL = FileManager.default.temporaryDirectory.appendingPathComponent("Transactions.csv")
        do {
            try csvString.write(to: tempURL, atomically: true, encoding: .utf8)
            csvData = tempURL
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
        UIActivityViewController(activityItems: activityItems,
                                 applicationActivities: applicationActivities)
    }

    func updateUIViewController(_ uiViewController: UIActivityViewController, context: Context) {}
}

struct ExportView_Previews: PreviewProvider {
    static var previews: some View {
        ExportView()
            .environmentObject(TransactionViewModel())
            .environmentObject(SettingsViewModel())
    }
}
