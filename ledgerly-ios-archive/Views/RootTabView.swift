import SwiftUI

struct RootTabView: View {

    // MARK: - Shared ViewModels
    @StateObject private var transactionVM = TransactionViewModel()
    @StateObject private var budgetVM = BudgetViewModel()
    @StateObject private var settingsVM = SettingsViewModel()
    @StateObject private var categoryVM = CategoryViewModel()

    // MARK: - Selected Tab
    @State private var selectedTab: Tab = .home

    enum Tab {
        case home
        case transactions
        case analysis
        case settings
    }

    var body: some View {
        TabView(selection: $selectedTab) {

            HomeView()
                .tabItem {
                    Label("Home", systemImage: "house.fill")
                }
                .tag(Tab.home)

            TransactionsView()
                .tabItem {
                    Label("Transactions", systemImage: "list.bullet")
                }
                .tag(Tab.transactions)

            AnalysisView()
                .tabItem {
                    Label("Analysis", systemImage: "chart.pie.fill")
                }
                .tag(Tab.analysis)

            SettingsView()
                .tabItem {
                    Label("Settings", systemImage: "gearshape.fill")
                }
                .tag(Tab.settings)
        }
        .accentColor(AppColors.accent)

        .environmentObject(transactionVM)
        .environmentObject(budgetVM)
        .environmentObject(settingsVM)
        .environmentObject(categoryVM)
    }
}

struct RootTabView_Previews: PreviewProvider {
    static var previews: some View {
        RootTabView()
    }
}