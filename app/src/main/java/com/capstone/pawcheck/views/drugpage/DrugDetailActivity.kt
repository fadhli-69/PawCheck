package com.capstone.pawcheck.views.drugpage

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.pawcheck.R
import com.capstone.pawcheck.databinding.ActivityDrugDetailBinding

class DrugDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDrugDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDrugDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drugName = intent.getStringExtra("DRUG_NAME")
        val drugDescription = intent.getStringExtra("DRUG_DESCRIPTION")
        val drugImageResId = intent.getIntExtra("DRUG_IMAGE", R.drawable.camera_background) // Gambar default jika tidak ada

        val tvTopDrugName: TextView = binding.tvTopDrugName
        val tvDrugName: TextView = binding.tvDrugName
        val tvDrugDescription: TextView = binding.deksripsiProduk
        val tvDrugImage: ImageView = binding.tvDrugDetail

        tvTopDrugName.text = drugName
        tvDrugName.text = drugName
        tvDrugDescription.text = drugDescription
        tvDrugImage.setImageResource(drugImageResId)

        binding.ivBack.setOnClickListener{
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

