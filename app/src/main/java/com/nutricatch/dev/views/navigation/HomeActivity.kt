package com.nutricatch.dev.views.navigation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nutricatch.dev.R
import com.nutricatch.dev.data.prefs.Preferences
import com.nutricatch.dev.data.prefs.dataStore
import com.nutricatch.dev.databinding.ActivityHomeBinding
import com.nutricatch.dev.views.navigation.daily_calories.DailyCaloriesFragmentDirections
import com.nutricatch.dev.views.navigation.home.HomeFragmentDirections

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var preferences: Preferences
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = Preferences.getInstance(applicationContext.dataStore)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        /*
        *   Check user mode, guest or registered user
        * */
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_home,
            R.id.navigation_camera,
            R.id.navigation_daily_calories,
        ).build()

        setSupportActionBar(binding.myToolbar)
        binding.myToolbar.visibility = View.GONE
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        var action = HomeFragmentDirections.actionNavigationHomeToCameraFragment()
        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            binding.navView.isVisible = appBarConfiguration.isTopLevelDestination(navDestination)
                .also { binding.fabScan.isVisible = it }
            when (navDestination.id) {
                R.id.navigation_home -> {
                    action = HomeFragmentDirections.actionNavigationHomeToCameraFragment()
                    // Fragment 1 is active
                    // Perform actions or updates specific to Fragment 1
                }

                R.id.navigation_daily_calories -> {
                    // Fragment 2 is active
                    // Perform actions or updates specific to Fragment 2
                    action =
                        DailyCaloriesFragmentDirections.actionNavigationDailyCaloriesToCameraFragment()
                }
                // Add other fragment IDs as needed
            }
        }

        binding.fabScan.setOnClickListener {
            navController.navigate(action)
        }

        binding.navView.menu.getItem(1).isEnabled = false

    }
}