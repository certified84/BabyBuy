package com.certified.babybuy.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import com.certified.babybuy.ui.auth.OnBoardingScreen
import com.certified.babybuy.ui.custom_component.CustomLoader
import com.certified.babybuy.ui.theme.BabyBuyTheme
import com.certified.babybuy.util.UIState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            BabyBuyTheme {
                Surface {
                    var isLoading by rememberSaveable { mutableStateOf(false) }
                    viewModel.uiState.collectAsState().value.let {
                        isLoading = it == UIState.LOADING
                    }
                    OnBoardingScreen()
                    CustomLoader(isLoading = isLoading)
                }
            }
        }
    }

    private fun checkLogin() {
        if (Firebase.auth.currentUser != null) {

        }
    }
}