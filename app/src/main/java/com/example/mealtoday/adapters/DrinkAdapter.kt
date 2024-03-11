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
import com.example.mealtoday.model.Drink
import com.example.mealtoday.databinding.ItemDrinkBinding

class DrinkAdapter: RecyclerView.Adapter<DrinkAdapter.ItemViewHolder>() {

    lateinit var onDrinkItemClick: ((Drink) -> Unit)

    private val diffUtil = object : DiffUtil.ItemCallback<Drink>() {
        override fun areItemsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem.idDrink == newItem.idDrink
        }

        override fun areContentsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    inner class ItemViewHolder(val binding: ItemDrinkBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemDrinkBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(data.strDrinkThumb)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.drinkImage)

        holder.apply {
            binding.drinkTitle.text = data.strDrink
            binding.alcoholic.text = data.strAlcoholic
            binding.glass.text = data.strGlass

            itemView.setOnClickListener {
                onDrinkItemClick.invoke(data)
            }
        }
    }

}