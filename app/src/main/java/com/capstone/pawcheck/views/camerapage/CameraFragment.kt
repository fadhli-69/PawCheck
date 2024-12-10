package com.capstone.pawcheck.views.camerapage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.capstone.pawcheck.ModelHelper
import com.capstone.pawcheck.R
import com.capstone.pawcheck.databinding.FragmentCameraBinding
import com.capstone.pawcheck.views.main.MainActivity
import com.capstone.pawcheck.views.result.ScanResultActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CameraFragment : Fragment() {

    // Jika sudah fix di akhir tinggal tambahkan fitur U-Crop

    private lateinit var binding: FragmentCameraBinding
    private lateinit var viewFinder: PreviewView

    private val requestCameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewFinder = binding.viewFinder

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }

        val galleryButton = binding.galleryButton
        galleryButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        val backButton = binding.ivBack
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build()

                preview.setSurfaceProvider(viewFinder.surfaceProvider)

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(
                        viewLifecycleOwner,
                        cameraSelector,
                        preview
                    )

                } catch (e: Exception) {
                    Log.e(TAG, "Use case binding failed", e)
                }

            } catch (e: Exception) {
                Log.e(TAG, "Camera provider is not available", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            analyzeImage(it)
        } ?: run {
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun analyzeImage(imageUri: Uri) {
        try {
            val bitmap = BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(imageUri))
            val modelHelper = ModelHelper(requireContext(), object : ModelHelper.ClassifierListener {
                override fun onError(error: String) {
                    Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_SHORT).show()
                }

                override fun onResults(
                    results: FloatArray,
                    inferenceTime: Long,
                    disease: String,
                    diagnosis: String,
                    treatment: List<String>
                ) {
                    val intent = Intent(requireContext(), ScanResultActivity::class.java).apply {
                        putExtra("image_uri", imageUri.toString())
                        putExtra("disease", disease)
                        putExtra("diagnosis", diagnosis)
                        putStringArrayListExtra("treatment", ArrayList(treatment))
                    }
                    startActivity(intent)
                }
            })
            modelHelper.classifyImage(bitmap)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error analyzing image: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }



    companion object {
        private const val TAG = "CameraFragment"
    }
}