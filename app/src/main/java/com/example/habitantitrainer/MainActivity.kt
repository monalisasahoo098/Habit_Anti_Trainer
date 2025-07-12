package com.example.habitantitrainer

import android.R.attr.defaultValue
import android.R.attr.type
import android.os.Bundle
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import com.example.habitantitrainer.ui.theme.HabitAntiTrainerTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object WelcomeScreen : Screen("Welcome_screen")
    object SignInScreen : Screen("SignInScreen_screen")
    object SignUpScreen : Screen("SignUpScreen_screen")
    object ResetScreen : Screen("ResetScreen_screen")
    object OtpScreen : Screen("OtpScreen_screen")
    object AboutScreen : Screen("AboutScreen_screen")
    object DashboardScreen : Screen("DashboardScreen_screen")
    object HabitScreen : Screen("HabitScreen_screen")
    object ProfileDetailScreen : Screen("ProfileDetailScreen_screen")


}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitAntiTrainerTheme {
                AppNavigation()

            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = com.example.habitantitrainer.Screen.SplashScreen.route
    ) {

        //splash Screen
        composable(com.example.habitantitrainer.Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }

        // welcome screen
        composable(com.example.habitantitrainer.Screen.WelcomeScreen.route) {
            WelcomeScreen(navController = navController)
        }

        //signin screen
        composable(com.example.habitantitrainer.Screen.SignInScreen.route) {
            SignInScreen(navController = navController)
        }

        //signup screen
        composable(com.example.habitantitrainer.Screen.SignUpScreen.route) {
            SignUpScreen(navController = navController)
        }

        //Reset Screen
        composable(com.example.habitantitrainer.Screen.ResetScreen.route) {
            ResetScreen(navController = navController)
        }

        //otp screen
        composable(
            "OtpScreen/{verificationId}/{phone}?email={email}&password={password}&name={name}",
            arguments = listOf(
                navArgument("verificationId") { type = NavType.StringType },
                navArgument("phone") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("password") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) {
            val verificationId = it.arguments?.getString("verificationId") ?: ""
            val phone = it.arguments?.getString("phone") ?: ""
            val email = it.arguments?.getString("email") ?: ""
            val password = it.arguments?.getString("password") ?: ""
            val name = it.arguments?.getString("name") ?: ""
            OtpScreen(navController, verificationId, phone, email, password, name)
        }



        // about screen
        composable(com.example.habitantitrainer.Screen.AboutScreen.route) {
            AboutScreen(navController = navController)
        }

        //habit screen
        composable(com.example.habitantitrainer.Screen.HabitScreen.route) {
            HabitScreen(navController = navController)
        }

        //dashboard screen
        composable(com.example.habitantitrainer.Screen.DashboardScreen.route) {
            DashboardScreen(navController = navController)
        }

        //profile detail screen
        composable(com.example.habitantitrainer.Screen.ProfileDetailScreen.route) {
            ProfileDetailScreen(navController = navController)
        }



    }
}