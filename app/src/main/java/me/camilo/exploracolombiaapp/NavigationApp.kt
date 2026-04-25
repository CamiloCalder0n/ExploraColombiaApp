package me.camilo.exploracolombiaapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun NavigationApp() {
    val myNavController = rememberNavController()

    // ── Mantener sesión activa ──
    // Si Firebase ya tiene un usuario autenticado, arranca directo en "home"
    val startDestination = if (Firebase.auth.currentUser != null) "home" else "login"

    NavHost(
        navController = myNavController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    myNavController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    myNavController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    // Tras registro exitoso va directo a home
                    myNavController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    myNavController.popBackStack()
                },
                onBackClick = {
                    myNavController.popBackStack()
                }
            )
        }

        composable("home") {
            HomeScreen(
                onLogout = {
                    myNavController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}