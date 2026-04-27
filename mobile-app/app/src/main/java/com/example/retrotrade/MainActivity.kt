package com.example.retrotrade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.retrotrade.ui.navigation.AppNavigation
import com.example.retrotrade.ui.theme.RetroTradeTheme
import com.google.firebase.auth.FirebaseAuth


val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetroTradeTheme {
                var navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}