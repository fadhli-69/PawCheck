package com.capstone.pawcheck.views.drugpage

import DrugsAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
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

        val drugsList = intent.getParcelableArrayListExtra<Drug>("DRUGS_LIST")
        if (drugsList != null) {
            allDrugs.addAll(drugsList)
        }

        drugAdapter = DrugsAdapter(mutableListOf())
        binding.drugSearchRecyclerView.apply {
            layoutManager = GridLayoutManager(this@DrugSearchActivity, 2)
            adapter = drugAdapter
            addItemDecoration(SpaceItemDecoration(20))
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.searchInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                val query = binding.searchInput.text.toString().trim()
                if (query.isNotEmpty()) {
                    filterDrugs(query)
                } else {
                    drugAdapter.updateList(emptyList())
                    binding.drugSearchRecyclerView.visibility = View.GONE
                    binding.placeholderText.visibility = View.VISIBLE
                }
                true
            } else {
                false
            }
        }

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
