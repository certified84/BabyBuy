package com.certified.babybuy.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Emoji(
    val emoji: String = "",
    val description: String = "",
    val category: String = "",
    val aliases: List<String> = listOf(),
    val tags: List<String> = listOf(),
    val unicode_version: String? = null,
    val ios_version: String? = null
): Parcelable