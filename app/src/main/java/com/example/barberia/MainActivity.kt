package com.example.barberia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.barberia.di.AppModule
import com.example.barberia.interfaces.RetrofitClient
import com.example.barberia.screens.Navegacion
import com.example.barberia.ui.theme.BarberiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar RetrofitClient con el contexto
        RetrofitClient.initialize(this)
        
        setContent {
            BarberiaTheme {
                val navController = rememberNavController()
                Navegacion(navController)
            }
        }
    }
}

