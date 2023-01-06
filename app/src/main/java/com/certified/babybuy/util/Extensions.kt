package com.certified.babybuy.util

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

object Extensions {
//    fun TextInputEditText.checkFieldEmpty(): Boolean {
//        return if (this.text.toString().isBlank()) {
//            with(this) {
//                error = "Required *"
//                requestFocus()
//            }
//            true
//        } else
//            false
//    }

    fun Fragment.showToast(message: String?) =
        message?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }

    fun Fragment.showSnackbar(message: String?, view: View? = null) =
        message?.let {
            Snackbar.make(view ?: this.requireView(), it, Snackbar.LENGTH_LONG)
                .setAction("Dismiss") { }
                .show()
        }
}