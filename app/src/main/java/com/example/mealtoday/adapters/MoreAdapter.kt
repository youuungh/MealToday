package com.example.mealtoday.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mealtoday.R
import com.example.mealtoday.data.MoreItem
import com.example.mealtoday.data.Slider
import com.example.mealtoday.databinding.MoreFooterBinding
import com.example.mealtoday.databinding.MoreHeaderBinding
import com.example.mealtoday.databinding.MoreStickyBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MoreAdapter(private val activity: FragmentActivity, private var sliderList: ArrayList<Slider>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val rvItemList = arrayListOf<MoreItem>()
    private var tabViewPager: ViewPager2? = null
    private var tabLayout: TabLayout? = null

    companion object {
        private const val HEADER = 0
        private const val STICKY = 1
        private const val FOOTER = 2
    }

    init {
        setListData()
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> HEADER
            1 -> STICKY
            else -> FOOTER
        }
    }

    private fun setListData() {
        rvItemList.clear()
        rvItemList.add(MoreItem(HEADER))
        rvItemList.add(MoreItem(STICKY))
        rvItemList.add(MoreItem(FOOTER))
        notifyDataSetChanged()
    }

    inner class HeaderViewHolder(val binding: MoreHeaderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val sliderAdapter = SliderAdapter()
            sliderAdapter.differ.submitList(sliderList)

            binding.rvBanner.apply {
                layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
                adapter = sliderAdapter
                setHasFixedSize(true)
            }
        }
    }

    inner class StickyViewHolder(val binding: MoreStickyBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            tabLayout = binding.tabLayout
        }
    }

    inner class FooterViewHolder(val binding: MoreFooterBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val daysOfWeek = arrayOf("월", "화", "수", "목", "금", "토", "일")

            tabViewPager = binding.tabViewPager
            tabViewPager?.apply {
                adapter = DailyStateAdapter(activity)
                TabLayoutMediator(tabLayout!!, this) { tab, position ->
                    tab.text = daysOfWeek[position]
                }.attach()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> HeaderViewHolder(
                MoreHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            STICKY -> StickyViewHolder(
                MoreStickyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> FooterViewHolder(
                MoreFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun getItemCount(): Int = rvItemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind()
            is StickyViewHolder -> holder.bind()
            is FooterViewHolder -> holder.bind()
        }
    }

    fun isHeader(position: Int): Boolean {
        return rvItemList[position].type == STICKY
    }

    fun getHeaderView(list: RecyclerView, position: Int): View? {
        val lastIndex =
            if (position < rvItemList.size) position else rvItemList.size - 1
        for (index in lastIndex downTo 0) {
            val model = rvItemList[index]
            if (model.type == STICKY) {
                return LayoutInflater.from(list.context)
                    .inflate(R.layout.more_sticky, list, false)
            }
        }
        return null
    }
}