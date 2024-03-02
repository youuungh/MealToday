package com.example.mealtoday.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealtoday.data.Meal
import com.example.mealtoday.databinding.ItemSearchBinding
import eightbitlab.com.blurview.RenderEffectBlur

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    inner class SearchViewHolder(val binding: ItemSearchBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = differ.currentList.size

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val data = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(data.strMealThumb)
            .override(100, 100)
            .into(holder.binding.searchImage)

        Glide.with(holder.itemView)
            .load(data.strMealThumb)
            .override(300, 300)
            .into(holder.binding.searchBg)

        holder.apply {
            binding.blurView.setupWith(binding.root, RenderEffectBlur())
            binding.searchTitle.text = data.strMeal
        }
    }
}