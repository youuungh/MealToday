package com.example.mealtoday.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealtoday.databinding.ItemHotBinding
import com.example.mealtoday.db.Hot

class HotAdapter(): RecyclerView.Adapter<HotAdapter.HotViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Hot>() {
        override fun areItemsTheSame(oldItem: Hot, newItem: Hot): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Hot, newItem: Hot): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    inner class HotViewHolder(val binding: ItemHotBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotViewHolder {
        return HotViewHolder(ItemHotBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: HotViewHolder, position: Int) {
        val data = differ.currentList[position]
        Glide.with(holder.itemView)
            .load(data.strMealThumb)
            .into(holder.binding.hotImage)
    }
}