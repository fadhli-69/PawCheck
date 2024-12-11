package com.capstone.pawcheck.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.pawcheck.data.remote.response.ListArticleItem
import com.capstone.pawcheck.databinding.ItemArticleBinding
import com.capstone.pawcheck.views.homepage.ArticleActivity

class ArticleAdapter(private val articles: List<ListArticleItem>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ListArticleItem) {
            binding.tvArticleName.text = article.name ?: "No Title"
            binding.tvArticleDescription.text = article.description ?: "No Description"
            Glide.with(binding.tvArticleImage.context)
                .load(article.photoUrl)
                .into(binding.tvArticleImage)

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, ArticleActivity::class.java).apply {
                    putExtra("ARTICLE_IMAGE", article.photoUrl)
                    putExtra("ARTICLE_NAME", article.name)
                    putExtra("ARTICLE_DESCRIPTION", article.description)
                    putExtra("ARTICLE_DATE", article.createdAt?.split("T")?.get(0))
                }
                context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size
}