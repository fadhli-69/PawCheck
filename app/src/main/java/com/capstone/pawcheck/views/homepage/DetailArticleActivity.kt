package com.capstone.pawcheck.views.homepage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.pawcheck.databinding.ActivityDetailArticleBinding

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val articleImage = intent.getStringExtra("ARTICLE_IMAGE")
        val articleName = intent.getStringExtra("ARTICLE_NAME")
        val articleDescription = intent.getStringExtra("ARTICLE_DESCRIPTION")
        val articleDate = intent.getStringExtra("ARTICLE_DATE")

        Glide.with(this)
            .load(articleImage)
            .into(binding.tvArticleImage)

        binding.headArticleName.text = articleName
        binding.tvArticleName.text = articleName
        binding.tvArticleDescription.text = articleDescription
        binding.tvPostDate.text = articleDate

        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}