package com.capstone.pawcheck.views.homepage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
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
            findNavController().navigate(R.id.action_homeFragment_to_cameraFragment)
        }

        return binding.root

    }
}