package com.chandana.newstrack.ui.pagingtopheadlinesources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chandana.newstrack.data.model.ApiSource
import com.chandana.newstrack.databinding.TopheadlinesSourcesItemBinding
import com.chandana.newstrack.utils.ItemClickListener
import com.chandana.newstrack.utils.extensions.capitalizeWords
import com.chandana.newstrack.utils.extensions.getCountryName
import com.chandana.newstrack.utils.extensions.getLanguageName

class TopHeadlinesPagingAdapter :
    PagingDataAdapter<ApiSource, TopHeadlinesPagingAdapter.DataViewHolder>(DIFF_CALLBACK) {

    lateinit var itemClickListener: ItemClickListener<ApiSource>

    class DataViewHolder(private val binding: TopheadlinesSourcesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(apiSource: ApiSource, itemClickListener: ItemClickListener<ApiSource>) {
            binding.sourceNameTV.text = apiSource.name
            binding.sourceDescriptionTV.text = apiSource.description
            binding.categoryTV.text = apiSource.category.capitalizeWords()
            binding.countryTV.text = apiSource.country.getCountryName(apiSource.country)
            binding.languageTV.text = apiSource.language.getLanguageName(apiSource.language)
            itemView.setOnClickListener {
                itemClickListener(apiSource)
            }
        }
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val source = getItem(position)
        if (source != null) {
            holder.bind(source, itemClickListener)
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

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ApiSource>() {
            override fun areItemsTheSame(oldItem: ApiSource, newItem: ApiSource): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ApiSource, newItem: ApiSource): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}
