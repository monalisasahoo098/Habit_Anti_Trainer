package com.example.habitantitrainer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var showText by remember { mutableStateOf(false) }

        val bounceAnim = rememberInfiniteTransition()
        val offsetY by bounceAnim.animateFloat(
            initialValue = 0f,
            targetValue = -20f,
            animationSpec = infiniteRepeatable(
                animation = tween(700, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        LaunchedEffect(Unit) {
            delay(2500)
            showText = true
            //naviagte to welcome screen
            delay(2000)
            navController.navigate(Screen.WelcomeScreen.route) {
                popUpTo(Screen.WelcomeScreen.route) { inclusive = true } // removes Splash from backstack
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF001F3F), Color(0xFF003366))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Background circles
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .offset(x = (-120).dp, y = (-140).dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFB0C4DE).copy(alpha = 0.2f), Color.Transparent)
                        ),
                        shape = CircleShape
                    )
            )

            Box(
                modifier = Modifier
                    .size(280.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 100.dp, y = 100.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFB0C4DE).copy(alpha = 0.15f), Color.Transparent)
                        ),
                        shape = CircleShape
                    )
            )

            // Logo + Title
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Uncomment and replace with your actual logo resource

                Image(
                    painter = painterResource(id = R.drawable.splash_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(130.dp)
                        .offset(y = offsetY.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(24.dp)
                )


                Spacer(modifier = Modifier.height(20.dp))

                AnimatedVisibility(visible = showText) {
                    Text(
                        text = "Habit Anti Trainer",
                        fontSize = 28.sp,
                        color = Color(0xFFB0C4DE),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }
            }
        }
    }
}
