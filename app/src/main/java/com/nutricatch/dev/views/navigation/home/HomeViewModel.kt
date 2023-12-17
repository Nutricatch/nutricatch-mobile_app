package com.nutricatch.dev.views.navigation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nutricatch.dev.data.prefs.Preferences
import com.nutricatch.dev.data.repository.UserRepository

class HomeViewModel(private val repository: UserRepository, preferences: Preferences) :
    ViewModel() {

    val token = preferences.getToken().asLiveData()

    val userData = repository.getProfile()

}