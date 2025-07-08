package com.example.habitantitrainer

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

@Composable
fun WelcomeScreen(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val pulseAnim = rememberInfiniteTransition()
        val scale by pulseAnim.animateFloat(
            initialValue = 1f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF001F3F), Color(0xFF003366))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Glowing background lights
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .offset(x = (-100).dp, y = (-140).dp)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFFB0C4DE).copy(alpha = 0.15f), Color.Transparent)
                        ),
                        shape = CircleShape
                    )
            )

            Box(
                modifier = Modifier
                    .size(280.dp)
                    .offset(x = (100).dp, y = (500).dp)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFFB0C4DE).copy(alpha = 0.1f), Color.Transparent)
                        ),
                        shape = CircleShape
                    )
            )

            // Main content (no elevation)
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF4FC3F7), Color(0xFFB0C4DE))
                                )
                            )
                        ) {
                            append("WELCOME")
                        }
                    },
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.scale(scale)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Break bad habits, track failures,\nget better every day.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    color = Color(0xFFB0C4DE),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .height(50.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF4FC3F7), Color(0xFF0288D1))
                            )
                        )
                        .clickable {
                            navController.navigate(Screen.SignInScreen.route)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Get Started",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }
            }
        }
    }
}
