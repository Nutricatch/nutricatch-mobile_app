package com.nutricatch.dev.views.app_check

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nutricatch.dev.data.prefs.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AppCheckViewModel(private val preferences: Preferences) : ViewModel() {
    fun isOnBoard(): Flow<Boolean> {
        return preferences.isOnBoard()
    }

    val token = preferences.getToken()

    val theme = preferences.themeMode.asLiveData()

    fun getLocale(): String? {
        var locale: String? = null
        viewModelScope.launch {
            preferences.appLocale.collect { locale = it }
        }
        return locale
    }
}