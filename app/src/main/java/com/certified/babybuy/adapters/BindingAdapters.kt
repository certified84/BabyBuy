package com.certified.babybuy.adapters

import android.graphics.Color
import android.view.View
import androidx.databinding.BindingAdapter
import com.certified.babybuy.data.model.Category
import com.certified.customprogressindicatorlibrary.CustomProgressIndicator
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textview.MaterialTextView

@BindingAdapter("visible")
fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("animate")
fun CustomProgressIndicator.animate(visible: Boolean) {
    if (visible) startAnimation() else stopAnimation()
}

@BindingAdapter("backgroundColor")
fun MaterialCardView.backgroundColor(hex: String) {
    setCardBackgroundColor(Color.parseColor(hex))
}

@BindingAdapter("itemCount")
fun MaterialTextView.itemCount(itemCount: Int) {
    text = "$itemCount Items"
}

@BindingAdapter("progress")
fun MaterialTextView.progress(category: Category) {
    val progress = (category.purchasedCount / category.itemCount) * 100.0
    text = "$progress %"
}

@BindingAdapter("progress")
fun LinearProgressIndicator.progress(category: Category) {
    val progress = (category.purchasedCount / category.itemCount) * 100.0
    setProgress(progress.toInt())
}