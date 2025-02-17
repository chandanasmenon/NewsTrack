package com.chandana.newstrack.ui.countrynews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.databinding.TopheadlinesSourcesItemBinding
import com.chandana.newstrack.utils.ItemClickListener
import com.chandana.newstrack.utils.extensions.capitalizeWords
import com.chandana.newstrack.utils.extensions.getCountryName
import com.chandana.newstrack.utils.extensions.getLanguageName

class CountryNewsAdapter(private val newsList: ArrayList<ApiSource>) :
    RecyclerView.Adapter<CountryNewsAdapter.DataViewHolder>() {
    lateinit var itemClickListener: ItemClickListener<ApiSource>

    class DataViewHolder(private val binding: TopheadlinesSourcesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(source: ApiSource, itemClickListener: ItemClickListener<ApiSource>) {
            binding.sourceNameTV.text = source.name
            binding.sourceDescriptionTV.text = source.description
            binding.categoryTV.text = source.category.capitalizeWords()
            binding.languageTV.text = source.language.getLanguageName(source.language)
            binding.countryTV.text = source.country.getCountryName(source.country)
            itemView.setOnClickListener {
                itemClickListener(source)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            TopheadlinesSourcesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(newsList[position], itemClickListener)
    }

    fun addData(list: List<ApiSource>) {
        newsList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData() {
        newsList.clear()
    }
}