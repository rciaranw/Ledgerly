package com.ledgerly

import android.app.Application
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ledgerly.ui.navigation.LedgerlyNavigation
import com.ledgerly.ui.screens.BiometricLockScreen
import com.ledgerly.ui.screens.BrandedSplashScreen
import com.ledgerly.ui.theme.LedgerlyTheme
import com.ledgerly.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay

class MainActivity : FragmentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        installSplashScreen()

        super.onCreate(
            savedInstanceState
        )

        enableEdgeToEdge()

        setContent {
            val application =
                applicationContext as Application

            val settingsViewModel:
                SettingsViewModel =
                viewModel(
                    factory =
                        ViewModelProvider
                            .AndroidViewModelFactory
                            .getInstance(
                                application
                            )
                )

            val settings =
                settingsViewModel.settings

            LedgerlyTheme(
                themeName =
                    settings.theme,
                textSize =
                    settings.textSize
            ) {
                var showSplash by remember {
                    mutableStateOf(true)
                }

                var appUnlocked by remember(
                    settings.biometricLockEnabled
                ) {
                    mutableStateOf(
                        !settings
                            .biometricLockEnabled
                    )
                }

                LaunchedEffect(Unit) {
                    delay(1200)
                    showSplash = false
                }

                when {
                    showSplash ->
                        BrandedSplashScreen()

                    settings
                        .biometricLockEnabled &&
                        !appUnlocked ->
                        BiometricLockScreen(
                            onUnlocked = {
                                appUnlocked = true
                            }
                        )

                    else ->
                        LedgerlyNavigation(
                            settingsViewModel =
                                settingsViewModel
                        )
                }
            }
        }
    }
}