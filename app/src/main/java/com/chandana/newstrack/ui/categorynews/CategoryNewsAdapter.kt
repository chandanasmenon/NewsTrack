package com.chandana.newstrack.ui.categorynews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.databinding.CategoryNewsItemBinding
import com.chandana.newstrack.utils.ItemClickListener
import com.chandana.newstrack.utils.extensions.capitalizeWords
import com.chandana.newstrack.utils.extensions.getCountryName
import com.chandana.newstrack.utils.extensions.getLanguageName

class CategoryNewsAdapter(private val newsList: ArrayList<ApiSource>) :
    RecyclerView.Adapter<CategoryNewsAdapter.DataViewHolder>() {
    lateinit var itemClickListener: ItemClickListener<ApiSource>

    class DataViewHolder(private val binding: CategoryNewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(news: ApiSource, itemClickListener: ItemClickListener<ApiSource>) {
            binding.sourceNameTV.text = news.name
            binding.sourceDescriptionTV.text = news.description
            binding.categoryTV.text = news.category.capitalizeWords()
            binding.countryTV.text = news.country.getCountryName(news.country)
            binding.languageTV.text = news.language.getLanguageName(news.language)
            itemView.setOnClickListener {
                itemClickListener(news)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            CategoryNewsItemBinding.inflate(
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