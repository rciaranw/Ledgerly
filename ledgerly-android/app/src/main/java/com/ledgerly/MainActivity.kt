package com.ledgerly

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ledgerly.ui.navigation.LedgerlyNavigation
import com.ledgerly.ui.screens.BrandedSplashScreen
import com.ledgerly.ui.theme.LedgerlyTheme
import com.ledgerly.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val application =
                applicationContext as Application

            val settingsViewModel: SettingsViewModel =
                viewModel(
                    factory = ViewModelProvider
                        .AndroidViewModelFactory
                        .getInstance(application)
                )

            val settings = settingsViewModel.settings

            LedgerlyTheme(
                themeName = settings.theme
            ) {
                var showSplash by remember {
                    mutableStateOf(true)
                }

                LaunchedEffect(Unit) {
                    delay(1200)
                    showSplash = false
                }

                if (showSplash) {
                    BrandedSplashScreen()
                } else {
                    LedgerlyNavigation()
                }
            }
        }
    }
}