package com.ledgerly.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ledgerly.data.database.DatabaseProvider
import com.ledgerly.data.database.toEntity
import com.ledgerly.data.database.toModel
import com.ledgerly.data.models.Settings
import com.ledgerly.data.repository.SettingsRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository:
        SettingsRepository

    var settings by mutableStateOf(
        Settings()
    )
        private set

    init {
        val database =
            DatabaseProvider.getDatabase(
                application
            )

        repository =
            SettingsRepository(
                settingsDao =
                    database.settingsDao()
            )

        viewModelScope.launch {
            repository.settings.collect { entity ->
                if (entity != null) {
                    settings =
                        entity.toModel()
                }
            }
        }
    }

    private fun saveSettings() {
        viewModelScope.launch {
            repository.saveSettings(
                settings.toEntity()
            )
        }
    }

    fun setCurrency(
        currencyCode: String
    ) {
        settings =
            settings.copy(
                currencyCode =
                    currencyCode
            )

        saveSettings()
    }

    fun setDateFormat(
        dateFormat: String
    ) {
        settings =
            settings.copy(
                dateFormat =
                    dateFormat
            )

        saveSettings()
    }

    fun setWeekStartDay(
        day: Int
    ) {
        if (day !in 1..7) {
            return
        }

        settings =
            settings.copy(
                weekStartDay =
                    day
            )

        saveSettings()
    }

    fun setMonthStartDay(
        day: Int
    ) {
        if (day !in 1..31) {
            return
        }

        settings =
            settings.copy(
                monthStartDay =
                    day
            )

        saveSettings()
    }

    fun setCarryOverEnabled(
        enabled: Boolean
    ) {
        settings =
            settings.copy(
                carryOverEnabled =
                    enabled
            )

        saveSettings()
    }

    fun toggleCarryOver() {
        settings =
            settings.copy(
                carryOverEnabled =
                    !settings
                        .carryOverEnabled
            )

        saveSettings()
    }

    fun setTheme(
        theme: String
    ) {
        settings =
            settings.copy(
                theme =
                    theme
            )

        saveSettings()
    }

    fun setLanguage(
        language: String
    ) {
        settings =
            settings.copy(
                language =
                    language
            )

        saveSettings()
    }

    fun setTextSize(
        textSize: String
    ) {
        settings =
            settings.copy(
                textSize =
                    textSize
            )

        saveSettings()
    }

    fun setBiometricLockEnabled(
        enabled: Boolean
    ) {
        settings =
            settings.copy(
                biometricLockEnabled =
                    enabled
            )

        saveSettings()
    }
}