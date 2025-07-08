package com.example.habitantitrainer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavHostController) {
    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Male") }
    var selectedRole by remember { mutableStateOf("Student") }
    var phoneNumber by remember { mutableStateOf("") }

    val ageGroups = listOf("Under 18", "18 - 25", "26 - 35", "36 - 50", "50+")
    var selectedAgeGroup by remember { mutableStateOf(ageGroups[1]) }
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF001F3F) // Deep blue background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 35.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "About",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF102840))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Age Group Dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = selectedAgeGroup,
                            onValueChange = {},
                            label = { Text("Age Group") },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.ArrowDropDown,
                                    contentDescription = "Dropdown",
                                    modifier = Modifier.clickable { expanded = !expanded }
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            ageGroups.forEach { age ->
                                DropdownMenuItem(
                                    text = { Text(age) },
                                    onClick = {
                                        selectedAgeGroup = age
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Gender
                    Text("Gender", color = Color.White, fontWeight = FontWeight.SemiBold)
                    Row {
                        listOf("Male", "Female", "Other").forEach { gender ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(end = 10.dp)
                            ) {
                                RadioButton(
                                    selected = selectedGender == gender,
                                    onClick = { selectedGender = gender }
                                )
                                Text(gender, color = Color.White)
                            }
                        }
                    }

                    // Role
                    Text("Role", color = Color.White, fontWeight = FontWeight.SemiBold)
                    Row {
                        listOf("Student", "Employee").forEach { role ->
                            OutlinedButton(
                                onClick = { selectedRole = role },
                                modifier = Modifier.padding(end = 8.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selectedRole == role) Color(0xFF4FC3F7) else Color.Transparent,
                                    contentColor = if (selectedRole == role) Color.Black else Color.White
                                )
                            ) {
                                Text(role)
                            }
                        }
                    }

                    // Phone + OTP
                    Column {
                        Text("Phone Number", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                label = { Text("Phone Number") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    Toast.makeText(
                                        context,
                                        "OTP sent to $phoneNumber",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4FC3F7))
                            ) {
                                Text("Verify")
                            }
                        }
                    }

                    // Submit
                    Button(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Submitted:\n$firstName $lastName\n$selectedGender, $selectedAgeGroup\n$selectedRole\nPhone: $phoneNumber",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4FC3F7)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Submit", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
