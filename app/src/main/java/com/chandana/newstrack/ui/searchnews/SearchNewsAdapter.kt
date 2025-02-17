package com.chandana.newstrack.ui.searchnews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chandana.newstrack.data.model.Article
import com.chandana.newstrack.databinding.SearchItemBinding
import com.chandana.newstrack.utils.ItemClickListener
import com.chandana.newstrack.utils.extensions.loadImage

class SearchNewsAdapter(private val newsList: ArrayList<Article>) :
    RecyclerView.Adapter<SearchNewsAdapter.DataViewHolder>() {

    lateinit var itemClickListener: ItemClickListener<Article>

    class DataViewHolder(private val binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article, itemClickListener: ItemClickListener<Article>) {
            binding.TitleTV.text = article.title
            binding.newsDescriptionTV.text = article.description
            binding.authorNameTV.text = article.author
            binding.bannerImage.loadImage(article.urlToImage.toString())
            itemView.setOnClickListener {
                itemClickListener(article)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            SearchItemBinding.inflate(
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

    fun addData(list: List<Article>) {
        newsList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData() {
        newsList.clear()
    }
}