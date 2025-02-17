package com.chandana.newstrack.ui.searchnews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chandana.newstrack.R
import com.chandana.newstrack.databinding.FilterDataItemBinding
import com.chandana.newstrack.utils.ItemClickListener

class FilterDataAdapter(private val filterData: ArrayList<String>) :
    RecyclerView.Adapter<FilterDataAdapter.DataViewHolder>() {

    private var selectedPosition = 0

    lateinit var itemClickListener: ItemClickListener<String>

    class DataViewHolder(private val binding: FilterDataItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            data: String,
            isSelected: Boolean
        ) {
            binding.filterData.text = data
            if (isSelected) {
                binding.filterData.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.dark_blue)
                )
                binding.filterData.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.white)
                )
            } else {
                binding.filterData.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.white)
                )
                binding.filterData.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.black)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            FilterDataItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return filterData.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(filterData[position], position == selectedPosition)

        holder.itemView.setOnClickListener {
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            itemClickListener(filterData[position])
        }
    }

    fun addData(list: List<String>) {
        filterData.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData() {
        filterData.clear()
    }
}
