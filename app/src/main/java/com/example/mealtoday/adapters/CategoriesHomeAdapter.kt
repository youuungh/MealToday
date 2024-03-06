package com.example.mealtoday.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.mealtoday.R
import com.example.mealtoday.databinding.ItemCategoryBinding
import com.example.mealtoday.model.Category

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
            .override(80, 80)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.categoryImage)

        holder.apply {
            binding.tvCategoryTitle.text = data.strCategory

            itemView.setOnClickListener {
                onCategoryItemClick.invoke(data)
            }
        }
    }
}