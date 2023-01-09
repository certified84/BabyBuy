package com.certified.babybuy.data.model

import com.certified.babybuy.util.currentDate

data class Item(
    val id: String = "",
    val title: String = "",
    val created: Long = currentDate().timeInMillis,
    val modified: Long = currentDate().timeInMillis,
    val image: String = "",
    val delegate: Contact = Contact(),
    val purchased: Boolean = false,
    val location: Location? = null,
    val reminder: Long = 0L,
    val category: Category = Category()
)