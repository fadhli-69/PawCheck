package com.capstone.pawcheck.views.authentication.authviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.pawcheck.data.local.entity.UserEntity
import com.capstone.pawcheck.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository
) : ViewModel() {

    // Authentication State
    private val _authState = MutableLiveData<Boolean?>()
    val authState: LiveData<Boolean?> get() = _authState

    // Registration State
    private val _registrationState = MutableLiveData<Resource<String>>()
    val registrationState: LiveData<Resource<String>> get() = _registrationState

    // Login State
    private val _loginState = MutableLiveData<Resource<String>>()
    val loginState: LiveData<Resource<String>> get() = _loginState

    // Logout State
    private val _logoutState = MutableLiveData<Resource<Boolean>>()
    val logoutState: LiveData<Resource<Boolean>> get() = _logoutState

    // User Profile Data
    private val _userProfile = MutableLiveData<Map<String, String>>()
    val userProfile: LiveData<Map<String, String>> get() = _userProfile

    // Update Profile State
    private val _updateProfileState = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val updateProfileState: StateFlow<Resource<Boolean>> = _updateProfileState.asStateFlow()

    /**
     * Check if the user is logged in
     */
    fun checkLoginStatusWithDelay() {
        viewModelScope.launch {
            _authState.value = null // Menandakan bahwa status login sedang diperiksa
            delay(1000) // Simulasi waktu tunggu
            _authState.value = firebaseAuth.currentUser != null
        }
    }

    /**
     * Login user with email and password
     */
    fun login(email: String, password: String) {
        _loginState.value = Resource.Loading()
        viewModelScope.launch {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
                _loginState.value = Resource.Success("Login successful")
                checkLoginStatusWithDelay() // Update login state
                Log.i("AuthViewModel", "Login successful for email: $email")
            } catch (e: Exception) {
                _loginState.value = Resource.Error(e.localizedMessage ?: "Login failed")
                Log.e("AuthViewModel", "Login failed for email: $email", e)
            }
        }
    }

    /**
     * Register user and save details to Firestore
     */
    fun register(name: String, email: String, password: String) {
        _registrationState.value = Resource.Loading()
        viewModelScope.launch {
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                saveUserToFirestore(name, email)
                _registrationState.value = Resource.Success("Registration successful")
                Log.i("AuthViewModel", "Registration successful for email: $email")
            } catch (e: Exception) {
                _registrationState.value = Resource.Error(e.localizedMessage ?: "Registration failed")
                Log.e("AuthViewModel", "Registration failed for email: $email", e)
            }
        }
    }

    /**
     * Save user information to Firestore
     */
    private suspend fun saveUserToFirestore(name: String, email: String) {
        val userId = firebaseAuth.currentUser?.uid ?: throw Exception("User not logged in")
        val user = mapOf(
            "name" to name,
            "email" to email
        )
        firestore.collection("users").document(userId).set(user).await()
    }

    /**
     * Logout user
     */
    fun logout() {
        _logoutState.value = Resource.Loading()
        try {
            firebaseAuth.signOut()
            _logoutState.value = Resource.Success(true)
            _authState.value = false
            Log.i("AuthViewModel", "User logged out successfully")
        } catch (e: Exception) {
            _logoutState.value = Resource.Error(e.localizedMessage ?: "Logout failed")
            Log.e("AuthViewModel", "Logout failed: ${e.localizedMessage}")
        }
    }

    /**
     * Fetch user profile data from Firestore
     */
    fun fetchUserProfile() {
        viewModelScope.launch {
            val userId = firebaseAuth.currentUser?.uid ?: run {
                _userProfile.value = emptyMap()
                return@launch
            }

            // Check local Room cache first
            val localProfile = userRepository.getUserProfile(userId)

            if (localProfile != null) {
                // Convert UserProfile to Map<String, String>
                _userProfile.value = mapOf("name" to localProfile.name, "email" to localProfile.email)
                return@launch
            }

            // If not in Room, fetch from Firestore
            try {
                val document = firestore.collection("users").document(userId).get().await()
                if (document.exists()) {
                    val name = document.getString("name") ?: "Unknown"
                    val email = document.getString("email") ?: "Unknown"
                    val userProfile = UserEntity(userId, name, email)

                    // Save the profile to Room
                    userRepository.saveUserProfile(userProfile)

                    // Convert UserProfile to Map<String, String> and update the LiveData
                    _userProfile.value = mapOf("name" to name, "email" to email)
                }
            } catch (e: Exception) {
                _userProfile.value = emptyMap()
                Log.e("AuthViewModel", "Failed to fetch user profile", e)
            }
        }
    }

    /**
     * Update user's name in Firestore
     */
    fun updateProfile(newName: String) {
        _updateProfileState.value = Resource.Loading()

        viewModelScope.launch {
            try {
                val userId = firebaseAuth.currentUser?.uid
                    ?: throw Exception("User not logged in")

                // Update Firestore
                firestore.collection("users").document(userId)
                    .update("name", newName)
                    .await()

                // Update local Room cache
                val updatedProfile = UserEntity(userId, newName, firebaseAuth.currentUser?.email ?: "")
                userRepository.saveUserProfile(updatedProfile)

                _updateProfileState.value = Resource.Success(true)
                Log.i("AuthViewModel", "Profile updated successfully")
            } catch (e: Exception) {
                _updateProfileState.value = Resource.Error(e.localizedMessage ?: "Profile update failed")
                Log.e("AuthViewModel", "Profile update failed", e)
            }
        }
    }
}
