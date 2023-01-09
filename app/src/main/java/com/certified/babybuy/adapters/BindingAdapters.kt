package com.certified.babybuy.adapters

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.certified.babybuy.data.model.Category
import com.certified.babybuy.data.model.Item
import com.certified.customprogressindicatorlibrary.CustomProgressIndicator
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.*

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
    Log.d("TAG", "progress: $progress")
    text = "$progress %"
}

@BindingAdapter("percent")
fun LinearProgressIndicator.percent(category: Category) {
    val progress = (category.purchasedCount / category.itemCount) * 100.0
    setProgress(progress.toInt())
}

@BindingAdapter("reminder")
fun MaterialTextView.reminder(reminder: Long?) {
    text = reminder?.let {
        "Reminder: ${
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(reminder)
        }"
    }
}

@BindingAdapter("listItems")
fun bindItemRecyclerView(recyclerView: RecyclerView, data: List<Item>?) {
    val adapter = recyclerView.adapter as ItemRecyclerAdapter
    adapter.submitList(data)
}

@BindingAdapter("listCategories")
fun bindCategoryRecyclerView(recyclerView: RecyclerView, data: List<Category>?) {
    val adapter = recyclerView.adapter as CategoryRecyclerAdapter
    adapter.submitList(data)
}

@BindingAdapter("color")
fun View.backgroundColor(color: String) {
    setBackgroundColor(Color.parseColor(color))
}