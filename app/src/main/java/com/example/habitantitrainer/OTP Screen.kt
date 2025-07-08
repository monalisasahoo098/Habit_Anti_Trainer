package com.example.habitantitrainer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OtpScreen(
    navController: NavHostController,
    verificationId: String,
    phone: String,
    email: String,
    password: String,
    name: String
) {
    val otpDigits = remember { List(6) { mutableStateOf("") } }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Enter OTP",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    otpDigits.forEachIndexed { index, state ->
                        OutlinedTextField(
                            value = state.value,
                            onValueChange = {
                                if (it.length <= 1 && it.all { c -> c.isDigit() }) {
                                    otpDigits[index].value = it
                                    if (it.isNotEmpty()) {
                                        if (index < 5) {
                                            focusRequesters[index + 1].requestFocus()
                                        } else {
                                            keyboardController?.hide()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .width(48.dp)
                                .height(56.dp)
                                .focusRequester(focusRequesters[index]),
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                color = Color.White,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4FC3F7),
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF4FC3F7), Color(0xFF0288D1))
                            )
                        )
                        .clickable {
                            val otp = otpDigits.joinToString("") { it.value }

                            if (otp.length == 6 && verificationId.isNotBlank()) {
                                val credential: PhoneAuthCredential =
                                    PhoneAuthProvider.getCredential(verificationId, otp)

                                auth.signInWithCredential(credential)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // Store user in Firestore
                                            val userMap = hashMapOf(
                                                "name" to name,
                                                "email" to email,
                                                "phone" to phone,
                                                "password" to password
                                            )
                                            db.collection("users")
                                                .document(phone)
                                                .set(userMap)
                                                .addOnSuccessListener {
                                                    Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show()
                                                    navController.navigate(Screen.AboutScreen.route) {
                                                        popUpTo("otp_screen/{verificationId}/{phone}/{email}/{password}/{name}") {
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(context, "Firestore error: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(context, "Enter valid 6-digit OTP", Toast.LENGTH_SHORT).show()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Verify OTP",
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
