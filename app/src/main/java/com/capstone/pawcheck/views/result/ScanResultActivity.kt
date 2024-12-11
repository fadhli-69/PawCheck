package com.capstone.pawcheck.views.result

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.pawcheck.databinding.ActivityScanResultBinding
import com.capstone.pawcheck.views.main.MainActivity

class ScanResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanResultBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra("image_uri")
        val disease = intent.getStringExtra("disease")
        val diagnosis = intent.getStringExtra("diagnosis")
        val treatment = intent.getStringArrayListExtra("treatment")

        imageUriString?.let {
            val imageUri = Uri.parse(it)
            binding.tvScanResult.setImageURI(imageUri)
        }

        binding.diagnoses.text = disease ?: "Unknown"
        binding.tvDiagnosesDescription.text = diagnosis ?: "No diagnosis available."

        if (treatment != null && treatment.isNotEmpty()) {
            val treatmentText = treatment.joinToString(separator = "\n") { "• $it" }
            binding.treatmentRecommendationsDescription.text = treatmentText
        } else {
            binding.treatmentRecommendationsDescription.text = "No treatment recommendations available."
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.actionHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("navigate_to", "home")
            }
            startActivity(intent)
            finish()
        }
    }
}
