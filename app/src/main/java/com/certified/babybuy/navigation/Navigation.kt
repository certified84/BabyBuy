package com.certified.babybuy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.certified.babybuy.ui.auth.OnboardingScreen
import com.certified.babybuy.ui.auth.login.LoginScreen
import com.certified.babybuy.ui.auth.passwordRecovery.ForgotPasswordScreen
import com.certified.babybuy.ui.auth.signup.SignupScreen
import com.certified.babybuy.ui.home.HomeScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = /* if (Firebase.auth.currentUser != null) Screen.Home.route else */ Screen.Onboarding.route
    ) {
        composable(route = Screen.Onboarding.route) { OnboardingScreen(navController) }
        composable(route = Screen.Signup.route) { SignupScreen(navController) }
        composable(route = Screen.Login.route) { LoginScreen(navController) }
        composable(route = Screen.PasswordRecovery.route) { ForgotPasswordScreen(navController) }
        composable(route = Screen.Home.route) { HomeScreen(navController) }
    }
}