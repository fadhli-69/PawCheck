package com.capstone.pawcheck.views.authentication.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.capstone.pawcheck.data.preferences.SettingPreferences
import com.capstone.pawcheck.databinding.ActivityRegisterBinding
import com.capstone.pawcheck.utils.ValidationUtils
import com.capstone.pawcheck.views.authentication.authviewmodel.AuthViewModel
import com.capstone.pawcheck.views.authentication.authviewmodel.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setupTheme()
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAnimations()
        setupListeners()
        observeRegistrationState()
    }

    private fun setupTheme() {
        val settingPreferences = SettingPreferences(this)
        lifecycleScope.launch {
            val isDarkMode = settingPreferences.getThemeSetting()
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun validateInputs(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true

        if (!ValidationUtils.isValidName(name)) {
            binding.tfUsername.error = "Name must be 3-50 characters (letters only)"
            isValid = false
        } else {
            binding.tfUsername.error = null
        }

        if (!ValidationUtils.isValidEmail(email)) {
            binding.tfEmail.error = "Invalid email format"
            isValid = false
        } else {
            binding.tfEmail.error = null
        }

        val passwordErrors = ValidationUtils.getPasswordValidationErrors(password)
        if (passwordErrors.isNotEmpty()) {
            binding.tfPassword.error = passwordErrors.first()
            isValid = false
        } else {
            binding.tfPassword.error = null
        }

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
            val name = binding.tfUsername.editText?.text.toString().trim()
            val email = binding.tfEmail.editText?.text.toString().trim()
            val password = binding.tfPassword.editText?.text.toString().trim()
            val confirmPassword = binding.tfConfirmPassword.editText?.text.toString().trim()


            if (validateInputs(name, email, password, confirmPassword)) {
                authViewModel.register(name, email, password)
            }
        }

        binding.topBar.setOnClickListener {
            finish()
        }
    }

    private fun observeRegistrationState() {
        authViewModel.registrationState.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSignUp.isEnabled = false
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSignUp.isEnabled = true
                    showToast("Registration successful")
                    finish()
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSignUp.isEnabled = true
                    showToast("Registration failed: ${result.message}")
                }

                else -> {}
            }
        }
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