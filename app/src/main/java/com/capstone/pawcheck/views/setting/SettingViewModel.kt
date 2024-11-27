package com.capstone.pawcheck.views.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.pawcheck.data.preferences.SettingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val themePreferences: SettingPreferences
) : ViewModel() {

    val themeSettings: LiveData<Boolean> = themePreferences.themeFlow.asLiveData()

    fun saveThemeSetting(isDarkMode: Boolean) {
        viewModelScope.launch {
            themePreferences.saveThemeSetting(isDarkMode)
        }
    }
}
