package com.certified.babybuy.data.model

import com.certified.babybuy.util.currentDate
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: String = "",
    val name: String = "",
    val created: Long = currentDate().timeInMillis,
    val modified: Long = currentDate().timeInMillis,
    val image: String = "",
    val delegate: Contact = Contact(),
    val purchased: Boolean = false,
    val location: Location? = null,
    val reminder: Long? = null,
    val category: Category = Category()
) : Parcelable