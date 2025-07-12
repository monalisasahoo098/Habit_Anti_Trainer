package com.example.habitantitrainer

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.habitantitrainer.Screen


data class BarDataThree(val xValue: String, val yValue: Float)

@Composable
fun DashboardScreen(navController: NavHostController) {
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFE6F0FA), Color.White)
    )

    val barChartDataThree = listOf(
        BarDataThree("Mon", 3f),
        BarDataThree("Tue", 5f),
        BarDataThree("Wed", 2f),
        BarDataThree("Thu", 4f),
        BarDataThree("Fri", 1f),
        BarDataThree("Sat", 6f),
        BarDataThree("Sun", 3f)
    )

    var userName by remember { mutableStateOf("User") }
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        auth.currentUser?.uid?.let { uid ->
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    userName = doc.getString("firstName") ?: "User"
                }
                .addOnFailureListener { e ->
                    Log.e("Dashboard", "Error fetching user", e)
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3399FF))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Welcome Back, $userName 👋",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
                Column {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8FF)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("You're tracking 4 habits", color = Color(0xFF003366))
                            Text("Streak: 5 Days 💪", color = Color(0xFF3399FF), fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatCard(0.89f, "Habit Controlled", Color(0xFF7C4DFF), 0xFFEDE7F6, Modifier.weight(1f))
                        StatCard(0.82f, "Completion Rate", Color(0xFF009688), 0xFFE0F2F1, Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Weekly Progress", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color(0xFF003366))
            BarChart(barChartDataThree)

            Spacer(modifier = Modifier.height(24.dp))

            Text("Today's Challenges", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color(0xFF003366))
            ColoredTask("💀 Quit smoking", Color(0xFFD0EBFF))
            ColoredTask("🚫 Avoid Procrastination", Color(0xFFFFCDD2))
            ColoredTask("🍭 No Sugar Today", Color(0xFFFFFFC4))
            ColoredTask("⏳ Limit Screen Time", Color(0xFFE1BEE7))

            Spacer(modifier = Modifier.height(24.dp))
            ReminderCard("7:45 AM", "No Sugar Today")

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "“Small steps every day lead to big changes.”",
                    color = Color(0xFF003366),
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(Icons.Default.Home, contentDescription = "Home", tint = Color(0xFF003366))
            }

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3399FF))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }

            IconButton(onClick = {
                navController.navigate("profileDetailScreen")
            }) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color(0xFF003366))
            }
        }
    }
}

@Composable
fun StatCard(progress: Float, label: String, color: Color, bgColor: Long, modifier: Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(bgColor)),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.padding(8.dp)
    ) {
        val animatedProgress by animateFloatAsState(targetValue = progress, label = "")
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = animatedProgress,
                    color = color,
                    strokeWidth = 6.dp,
                    modifier = Modifier.size(80.dp)
                )
                Text("${(animatedProgress * 100).toInt()}%", color = Color(0xFF003366), fontWeight = FontWeight.Bold)
            }
            Text(label, color = Color(0xFF003366), fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
fun ColoredTask(text: String, backgroundColor: Color) {
    var checked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { checked = !checked }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF7B1FA2),
                    uncheckedColor = Color(0xFF003366)
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, fontWeight = FontWeight.Medium, color = Color(0xFF003366))
        }
        if (checked) {
            Text(
                "Great job! Keep going 🎉",
                color = Color(0xFF00796B),
                fontStyle = FontStyle.Italic,
                fontSize = 13.sp,
                modifier = Modifier.padding(start = 40.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun BarChart(data: List<BarDataThree>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .width(20.dp)
                        .height((it.yValue * 20).dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF90CAF9))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(it.xValue, fontSize = 10.sp, color = Color(0xFF003366))
            }
        }
    }
}

@Composable
fun ReminderCard(reminderTime: String, reminderText: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF1565C0)),
                contentAlignment = Alignment.Center
            ) {
                Text("📅", fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text("Next Reminder", fontWeight = FontWeight.Bold, color = Color(0xFF003366), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("🕒 $reminderTime – $reminderText", color = Color(0xFF00796B), fontSize = 14.sp)
            }
        }
    }
}
