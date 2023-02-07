package com.certified.babybuy.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Signup : Screen("signup")
    object Login : Screen("login")
    object PasswordRecovery : Screen("passwordRecovery")
    object Home : Screen("home")
}
