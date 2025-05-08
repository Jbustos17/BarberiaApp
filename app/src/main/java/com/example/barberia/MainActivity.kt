package com.example.barberia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.example.barberia.ui.theme.BarberiaTheme
import androidx.navigation.compose.rememberNavController
import com.example.barberia.Screens.Navegacion
import com.example.barberia.Screens.ServicioScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServicioScreen()
        }

    }
}

