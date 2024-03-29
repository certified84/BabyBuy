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
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.certified.babybuy.R
import com.certified.babybuy.data.model.Category
import com.certified.babybuy.data.model.Item
import com.certified.babybuy.util.currentDate
import com.certified.customprogressindicatorlibrary.CustomProgressIndicator
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
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
fun MaterialCardView.backgroundColor(hex: String?) {
    hex?.let { setCardBackgroundColor(Color.parseColor(it)) }
}

@BindingAdapter("viewBackgroundColor")
fun View.viewBackgroundColor(hex: String?) {
    hex?.let { setBackgroundColor(Color.parseColor(it)) }
}

@BindingAdapter("itemCount")
fun MaterialTextView.itemCount(itemCount: Int?) {
    text = "$itemCount Items"
}

@BindingAdapter("progress")
fun MaterialTextView.progress(category: Category?) {
    category?.let {
        val progress = ((1.0 * it.purchasedCount) / it.itemCount) * 100.0
        Log.d("TAG", "progress: $progress")
        text = "${"%.2f".format(progress)} %"
    }
}

@BindingAdapter("percent")
fun LinearProgressIndicator.percent(category: Category?) {
    category?.let {
        val progress = ((1.0 * it.purchasedCount) / it.itemCount) * 100.0
        setProgress(progress.toInt())
    }
}

@BindingAdapter("reminder")
fun MaterialTextView.reminder(reminder: Long?) {
    text = reminder?.let {
        "Reminder: ${
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
        }"
    }
}

@BindingAdapter("category")
fun MaterialTextView.category(title: String?) {
    text = if (title == null || title == "") "Enter new category" else "Edit category"
}

@BindingAdapter("item")
fun MaterialTextView.item(title: String?) {
    text = if (title == null || title == "") "Enter new item" else "Edit item"
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
fun View.backgroundColor(color: String?) {
    color?.let { setBackgroundColor(Color.parseColor(it)) }
}

@BindingAdapter("editItemImage")
fun ImageView.editItemImage(imageUrl: String?) {
    if (imageUrl?.isBlank() == true || imageUrl == null)
        this.load(R.drawable.ic_onboarding_icon) {
            transformations(
                RoundedCornersTransformation(
                    20f
                )
            )
        }
    else
        this.load(imageUrl) {
            placeholder(R.drawable.ic_onboarding_icon)
            transformations(RoundedCornersTransformation(20f))
        }
}

@BindingAdapter("profileImage")
fun ImageView.profileImage(imageUrl: String?) {
    if (imageUrl?.isBlank() == true || imageUrl == null)
        this.load(R.drawable.ic_onboarding_icon) { transformations(CircleCropTransformation()) }
    else
        this.load(imageUrl) {
            placeholder(R.drawable.ic_onboarding_icon)
            transformations(CircleCropTransformation())
        }
}

@BindingAdapter("itemImage")
fun bindItemImageView(imageView: ImageView, imageUrl: String?) {
    if (imageUrl?.isBlank() == true || imageUrl == null)
        imageView.load(R.drawable.ic_onboarding_icon)
    else
        imageView.load(imageUrl) {
            placeholder(R.drawable.ic_onboarding_icon)
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
fun AppCompatImageButton.viewTint(color: String?) {
    color?.let { imageTintList = ColorStateList.valueOf(Color.parseColor(it)) }
}

@BindingAdapter("fabBackgroundColor")
fun FloatingActionButton.fabBackgroundColor(color: String?) {
    color?.let { backgroundTintList = ColorStateList.valueOf(Color.parseColor(it)) }
}

@BindingAdapter("extendedFabBackgroundColor")
fun ExtendedFloatingActionButton.extendedFabBackgroundColor(color: String?) {
    color?.let { backgroundTintList = ColorStateList.valueOf(Color.parseColor(it)) }
}