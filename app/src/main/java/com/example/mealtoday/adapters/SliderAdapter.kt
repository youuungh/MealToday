package com.example.mealtoday.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealtoday.data.Hot
import com.example.mealtoday.data.Slider
import com.example.mealtoday.databinding.SliderLayoutBinding

class SliderAdapter: RecyclerView.Adapter<SliderAdapter.ItemViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Slider>() {
        override fun areItemsTheSame(oldItem: Slider, newItem: Slider): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Slider, newItem: Slider): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    inner class ItemViewHolder(val binding: SliderLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderAdapter.ItemViewHolder {
        return ItemViewHolder(SliderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SliderAdapter.ItemViewHolder, position: Int) {
        val data = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(data.strMealThumb)
            .into(holder.binding.sliderImage)

        holder.apply {
            holder.binding.sliderTitle.text = data.strMeal
        }
    }
}