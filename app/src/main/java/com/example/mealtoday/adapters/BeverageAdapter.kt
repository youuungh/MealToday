package com.example.mealtoday.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.mealtoday.R
import com.example.mealtoday.model.Beverage
import com.example.mealtoday.databinding.ItemBeverageBinding

class BeverageAdapter: RecyclerView.Adapter<BeverageAdapter.ItemViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Beverage>() {
        override fun areItemsTheSame(oldItem: Beverage, newItem: Beverage): Boolean {
            return oldItem.idDrink == newItem.idDrink
        }

        override fun areContentsTheSame(oldItem: Beverage, newItem: Beverage): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    inner class ItemViewHolder(val binding: ItemBeverageBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemBeverageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(data.strDrinkThumb)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.beverageImage)

        holder.apply {
            binding.beverageTitle.text = data.strDrink
        }
    }

}