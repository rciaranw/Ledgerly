package com.ledgerly.data.database

import com.ledgerly.data.models.Settings

fun Settings.toEntity(): SettingsEntity {
    return SettingsEntity(
        currencyCode = this.currencyCode,
        weekStartDay = this.weekStartDay,
        monthStartDay = this.monthStartDay,
        carryOverEnabled = this.carryOverEnabled,
        theme = this.theme
    )
}

fun SettingsEntity.toModel(): Settings {
    return Settings(
        currencyCode = this.currencyCode,
        weekStartDay = this.weekStartDay,
        monthStartDay = this.monthStartDay,
        carryOverEnabled = this.carryOverEnabled,
        theme = this.theme
    )
}