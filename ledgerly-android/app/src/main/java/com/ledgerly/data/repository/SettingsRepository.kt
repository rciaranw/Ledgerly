package com.ledgerly.data.repository

import com.ledgerly.data.database.SettingsDao
import com.ledgerly.data.database.SettingsEntity
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
    private val settingsDao: SettingsDao
) {
    val settings: Flow<SettingsEntity?> =
        settingsDao.getSettings()

    suspend fun saveSettings(
        settings: SettingsEntity
    ) {
        settingsDao.saveSettings(settings)
    }
}