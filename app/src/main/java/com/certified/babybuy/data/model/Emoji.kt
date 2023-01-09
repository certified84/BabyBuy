package com.certified.babybuy.data.model

data class Emoji(
    val emoji: String = "",
    val description: String = "",
    val category: String = "",
    val aliases: List<String> = listOf(),
    val tags: List<String> = listOf(),
    val unicode_version: String? = null,
    val ios_version: String? = null
)