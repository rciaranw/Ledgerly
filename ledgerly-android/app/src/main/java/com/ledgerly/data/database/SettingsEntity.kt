package com.ledgerly.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey
    val id: String = "main_settings",
    val currencyCode: String,
    val weekStartDay: Int,
    val monthStartDay: Int,
    val carryOverEnabled: Boolean,
    val theme: String
)