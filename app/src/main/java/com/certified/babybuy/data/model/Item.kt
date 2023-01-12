package com.certified.babybuy.data.model

import com.certified.babybuy.util.currentDate
import android.os.Parcelable
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: String = "",
    val name: String = "",
    val created: Long = currentDate().timeInMillis,
    val modified: Long = currentDate().timeInMillis,
    val image: String? = null,
    val delegate: Contact = Contact(),
    val purchased: Boolean = false,
    val location: Location? = null,
    val reminder: Long? = null,
    val category: Category = Category(),
    val uid: String? = Firebase.auth.currentUser?.uid
) : Parcelable