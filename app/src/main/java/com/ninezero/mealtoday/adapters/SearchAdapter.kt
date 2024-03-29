package com.ninezero.mealtoday.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ninezero.mealtoday.model.Meal
import com.ninezero.mealtoday.databinding.ItemSearchBinding
import eightbitlab.com.blurview.RenderEffectBlur

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    lateinit var onSearchItemClick: ((Meal) -> Unit)

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
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.searchImage)

        Glide.with(holder.itemView)
            .load(data.strMealThumb)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.searchBg)

        holder.apply {
            binding.blurView.setupWith(binding.container, RenderEffectBlur())
            binding.searchTitle.text = data.strMeal

            itemView.setOnClickListener {
                onSearchItemClick.invoke(data)
            }
        }
    }
}