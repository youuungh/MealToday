package com.ninezero.mealtoday.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ninezero.mealtoday.model.Cocktail
import com.ninezero.mealtoday.databinding.ItemCocktailBinding
import com.google.android.material.animation.AnimationUtils.lerp

class CockTailAdapter: RecyclerView.Adapter<CockTailAdapter.ItemViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Cocktail>() {
        override fun areItemsTheSame(oldItem: Cocktail, newItem: Cocktail): Boolean {
            return oldItem.idDrink == newItem.idDrink
        }

        override fun areContentsTheSame(oldItem: Cocktail, newItem: Cocktail): Boolean {
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
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.cocktailImage)

        holder.apply {
            binding.cocktailTitle.text = data.strDrink

            binding.root.setOnMaskChangedListener { maskRect ->
                binding.cocktailTitle.translationX = maskRect.left
                binding.cocktailTitle.alpha = lerp(1F, 0F, 0F, 80F, maskRect.left)
            }
        }
    }
}