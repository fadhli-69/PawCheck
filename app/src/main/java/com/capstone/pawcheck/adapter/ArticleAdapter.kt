package com.capstone.pawcheck.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.pawcheck.data.local.entity.ArticleEntity
import com.capstone.pawcheck.databinding.ItemArticleBinding
import com.capstone.pawcheck.views.homepage.DetailArticleActivity

class ArticleAdapter(private var articles: List<ArticleEntity>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleEntity) {
            binding.tvArticleName.text = article.name
            binding.tvArticleDescription.text = article.description
            Glide.with(binding.tvArticleImage.context)
                .load(article.photoUrl)
                .into(binding.tvArticleImage)

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailArticleActivity::class.java).apply {
                    putExtra("ARTICLE_IMAGE", article.photoUrl)
                    putExtra("ARTICLE_NAME", article.name)
                    putExtra("ARTICLE_DESCRIPTION", article.description)
                    putExtra("ARTICLE_DATE", article.createdAt)
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

    fun updateArticles(newArticles: List<ArticleEntity>) {
        val diffCallback = ArticleDiffCallback(articles, newArticles)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        articles = newArticles
        diffResult.dispatchUpdatesTo(this)
    }

    class ArticleDiffCallback(
        private val oldList: List<ArticleEntity>,
        private val newList: List<ArticleEntity>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
