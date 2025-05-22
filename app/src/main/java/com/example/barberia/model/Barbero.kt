    package com.example.barberia.model

    import com.example.barberia.R
    import com.google.gson.annotations.SerializedName

    data class Barbero(
        val idBarbero: Long? = null,
        val nombre: String,
        val telefono: String?,
        val usuario: String?,
        val fotoUrl: String? = null,
        @SerializedName("contraseña") val contrasenia: String,
        @SerializedName("id_admin") val idAdministrador: Long // <-- Usa @SerializedName("id_admin")
    )
     {
        fun fotoResId(): Int = when (nombre.lowercase().trim()) {
            "andrés ramirez" -> R.drawable.foto_andres_ramirez
            "luis torres" -> R.drawable.foto_luis_torres
            "mateo hernandez" -> R.drawable.foto_mateo_hernandez
            "diego perez" -> R.drawable.foto_diego_perez
            "samuel moreno" -> R.drawable.foto_samuel_moreno
            else -> R.drawable.ic_barbero_placeholder
        }
    }

