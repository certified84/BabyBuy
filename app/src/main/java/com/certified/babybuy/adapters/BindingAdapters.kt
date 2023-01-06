package com.certified.babybuy.adapters

import android.view.View
import androidx.databinding.BindingAdapter
import com.certified.customprogressindicatorlibrary.CustomProgressIndicator

@BindingAdapter("visible")
fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("animate")
fun CustomProgressIndicator.animate(visible: Boolean) {
    if (visible) startAnimation() else stopAnimation()
}