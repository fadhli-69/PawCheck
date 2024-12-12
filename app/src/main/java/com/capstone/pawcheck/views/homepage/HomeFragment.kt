package com.capstone.pawcheck.views.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.pawcheck.adapter.ArticleAdapter
import com.capstone.pawcheck.databinding.FragmentHomeBinding
import com.capstone.pawcheck.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArticleAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        homeViewModel.articles.observe(viewLifecycleOwner) { articles ->
            adapter.updateArticles(articles) // Memperbarui data di adapter
        }

        homeViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            // Menampilkan pesan error dengan Toast
            Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
        }

        homeViewModel.fetchArticles() // Memulai sinkronisasi data
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
