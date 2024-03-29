package com.ninezero.mealtoday.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ninezero.mealtoday.model.Meal
import com.ninezero.mealtoday.databinding.ItemFavoriteBinding
import com.ninezero.mealtoday.ui.fragments.FavoritesFragmentDirections
import eightbitlab.com.blurview.RenderEffectBlur

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.ItemViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    inner class ItemViewHolder(val binding: ItemFavoriteBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = differ.currentList.size

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(data.strMealThumb)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.favoriteImage)

        holder.apply {
            binding.blurView.setupWith(binding.container, RenderEffectBlur())
            binding.title.text = data.strMeal

            itemView.transitionName = "trans_${data.idMeal}"
            itemView.setOnClickListener {
                val extras = FragmentNavigatorExtras(binding.cvFavoriteImage to "trans_${data.idMeal}")
                Navigation.findNavController(it).navigate(
                    FavoritesFragmentDirections.actionFavoritesFragmentToMealFragment(
                        data.idMeal, data.strMealThumb, data.strMeal),
                    extras
                )
            }
        }
    }
}