package com.certified.babybuy.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.certified.babybuy.ui.theme.BabyBuyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            BabyBuyTheme {

            }
        }
    }

//    private fun checkLogin() {
//        val currentDestination = navController.currentDestination?.id
//        val navOptions = NavOptions.Builder()
//            .setPopUpTo(R.id.nav_graph, true)
//            .build()
//        if (Firebase.auth.currentUser != null && currentDestination == R.id.onboardingFragment) {
//            navController.navigate(
//                R.id.action_onboardingFragment_to_homeFragment,
//                null,
//                navOptions
//            )
//        }
//    }
}