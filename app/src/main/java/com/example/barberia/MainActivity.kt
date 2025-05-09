package com.example.barberia


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.barberia.screens.Navegacion
import com.example.barberia.ui.theme.BarberiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BarberiaTheme {
                val navController = rememberNavController()
                Navegacion(navController)
            }
        }
    }
}

