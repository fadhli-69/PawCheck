package com.capstone.pawcheck.views.homepage

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.capstone.pawcheck.R
import com.capstone.pawcheck.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding : ActivityArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val articleImage = intent.getStringExtra("ARTICLE_IMAGE")
        val articleName = intent.getStringExtra("ARTICLE_NAME")
        val articleDescription = intent.getStringExtra("ARTICLE_DESCRIPTION")
        val articleDate = intent.getStringExtra("ARTICLE_DATE")

        Glide.with(this).load(articleImage).into(binding.tvArticleImage)
        binding.headArticleName.text = articleName
        binding.tvArticleName.text = articleName
        binding.tvArticleDescription.text = articleDescription
        binding.tvPostDate.text = articleDate

        binding.ivBack.setOnClickListener{
            finish()
        }

    }

}