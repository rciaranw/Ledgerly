package com.ledgerly.utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyFormatter {

    fun format(
        amount: Double,
        currencyCode: String = "GBP"
    ): String {

        val formatter = NumberFormat.getCurrencyInstance()

        formatter.currency =
            Currency.getInstance(currencyCode)

        return formatter.format(amount)
    }
}