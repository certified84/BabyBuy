package com.certified.babybuy.util

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

object Extensions {

    fun Fragment.showToast(message: String?) =
        message?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }

    fun Fragment.showSnackbar(message: String?, view: View? = null) =
        message?.let {
            Snackbar.make(view ?: this.requireView(), it, Snackbar.LENGTH_LONG)
                .setAction("Dismiss") { }
                .show()
        }
}