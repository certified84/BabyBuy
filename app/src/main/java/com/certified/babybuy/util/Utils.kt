package com.certified.babybuy.util

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import java.util.regex.Pattern

fun verifyPassword(
    password: String,
    editText: TextInputEditText,
    editTextLayout: TextInputLayout
): Boolean {

    if (password.length < 8) {
        editTextLayout.error = "Minimum of 8 characters"
        editText.requestFocus()
        return false
    }

    var numberFlag = false
    var upperCaseFlag = false

    for (i in password.indices) {
        val ch = password[i]
        when {
            ch.isUpperCase() -> {
                upperCaseFlag = true
            }
            ch.isDigit() -> {
                numberFlag = true
            }
        }
    }

    if (!upperCaseFlag) {
        editTextLayout.error = "Uppercase letter required* "
        editText.requestFocus()
        return false
    }

    val pattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE)
    if (!pattern.matcher(password).find()) {
        editTextLayout.error = "Special character required* "
        editText.requestFocus()
        return false
    }

    if (!numberFlag) {
        editTextLayout.error = "Number required* "
        editText.requestFocus()
        return false
    }

    return true
}

fun currentDate(): Calendar = Calendar.getInstance()