package com.ledgerly.data.models

data class Category(
    val id: String,
    val name: String,
    val systemIcon: String,
    val isDefault: Boolean = false
)