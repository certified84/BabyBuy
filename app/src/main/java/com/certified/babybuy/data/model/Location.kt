package com.certified.babybuy.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val name: String? = "",
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0
) : Parcelable