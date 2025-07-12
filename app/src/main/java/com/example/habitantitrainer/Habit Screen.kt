package com.example.habitantitrainer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.draw.clip
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HabitScreen(
    navController: NavHostController,
    onContinueClick: (String) -> Unit = {}
) {
    val predefinedHabits = listOf(
        "Smoking",
        "Overeating",
        "Procrastination",
        "Nail Biting",
        "Overspending",
        "Other"
    )
    var selectedHabit by remember { mutableStateOf("") }
    var customHabit by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF003366))
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Select Your Bad Habit",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB0C4DE),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Chips layout
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                predefinedHabits.forEach { habit ->
                    val isSelected = selectedHabit == habit

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (isSelected) Color(0xFF3399FF) else Color.White.copy(alpha = 0.1f),
                        tonalElevation = if (isSelected) 4.dp else 0.dp,
                        shadowElevation = if (isSelected) 2.dp else 0.dp,
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .width(120.dp)
                            .height(40.dp)
                            .clip(RoundedCornerShape(50))
                            .clickable {
                                selectedHabit = habit
                                if (habit != "Other") customHabit = ""
                            }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = habit,
                                color = if (isSelected) Color.White else Color(0xFFB0C4DE),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (selectedHabit == "Other") {
                OutlinedTextField(
                    value = customHabit,
                    onValueChange = { customHabit = it },
                    label = { Text("Enter your habit", color = Color(0xFFB0C4DE)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledTextColor = Color.LightGray,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color(0xFFB0C4DE),
                        cursorColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Spacer(modifier = Modifier.height(36.dp))
            }

            val finalHabit = if (selectedHabit == "Other") customHabit else selectedHabit

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF66CCFF), Color(0xFF3399FF))
                        )
                    )
                    .clickable(enabled = finalHabit.isNotBlank()) {
                        onContinueClick(finalHabit)
                        navController.navigate(Screen.DashboardScreen.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Continue",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,

                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HabitScreenPreview() {
    val navController = rememberNavController()
    HabitScreen(navController = navController) { selectedHabit ->
        println("Selected: $selectedHabit")
    }
}
