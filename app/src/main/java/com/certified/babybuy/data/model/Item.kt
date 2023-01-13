package com.certified.babybuy.data.model

import android.os.Parcelable
import com.certified.babybuy.util.colors
import com.certified.babybuy.util.currentDate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val created: Long = currentDate().timeInMillis,
    val modified: Long = currentDate().timeInMillis,
    val image: String? = null,
    val delegate: Contact? = null,
    val purchased: Boolean = false,
    val location: Location? = null,
    val reminder: Long? = null,
    val categoryId: String? = "",
    val categoryTitle: String? = "",
    val hex: String? = colors.random(),
    val uid: String? = Firebase.auth.currentUser?.uid
) : Parcelable