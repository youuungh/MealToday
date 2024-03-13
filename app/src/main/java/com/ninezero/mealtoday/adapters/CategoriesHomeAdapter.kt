package com.ninezero.mealtoday.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ninezero.mealtoday.databinding.ItemCategoryBinding
import com.ninezero.mealtoday.model.Category

class CategoriesHomeAdapter: RecyclerView.Adapter<CategoriesHomeAdapter.ItemViewHolder>() {

    lateinit var onCategoryItemClick: ((Category) -> Unit)

    private val diffUtil = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.idCategory == newItem.idCategory
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    inner class ItemViewHolder(val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(data.strCategoryThumb)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.categoryImage)

        holder.apply {
            binding.categoryTitle.text = data.strCategory

            itemView.setOnClickListener {
                onCategoryItemClick.invoke(data)
            }
        }
    }
}