package com.ledgerly.data.models

data class Settings(
    val currencyCode: String = "GBP",
    val dateFormat: String = "DD/MM/YYYY",
    val weekStartDay: Int = 1,
    val monthStartDay: Int = 1,
    val carryOverEnabled: Boolean = false,
    val theme: String = "Default",
    val language: String = "English",
    val textSize: String = "Standard",
    val biometricLockEnabled: Boolean = false
)