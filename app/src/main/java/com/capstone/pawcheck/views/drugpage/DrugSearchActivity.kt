package com.capstone.pawcheck.views.drugpage

import DrugsAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.pawcheck.R
import com.capstone.pawcheck.adapter.Drug
import com.capstone.pawcheck.customviews.SpaceItemDecoration
import com.capstone.pawcheck.databinding.ActivityDrugSearchBinding

class DrugSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrugSearchBinding
    private lateinit var drugAdapter: DrugsAdapter
    private val allDrugs = mutableListOf<Drug>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrugSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drugsList = intent.getSerializableExtra("DRUGS_LIST") as? Array<Pair<String, String>>
        drugsList?.forEach {
            allDrugs.add(Drug(it.first, it.second, R.drawable.camera_background))
        }

        drugAdapter = DrugsAdapter(mutableListOf())
        binding.drugSearchRecyclerView.apply {
            layoutManager = GridLayoutManager(this@DrugSearchActivity, 2) // Grid dengan 2 kolom
            adapter = drugAdapter
            addItemDecoration(SpaceItemDecoration(20)) // Jarak antar item
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterDrugs(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterDrugs(query: String) {
        val filteredDrugs = allDrugs.filter {
            it.name.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true)
        }

        drugAdapter.updateList(filteredDrugs)

        if (filteredDrugs.isEmpty()) {
            binding.drugSearchRecyclerView.visibility = View.GONE
            binding.placeholderText.visibility = View.VISIBLE
        } else {
            binding.drugSearchRecyclerView.visibility = View.VISIBLE
            binding.placeholderText.visibility = View.GONE
        }
    }
}
