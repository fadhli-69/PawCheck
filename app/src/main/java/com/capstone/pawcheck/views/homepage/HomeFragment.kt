package com.capstone.pawcheck.views.homepage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.pawcheck.R
import com.capstone.pawcheck.databinding.FragmentCameraBinding
import com.capstone.pawcheck.databinding.FragmentHomeBinding
import com.capstone.pawcheck.views.camerapage.CameraFragment

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btnCamFrag.setOnClickListener {
            val cameraFragment = CameraFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, cameraFragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root

    }
}