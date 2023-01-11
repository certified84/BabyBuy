package com.certified.babybuy.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: String = "",
    val title: String = "",
    val desc: String = "",
    val hex: String = "",
    val itemCount: Int = 0,
    val purchasedCount: Int = 0,
    val emoji: Emoji? = null,
    var _hex: String = ""
) : Parcelable {
    init {
        _hex = "#B3${hex.substringAfter("#")}"
    }
}