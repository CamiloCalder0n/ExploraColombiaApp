package me.camilo.exploracolombiaapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import me.camilo.exploracolombiaapp.ui.elements.AddPlaceScreen
import me.camilo.exploracolombiaapp.ui.elements.HomeScreen
import me.camilo.exploracolombiaapp.ui.elements.LoginScreen
import me.camilo.exploracolombiaapp.ui.elements.RegisterScreen

@Composable
fun NavigationApp() {
    val myNavController = rememberNavController()
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
            HomeScreen(onClickAddPlace = {
                myNavController.navigate("addPlace")
            })
        }

        composable("addPlace") {
            AddPlaceScreen()
        }
        composable("addPlace") {
            AddPlaceScreen(onBackClick = { myNavController.popBackStack() })
        }
    }
}