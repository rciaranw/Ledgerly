package com.ledgerly.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CategoryIcon(
    iconName: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = iconForName(iconName),
        contentDescription = contentDescription,
        modifier = modifier,
        tint = MaterialTheme.colorScheme.primary
    )
}

fun iconForName(
    iconName: String
): ImageVector {
    return when (iconName.lowercase()) {
        "salary" -> Icons.Filled.Work
        "work" -> Icons.Filled.Work
        "income" -> Icons.Filled.AttachMoney
        "cash" -> Icons.Filled.AttachMoney
        "wallet" -> Icons.Filled.AccountBalanceWallet

        "food" -> Icons.Filled.Fastfood
        "restaurant" -> Icons.Filled.Restaurant
        "restaurants" -> Icons.Filled.Restaurant
        "coffee" -> Icons.Filled.LocalCafe

        "shopping" -> Icons.Filled.ShoppingBag
        "gift" -> Icons.Filled.CardGiftcard

        "travel" -> Icons.Filled.Flight
        "flight" -> Icons.Filled.Flight
        "car" -> Icons.Filled.DirectionsCar
        "transport" -> Icons.Filled.Train
        "train" -> Icons.Filled.Train

        "home" -> Icons.Filled.Home
        "bills" -> Icons.Filled.ReceiptLong
        "utilities" -> Icons.Filled.ReceiptLong
        "health" -> Icons.Filled.LocalHospital
        "medical" -> Icons.Filled.LocalHospital

        "entertainment" -> Icons.Filled.Movie
        "pets" -> Icons.Filled.Pets
        "savings" -> Icons.Filled.Savings

        "category" -> Icons.Filled.Category
        "label" -> Icons.Filled.Label

        else -> Icons.Filled.Label
    }
}