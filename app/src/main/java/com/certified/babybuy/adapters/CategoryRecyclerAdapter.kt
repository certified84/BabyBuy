package com.certified.babybuy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.certified.babybuy.data.model.Category
import com.certified.babybuy.databinding.ItemCategoryBinding

class CategoryRecyclerAdapter :
    ListAdapter<Category, CategoryRecyclerAdapter.ViewHolder>(diffCallBack) {

    private lateinit var listener: OnItemClickedListener

    inner class ViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.category = category
        }

        init {
            itemView.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position))
                }
            }
        }
    }

    interface OnItemClickedListener {
        fun onItemClick(category: Category)
    }

    fun setOnItemClickedListener(listener: OnItemClickedListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Category,
                newItem: Category
            ) = oldItem == newItem
        }
    }
}