package com.example.myapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapp.ui.theme.MyAppTheme


const val VALID_LOGIN = "admin"
const val VALID_PASSWORD = "admin"
const val MAX_ATTEMPTS = 3


fun isValidLogin(login: String, password: String): Boolean {
    return login.trim() == VALID_LOGIN && password == VALID_PASSWORD
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var loggedIn by rememberSaveable { mutableStateOf(false) }

                    Box(modifier = Modifier.padding(innerPadding)) {
                        if (loggedIn) {
                            WelcomeScreen(onLogout = { loggedIn = false })
                        } else {
                            LoginScreen(onLoginSuccess = { loggedIn = true })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var failedAttempts by rememberSaveable { mutableStateOf(0) }

    val context = LocalContext.current
    val mainColor = Color(0xFFF2735F)

    val attemptsLeft = MAX_ATTEMPTS - failedAttempts
    val isBlocked = attemptsLeft <= 0

    fun onLoginClick() {
        if (isBlocked) return

        if (isValidLogin(login, password)) {
            Log.i("my log", "login: correct")
            onLoginSuccess()
        } else {
            failedAttempts++
            password = ""

            val message = if (failedAttempts >= MAX_ATTEMPTS) {
                "Too many failed attempts. Login is blocked."
            } else {
                "Incorrect login or password"
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            Log.i("my log", "login: failed attempt $failedAttempts of $MAX_ATTEMPTS")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(mainColor, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "\uD83D\uDD12",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Login") },
            singleLine = true,
            enabled = !isBlocked,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            enabled = !isBlocked,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onLoginClick() },
            enabled = !isBlocked,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = mainColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log in", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isBlocked) {
            Text(
                text = "Too many failed attempts.\nLogin is blocked.",
                color = Color.Red,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        } else if (failedAttempts > 0) {
            Text(
                text = "Incorrect login or password.\nAttempts left: $attemptsLeft",
                color = Color.Red,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun WelcomeScreen(onLogout: () -> Unit) {
    val okColor = Color(0xFF4CAF50)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Green check
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(okColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "\u2713",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome to the application",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You are logged in as $VALID_LOGIN",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onLogout,
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Log out")
        }
    }
}
