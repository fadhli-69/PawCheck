package com.capstone.pawcheck.views.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.capstone.pawcheck.R
import com.capstone.pawcheck.data.preferences.SettingPreferences
import com.capstone.pawcheck.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Atur tema aplikasi berdasarkan preferensi
        val settingPreferences = SettingPreferences(this)
        val isDarkMode = runBlocking {
            settingPreferences.getThemeSetting()
        }
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        // Inisialisasi binding dan navigasi
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Setup BottomNavigationView
        setupBottomNavigationView()
    }

    private fun setupBottomNavigationView() {
        binding.navView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    navController.popBackStack(R.id.navigation_home, false)
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_camera -> {
                    navController.popBackStack(R.id.navigation_camera, false)
                    navController.navigate(R.id.navigation_camera)
                    true
                }
                R.id.navigation_drugs -> {
                    navController.popBackStack(R.id.navigation_drugs, false)
                    navController.navigate(R.id.navigation_drugs)
                    true
                }
                R.id.navigation_profile -> {
                    navController.popBackStack(R.id.navigation_profile, false)
                    navController.navigate(R.id.navigation_profile)
                    true
                }
                else -> false
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        val navigateTo = intent.getStringExtra("navigate_to")
        if (navigateTo == "home") {
            navController.popBackStack(R.id.navigation_home, false)
            navController.navigate(R.id.navigation_home)
        }
    }
}