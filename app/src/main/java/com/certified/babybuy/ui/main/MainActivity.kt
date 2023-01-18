package com.certified.babybuy.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.certified.babybuy.BuildConfig
import com.certified.babybuy.R
import com.certified.babybuy.databinding.ActivityMainBinding
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.nav_host_fragment)
        Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        splashScreen.setKeepOnScreenCondition { viewModel.isLoading.value }
        checkLogin()
    }

    private fun checkLogin() {
        val currentDestination = navController.currentDestination?.id
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph, true)
            .build()
        if (Firebase.auth.currentUser != null && currentDestination == R.id.onboardingFragment) {
            navController.navigate(
                R.id.action_onboardingFragment_to_homeFragment,
                null,
                navOptions
            )
        }
    }
}