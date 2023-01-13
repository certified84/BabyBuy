package com.certified.babybuy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.certified.babybuy.data.model.Item
import com.certified.babybuy.databinding.ItemCategoryLayoutBinding
import com.certified.babybuy.databinding.ItemLayoutBinding

class ItemRecyclerAdapter(private val which: String = "item") :
    ListAdapter<Item, RecyclerView.ViewHolder>(
        diffCallBack
    ) {

    private lateinit var listener: OnItemClickedListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding =
            ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemCategoryBinding =
            ItemCategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == 1) ItemViewHolder(itemBinding) else ItemCategoryViewHolder(
            itemCategoryBinding
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        when (holder) {
            is ItemViewHolder -> holder.bind(currentItem)
            is ItemCategoryViewHolder -> holder.bind(currentItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)
        return if (which == "category") 0 else 1
    }

    inner class ItemViewHolder(val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.item = item
        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position))
                }
            }
        }
    }

    inner class ItemCategoryViewHolder(val binding: ItemCategoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.item = item
        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position))
                }
            }
        }
    }

    interface OnItemClickedListener {
        fun onItemClick(item: Item)
    }

    fun setOnItemClickedListener(listener: OnItemClickedListener) {
        this.listener = listener
    }

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Item,
                newItem: Item
            ) = oldItem == newItem
        }
    }
}