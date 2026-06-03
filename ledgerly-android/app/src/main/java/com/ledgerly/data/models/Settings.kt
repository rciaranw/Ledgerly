package com.ledgerly.data.models

data class Settings(
    val currencyCode: String = "GBP",
    val weekStartDay: Int = 1,
    val monthStartDay: Int = 1,
    val carryOverEnabled: Boolean = false,
    val theme: String = "Turquoise"
)