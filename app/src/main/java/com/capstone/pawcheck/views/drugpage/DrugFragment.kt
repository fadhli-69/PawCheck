package com.capstone.pawcheck.views.drugpage

import DrugsAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.pawcheck.R
import com.capstone.pawcheck.adapter.Drug
import com.capstone.pawcheck.customviews.SpaceItemDecoration
import com.capstone.pawcheck.databinding.FragmentDrugsBinding

class DrugFragment : Fragment(R.layout.fragment_drugs) {

    private var _binding: FragmentDrugsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDrugsBinding.bind(view)

        val sampleDrugs = mutableListOf(
            Drug("Paracetamol", "Pain relief and fever reducer", R.drawable.camera_background),
            Drug("Ibuprofen", "Anti-inflammatory medication", R.drawable.camera_background),
            Drug("Paracetamol", "Pain relief and fever reducer", R.drawable.camera_background),
            Drug("Ibuprofen", "Anti-inflammatory medication", R.drawable.camera_background),
            Drug("Paracetamol", "Pain relief and fever reducer", R.drawable.camera_background),
            Drug("Ibuprofen", "Anti-inflammatory medication", R.drawable.camera_background),
        )

        binding.drugRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.drugRecyclerView.adapter = DrugsAdapter(sampleDrugs)
        binding.drugRecyclerView.addItemDecoration(SpaceItemDecoration(20))

        binding.cvSearchLL.setOnClickListener {
            val intent = Intent(requireContext(), DrugSearchActivity::class.java)
            val drugsList = sampleDrugs.map { drug -> drug.name to drug.description }.toTypedArray()
            intent.putExtra("DRUGS_LIST", drugsList)
            startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
