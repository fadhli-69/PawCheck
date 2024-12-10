package com.capstone.pawcheck.views.profilepage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.capstone.pawcheck.databinding.FragmentEditProfileBinding
import com.capstone.pawcheck.utils.ValidationUtils
import com.capstone.pawcheck.views.authentication.authviewmodel.AuthViewModel
import com.capstone.pawcheck.views.authentication.authviewmodel.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUserProfile()
        setupTextValidation()
        setupClickListeners()
        observeUpdateProfileState()
    }

    private fun observeUserProfile() {
        // Observe the user profile from the ViewModel and set it in the EditText
        authViewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            // Set the name from the profile, or leave the EditText empty if not available
            binding.etName.setText(profile["name"] ?: "")
        }
    }

    private fun setupTextValidation() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val name = s.toString().trim()
                if (ValidationUtils.isValidName(name)) {
                    binding.tilName.error = null
                    binding.btnSave.isEnabled = true
                } else {
                    binding.tilName.error = "Name must be 3-10 letters"
                    binding.btnSave.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupClickListeners() {
        // Back button to navigate up
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // Save button to update the profile
        binding.btnSave.setOnClickListener {
            val newName = binding.etName.text.toString().trim()

            // Validate name before updating
            if (ValidationUtils.isValidName(newName)) {
                binding.tilName.error = null
                authViewModel.updateProfile(newName)
            } else {
                binding.tilName.error = "Name must be 3-10 letters"
            }
        }
    }

    private fun observeUpdateProfileState() {
        // Observe the update profile state for success/failure/loading status
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.updateProfileState.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            if (binding.etName.text.toString().isNotBlank()) {
                                binding.btnSave.isEnabled = false
                                binding.progressBar.visibility = View.VISIBLE
                            }
                        }

                        is Resource.Success -> {
                            binding.btnSave.isEnabled = true
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Profile updated successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigateUp() // Navigate back after update
                        }

                        is Resource.Error -> {
                            binding.btnSave.isEnabled = true
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                resource.message ?: "Update failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}