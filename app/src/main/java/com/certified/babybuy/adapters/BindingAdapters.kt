package com.certified.babybuy.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.certified.babybuy.R
import com.certified.babybuy.data.model.Category
import com.certified.babybuy.data.model.Item
import com.certified.babybuy.util.currentDate
import com.certified.customprogressindicatorlibrary.CustomProgressIndicator
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("visible")
fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("invisible")
fun View.invisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("animate")
fun CustomProgressIndicator.animate(visible: Boolean) {
    if (visible) startAnimation() else stopAnimation()
}

@BindingAdapter("backgroundColor")
fun MaterialCardView.backgroundColor(hex: String) {
    setCardBackgroundColor(Color.parseColor(hex))
}

@BindingAdapter("viewBackgroundColor")
fun View.viewBackgroundColor(hex: String) {
    setBackgroundColor(Color.parseColor(hex))
}

@BindingAdapter("itemCount")
fun MaterialTextView.itemCount(itemCount: Int) {
    text = "$itemCount Items"
}

@BindingAdapter("progress")
fun MaterialTextView.progress(category: Category) {
    val progress = ((1.0 * category.purchasedCount) / category.itemCount) * 100.0
    Log.d("TAG", "progress: $progress")
    text = "${"%.2f".format(progress)} %"
}

@BindingAdapter("percent")
fun LinearProgressIndicator.percent(category: Category) {
    val progress = ((1.0 * category.purchasedCount) / category.itemCount) * 100.0
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

@BindingAdapter("category")
fun MaterialTextView.category(title: String) {
    text = if (title == "") "Enter new category" else "Edit category"
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

@BindingAdapter("editItemImage")
fun bindEditItemImageView(imageView: ImageView, imageUrl: String?) {
    if (imageUrl?.isBlank() == true || imageUrl == null)
        imageView.load(R.drawable.ic_onboarding_icon) {
            transformations(
                RoundedCornersTransformation(
                    20f
                )
            )
        }
    else
        imageView.load(imageUrl) {
            placeholder(R.drawable.ic_onboarding_icon)
            transformations(RoundedCornersTransformation(20f))
        }
}

@BindingAdapter("reminderText")
fun Chip.reminderText(reminder: Long?) {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    text = if (reminder == null) "${formatter.format(currentDate().timeInMillis)}" else "${
        formatter.format(reminder)
    }"
}

@BindingAdapter("alpha")
fun View.alpha(state: Boolean) {
    alpha = if (state) .4f else 1f
}

@BindingAdapter("viewTint")
fun AppCompatImageButton.viewTint(color: String) {
    imageTintList = ColorStateList.valueOf(Color.parseColor(color))
}

@BindingAdapter("fabBackgroundColor")
fun FloatingActionButton.fabBackgroundColor(color: String) {
    backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))
}