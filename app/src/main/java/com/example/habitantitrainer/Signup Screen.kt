package com.example.habitantitrainer

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SignUpScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFF001F3F), Color(0xFF003366)))),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Create Account", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = if (it.isBlank()) "Name cannot be empty" else ""
                    },
                    placeholder = { Text("Full Name", color = Color.LightGray) },
                    textStyle = TextStyle(color = Color.White),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4FC3F7),
                        unfocusedBorderColor = Color.Gray
                    )
                )
                if (nameError.isNotEmpty()) Text(nameError, color = Color.Red)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = if (!Patterns.EMAIL_ADDRESS.matcher(it).matches()) "Enter valid email" else ""
                    },
                    placeholder = { Text("Email", color = Color.LightGray) },
                    textStyle = TextStyle(color = Color.White),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4FC3F7),
                        unfocusedBorderColor = Color.Gray
                    )
                )
                if (emailError.isNotEmpty()) Text(emailError, color = Color.Red)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = if (it.length < 6) "Password must be at least 6 characters with an uppercase letter" else ""
                    },
                    placeholder = { Text("Password", color = Color.LightGray) },
                    textStyle = TextStyle(color = Color.White),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4FC3F7),
                        unfocusedBorderColor = Color.Gray
                    )
                )
                if (passwordError.isNotEmpty()) Text(passwordError, color = Color.Red)

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(Brush.horizontalGradient(listOf(Color(0xFF4FC3F7), Color(0xFF0288D1))))
                        .clickable {
                            if (name.isBlank() || emailError.isNotEmpty() || passwordError.isNotEmpty()) {
                                Toast.makeText(context, "Please fix errors above", Toast.LENGTH_SHORT).show()
                                return@clickable
                            }

                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val uid = auth.currentUser?.uid
                                        val userData = hashMapOf(
                                            "name" to name,
                                            "email" to email
                                        )
                                        uid?.let {
                                            db.collection("users").document(it).set(userData)
                                                .addOnSuccessListener {
                                                    Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                                                    navController.navigate(Screen.SignInScreen.route)
                                                }
                                                .addOnFailureListener {
                                                    Toast.makeText(context, "Firestore Error: ${it.message}", Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                    } else {
                                        Toast.makeText(context, "Auth Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sign Up", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row {
                    Text("Already Have an Account? ", color = Color.White)
                    Text(
                        "Sign in",
                        color = Color(0xFFB0C4DE),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.SignInScreen.route)
                        }
                    )
                }
            }
        }
    }
}
