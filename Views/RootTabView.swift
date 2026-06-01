import SwiftUI

struct RootTabView: View {

    // MARK: - Selected Tab
    @State private var selectedTab: Tab = .home

    // MARK: - Tabs Enum
    enum Tab {
        case home, transactions, analysis, settings
    }

    var body: some View {
        TabView(selection: $selectedTab) {

            // MARK: - Home
            HomeView()
                .tabItem {
                    Label("Home", systemImage: "house.fill")
                }
                .tag(Tab.home)

            // MARK: - Transactions
            TransactionsView()
                .tabItem {
                    Label("Transactions", systemImage: "list.bullet")
                }
                .tag(Tab.transactions)

            // MARK: - Analysis
            AnalysisView()
                .tabItem {
                    Label("Analysis", systemImage: "chart.pie.fill")
                }
                .tag(Tab.analysis)

            // MARK: - Settings
            SettingsView()
                .tabItem {
                    Label("Settings", systemImage: "gearshape.fill")
                }
                .tag(Tab.settings)
        }
        .accentColor(AppColors.accent) // Turquoise main theme color
    }
}

struct RootTabView_Previews: PreviewProvider {
    static var previews: some View {
        RootTabView()
    }
}
