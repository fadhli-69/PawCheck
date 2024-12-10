package com.capstone.pawcheck.views.authentication.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.pawcheck.databinding.ActivityRegisterBinding
import com.capstone.pawcheck.utils.ValidationUtils
import com.capstone.pawcheck.views.authentication.authviewmodel.AuthViewModel
import com.capstone.pawcheck.views.authentication.authviewmodel.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAnimations()
        setupListeners()
        observeRegistrationState()
    }

    private fun validateInputs(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true

        // Name validation
        if (!ValidationUtils.isValidName(name)) {
            binding.tfUsername.error = "Name must be 3-50 characters (letters only)"
            isValid = false
        } else {
            binding.tfUsername.error = null
        }

        // Email validation
        if (!ValidationUtils.isValidEmail(email)) {
            binding.tfEmail.error = "Invalid email format"
            isValid = false
        } else {
            binding.tfEmail.error = null
        }

        // Password validation
        val passwordErrors = ValidationUtils.getPasswordValidationErrors(password)
        if (passwordErrors.isNotEmpty()) {
            binding.tfPassword.error = passwordErrors.first()
            isValid = false
        } else {
            binding.tfPassword.error = null
        }

        // Confirm password validation
        if (!ValidationUtils.doPasswordsMatch(password, confirmPassword)) {
            binding.tfConfirmPassword.error = "Passwords do not match"
            isValid = false
        } else {
            binding.tfConfirmPassword.error = null
        }

        return isValid
    }

    private fun setupListeners() {
        binding.btnSignUp.setOnClickListener {
            // Retrieve input values
            val name = binding.tfUsername.editText?.text.toString().trim()
            val email = binding.tfEmail.editText?.text.toString().trim()
            val password = binding.tfPassword.editText?.text.toString().trim()
            val confirmPassword = binding.tfConfirmPassword.editText?.text.toString().trim()

            // Validate inputs before registration
            if (validateInputs(name, email, password, confirmPassword)) {
                // If inputs are valid, proceed with registration
                authViewModel.register(name, email, password)
            }
        }

        binding.topBar.setOnClickListener {
            finish()  // Close the RegisterActivity when the top bar is clicked
        }
    }

    private fun observeRegistrationState() {
        authViewModel.registrationState.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSignUp.isEnabled = false  // Disable button while loading
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSignUp.isEnabled = true
                    showToast("Registration successful")  // Show success message
                    finish()  // Close the register screen and go back
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSignUp.isEnabled = true
                    showToast("Registration failed: ${result.message}")  // Show error message
                }

                else -> {}
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAnimations() {
        val alphaAnimations = listOf(
            binding.topBar,
            binding.createAccount,
            binding.tfUsername,
            binding.tfEmail,
            binding.tfPassword,
            binding.tfConfirmPassword,
            binding.btnSignUp
        ).map { view ->
            ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).setDuration(200)
        }

        AnimatorSet().apply {
            playSequentially(alphaAnimations)
            startDelay = 100
        }.start()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}