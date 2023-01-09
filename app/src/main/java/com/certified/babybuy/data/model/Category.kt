package com.certified.babybuy.data.model

data class Category(
    val title: String = "",
    val desc: String = "",
    val hex: String = "",
    val itemCount: Int = 0,
    val purchasedCount: Int = 0,
    val emoji: Emoji? = null
)