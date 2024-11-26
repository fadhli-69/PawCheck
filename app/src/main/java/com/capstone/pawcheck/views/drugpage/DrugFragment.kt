package com.capstone.pawcheck.views.drugpage

import DrugsAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.pawcheck.R
import com.capstone.pawcheck.customviews.SpaceItemDecoration
import com.capstone.pawcheck.data.propertiesdata.DrugsData
import com.capstone.pawcheck.databinding.FragmentDrugsBinding

class DrugFragment : Fragment(R.layout.fragment_drugs) {

    private var _binding: FragmentDrugsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDrugsBinding.bind(view)

        val drugsData = DrugsData.drugsData

        binding.drugRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.drugRecyclerView.adapter = DrugsAdapter(drugsData)
        binding.drugRecyclerView.addItemDecoration(SpaceItemDecoration(20))

        binding.cvSearchLL.setOnClickListener {
            val intent = Intent(requireContext(), DrugSearchActivity::class.java)
            intent.putParcelableArrayListExtra("DRUGS_LIST", ArrayList(drugsData))
            startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
