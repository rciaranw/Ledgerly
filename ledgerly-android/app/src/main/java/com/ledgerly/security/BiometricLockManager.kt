package com.ledgerly.security

import android.content.Context
import androidx.biometric.BiometricManager

class BiometricLockManager(
    private val context: Context
) {

    fun canAuthenticate(): Boolean {
        val biometricManager =
            BiometricManager.from(
                context
            )

        val authenticators =
            BiometricManager.Authenticators
                .BIOMETRIC_STRONG or
                BiometricManager.Authenticators
                    .DEVICE_CREDENTIAL

        return biometricManager
            .canAuthenticate(
                authenticators
            ) ==
            BiometricManager
                .BIOMETRIC_SUCCESS
    }
}