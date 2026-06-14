package com.ledgerly.ui.screens

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.ledgerly.security.BiometricLockManager

@Composable
fun BiometricLockScreen(
    onUnlocked: () -> Unit
) {
    val context =
        LocalContext.current

    val activity =
        context as? FragmentActivity

    val lockManager =
        remember(context) {
            BiometricLockManager(
                context
            )
        }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var promptShown by remember {
        mutableStateOf(false)
    }

    fun showPrompt() {
        if (activity == null) {
            errorMessage =
                "Device authentication is unavailable."

            return
        }

        if (!lockManager.canAuthenticate()) {
            errorMessage =
                "No supported device authentication is configured."

            return
        }

        val executor =
            androidx.core.content.ContextCompat
                .getMainExecutor(
                    context
                )

        val biometricPrompt =
            BiometricPrompt(
                activity,
                executor,
                object :
                    BiometricPrompt
                        .AuthenticationCallback() {

                    override fun onAuthenticationSucceeded(
                        result:
                            BiometricPrompt
                                .AuthenticationResult
                    ) {
                        super
                            .onAuthenticationSucceeded(
                                result
                            )

                        errorMessage = null
                        onUnlocked()
                    }

                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence
                    ) {
                        super
                            .onAuthenticationError(
                                errorCode,
                                errString
                            )

                        if (
                            errorCode !=
                            BiometricPrompt.ERROR_USER_CANCELED &&
                            errorCode !=
                            BiometricPrompt.ERROR_NEGATIVE_BUTTON
                        ) {
                            errorMessage =
                                errString.toString()
                        }
                    }

                    override fun onAuthenticationFailed() {
                        super
                            .onAuthenticationFailed()

                        errorMessage =
                            "Authentication was not recognised."
                    }
                }
            )

        val promptInfo =
            BiometricPrompt.PromptInfo
                .Builder()
                .setTitle(
                    "Unlock Ledgerly"
                )
                .setSubtitle(
                    "Authenticate to access your financial data"
                )
                .setAllowedAuthenticators(
                    BiometricManager
                        .Authenticators
                        .BIOMETRIC_STRONG or
                        BiometricManager
                            .Authenticators
                            .DEVICE_CREDENTIAL
                )
                .build()

        biometricPrompt.authenticate(
            promptInfo
        )
    }

    LaunchedEffect(Unit) {
        if (!promptShown) {
            promptShown = true
            showPrompt()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {
        Text(
            text = "Ledgerly Locked",
            style =
                MaterialTheme.typography.headlineMedium,
            fontWeight =
                FontWeight.Bold
        )

        Text(
            text =
                "Authenticate to continue.",
            modifier =
                Modifier.padding(
                    top = 8.dp,
                    bottom = 18.dp
                ),
            style =
                MaterialTheme.typography.bodyMedium
        )

        errorMessage?.let { message ->
            Text(
                text =
                    message,
                color =
                    MaterialTheme.colorScheme.error,
                modifier =
                    Modifier.padding(
                        bottom = 14.dp
                    )
            )
        }

        Button(
            onClick = {
                showPrompt()
            }
        ) {
            Text(
                text =
                    "Unlock"
            )
        }
    }
}