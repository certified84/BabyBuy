package com.certified.babybuy.util

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

    fun Fragment.showActionDialog(title: String, message: String, action: (() -> Unit)?) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
                action?.invoke()
            }
            .setCancelable(false)
            .show()
    }

    fun Fragment.showYesNoDialog(title: String, message: String, action: (() -> Unit)?) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                action?.invoke()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }
}