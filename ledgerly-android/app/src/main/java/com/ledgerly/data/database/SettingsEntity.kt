package com.ledgerly.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "settings"
)
data class SettingsEntity(
    @PrimaryKey
    val id: String = "main_settings",
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