package com.capstone.pawcheck.views.result

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.pawcheck.ModelHelper
import com.capstone.pawcheck.databinding.ActivityScanResultBinding
import androidx.camera.core.ImageProxy

class ScanResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra("image_uri")
        val disease = intent.getStringExtra("disease")
        val diagnosis = intent.getStringExtra("diagnosis")

        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            binding.tvScanResult.setImageURI(imageUri)
        }

        binding.diagnoses.text = disease ?: "Unknown"
        binding.tvDiagnosesDescription.text = diagnosis ?: "No diagnosis available."
    }
}

