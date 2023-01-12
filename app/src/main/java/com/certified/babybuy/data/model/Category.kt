package com.certified.babybuy.data.model

import android.os.Parcelable
import com.certified.babybuy.util.colors
import com.certified.babybuy.util.currentDate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: String = "",
    val title: String = "",
    val desc: String = "",
    val hex: String = colors.random(),
    val itemCount: Int = 0,
    val purchasedCount: Int = 0,
    val emoji: Emoji? = null,
    var _hex: String = "",
    val created: Long = currentDate().timeInMillis,
    val uid: String? = Firebase.auth.currentUser?.uid
) : Parcelable {
    init {
        _hex = "#B3${hex.substringAfter("#")}"
    }
}