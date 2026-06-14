package com.ledgerly.data.database

import com.ledgerly.data.models.Settings

fun SettingsEntity.toModel(): Settings {
    return Settings(
        currencyCode =
            currencyCode,
        dateFormat =
            dateFormat,
        weekStartDay =
            weekStartDay,
        monthStartDay =
            monthStartDay,
        carryOverEnabled =
            carryOverEnabled,
        theme =
            theme,
        language =
            language,
        textSize =
            textSize,
        biometricLockEnabled =
            biometricLockEnabled
    )
}

fun Settings.toEntity(): SettingsEntity {
    return SettingsEntity(
        id =
            "main_settings",
        currencyCode =
            currencyCode,
        dateFormat =
            dateFormat,
        weekStartDay =
            weekStartDay,
        monthStartDay =
            monthStartDay,
        carryOverEnabled =
            carryOverEnabled,
        theme =
            theme,
        language =
            language,
        textSize =
            textSize,
        biometricLockEnabled =
            biometricLockEnabled
    )
}