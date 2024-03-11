package com.example.mealtoday.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mealtoday.model.Banner
import com.example.mealtoday.databinding.ItemBannerBinding

class BannerAdapter: RecyclerView.Adapter<BannerAdapter.ItemViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Banner>() {
        override fun areItemsTheSame(oldItem: Banner, newItem: Banner): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Banner, newItem: Banner): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    inner class ItemViewHolder(val binding: ItemBannerBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerAdapter.ItemViewHolder {
        return ItemViewHolder(ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BannerAdapter.ItemViewHolder, position: Int) {
        val data = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(data.strMealThumb)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.bannerImage)

        holder.apply {
            binding.bannerTitle.text = data.strMeal
        }
    }
}