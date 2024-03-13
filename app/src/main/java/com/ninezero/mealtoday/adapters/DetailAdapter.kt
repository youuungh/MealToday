package com.ninezero.mealtoday.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ninezero.mealtoday.databinding.ItemHotAllBinding
import com.ninezero.mealtoday.model.Hot

class DetailAdapter(type: Int): RecyclerView.Adapter<DetailAdapter.HotAllViewHolder>() {

    lateinit var onDetailItemClick: ((Hot) -> Unit)

    private val diffUtil = object : DiffUtil.ItemCallback<Hot>() {
        override fun areItemsTheSame(oldItem: Hot, newItem: Hot): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Hot, newItem: Hot): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    inner class HotAllViewHolder(val binding: ItemHotAllBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotAllViewHolder {
        return HotAllViewHolder(ItemHotAllBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: HotAllViewHolder, position: Int) {
        val data = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(data.strMealThumb)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.detailImage)

        holder.apply {
            binding.detailTitle.text = data.strMeal

            itemView.setOnClickListener {
                onDetailItemClick.invoke(data)
            }
        }
    }
}