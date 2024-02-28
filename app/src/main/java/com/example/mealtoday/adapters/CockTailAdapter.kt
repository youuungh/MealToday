package com.example.mealtoday.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealtoday.data.Drink
import com.example.mealtoday.databinding.ItemCocktailBinding
import com.google.android.material.animation.AnimationUtils.lerp

class CockTailAdapter: RecyclerView.Adapter<CockTailAdapter.ItemViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Drink>() {
        override fun areItemsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem.idDrink == newItem.idDrink
        }

        override fun areContentsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    inner class ItemViewHolder(val binding: ItemCocktailBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemCocktailBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = differ.currentList.size

    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(data.strDrinkThumb)
            .into(holder.binding.cocktailImage)

        holder.apply {
            holder.binding.cocktailTitle.text = data.strDrink

            holder.binding.root.setOnMaskChangedListener { maskRect ->
                binding.cocktailTitle.translationX = maskRect.left
                binding.cocktailTitle.alpha = lerp(1F, 0F, 0F, 80F, maskRect.left)
            }
        }
    }
}