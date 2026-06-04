package com.ledgerly.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VolunteerActivism
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
        "cash",
        "payments" ->
            Icons.Filled.Payments

        "cashback",
        "undo",
        "refund",
        "assignment_return" ->
            Icons.Filled.ReceiptLong

        "charity",
        "volunteer_activism" ->
            Icons.Filled.VolunteerActivism

        "child_allowance",
        "child_care" ->
            Icons.Filled.ChildCare

        "credit",
        "credit_card" ->
            Icons.Filled.CreditCard

        "entertainment",
        "movie" ->
            Icons.Filled.Movie

        "gambling",
        "casino" ->
            Icons.Filled.Paid

        "general",
        "category" ->
            Icons.Filled.Category

        "label" ->
            Icons.Filled.Label

        "gift",
        "redeem" ->
            Icons.Filled.CardGiftcard

        "health",
        "medical",
        "medical_services" ->
            Icons.Filled.LocalHospital

        "insurance",
        "shield" ->
            Icons.Filled.Security

        "interest",
        "percent" ->
            Icons.Filled.AttachMoney

        "investment",
        "trending_up" ->
            Icons.Filled.TrendingUp

        "loan",
        "account_balance" ->
            Icons.Filled.AccountBalance

        "net_sales",
        "point_of_sale",
        "income" ->
            Icons.Filled.AttachMoney

        "remittances",
        "sync_alt" ->
            Icons.Filled.SyncAlt

        "restaurants",
        "restaurant",
        "food" ->
            Icons.Filled.Restaurant

        "salary",
        "work" ->
            Icons.Filled.Work

        "savings" ->
            Icons.Filled.Savings

        "shopping",
        "shopping_bag" ->
            Icons.Filled.ShoppingBag

        "top_ups",
        "add_circle" ->
            Icons.Filled.AddCircle

        "transfers",
        "swap_horiz",
        "transport" ->
            Icons.Filled.SyncAlt

        "travel",
        "flight" ->
            Icons.Filled.Flight

        "utilities",
        "bolt",
        "bills" ->
            Icons.Filled.Bolt

        "wallet" ->
            Icons.Filled.AccountBalanceWallet

        "coffee" ->
            Icons.Filled.LocalCafe

        "car" ->
            Icons.Filled.DirectionsCar

        "train" ->
            Icons.Filled.Train

        "home" ->
            Icons.Filled.Home

        "pets" ->
            Icons.Filled.Pets

        else ->
            Icons.Filled.Label
    }
}