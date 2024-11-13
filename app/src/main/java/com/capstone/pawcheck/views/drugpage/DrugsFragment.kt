package com.capstone.pawcheck.views.drugpage

import DrugsAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.pawcheck.R
import com.capstone.pawcheck.adapter.Drug
import com.capstone.pawcheck.customviews.SpaceItemDecoration

class DrugsFragment : Fragment(R.layout.fragment_drugs) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.drugRecyclerView)

        // Contoh data untuk diisi di dalam RecyclerView
        val sampleDrugs = listOf(
            Drug("Paracetamol", "Pain relief and fever reducer", R.drawable.camera_background),
            Drug("Ibuprofen", "Anti-inflammatory medication", R.drawable.camera_background),
            Drug("Paracetamol", "Pain relief and fever reducer", R.drawable.camera_background),
            Drug("Ibuprofen", "Anti-inflammatory medication", R.drawable.camera_background),
            Drug("Paracetamol", "Pain relief and fever reducer", R.drawable.camera_background),
            Drug("Ibuprofen", "Anti-inflammatory medication", R.drawable.camera_background),
        )

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = DrugsAdapter(sampleDrugs)

        recyclerView.addItemDecoration(SpaceItemDecoration(20)) // 20dp spasi antar card

    }
}
