package com.example.barberia.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Paleta de colores mejorada
object BarberiaColors {
    // Colores principales
    val Primary = Color(0xFF2E7D32) // Verde más elegante
    val PrimaryVariant = Color(0xFF1B5E20) // Verde oscuro
    val Secondary = Color(0xFFD4AF37) // Dorado elegante
    val SecondaryVariant = Color(0xFFB8860B) // Dorado oscuro
    
    // Colores de superficie
    val Surface = Color(0xFFFAFAFA) // Blanco suave
    val SurfaceVariant = Color(0xFFF5F5F5) // Gris muy claro
    val Background = Color(0xFFFFFFFF) // Blanco puro
    val BackgroundDark = Color(0xFF121212) // Negro suave
    
    // Colores de estado
    val Success = Color(0xFF4CAF50) // Verde éxito
    val Warning = Color(0xFFFF9800) // Naranja advertencia
    val Error = Color(0xFFE53935) // Rojo error
    val Info = Color(0xFF2196F3) // Azul información
    
    // Colores de texto
    val OnPrimary = Color(0xFFFFFFFF)
    val OnSecondary = Color(0xFF000000)
    val OnSurface = Color(0xFF1C1B1F)
    val OnBackground = Color(0xFF1C1B1F)
    val OnSurfaceVariant = Color(0xFF49454F)
    
    // Colores de acento
    val Accent = Color(0xFF6A1B9A) // Púrpura elegante
    val AccentVariant = Color(0xFF4A148C) // Púrpura oscuro
    
    // Colores de sombra y elevación
    val Shadow = Color(0x1A000000) // Sombra suave
    val ShadowDark = Color(0x33000000) // Sombra más intensa
    
    // Colores de borde
    val Border = Color(0xFFE0E0E0) // Borde suave
    val BorderDark = Color(0xFFBDBDBD) // Borde más visible
}

// Sistema de tipografía mejorado
val BarberiaTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)

// Sistema de formas mejorado
val BarberiaShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp),
)

// Esquema de colores claro
val BarberiaLightColorScheme = lightColorScheme(
    primary = BarberiaColors.Primary,
    onPrimary = BarberiaColors.OnPrimary,
    primaryContainer = BarberiaColors.PrimaryVariant,
    onPrimaryContainer = BarberiaColors.OnPrimary,
    secondary = BarberiaColors.Secondary,
    onSecondary = BarberiaColors.OnSecondary,
    secondaryContainer = BarberiaColors.SecondaryVariant,
    onSecondaryContainer = BarberiaColors.OnSecondary,
    tertiary = BarberiaColors.Accent,
    onTertiary = BarberiaColors.OnPrimary,
    tertiaryContainer = BarberiaColors.AccentVariant,
    onTertiaryContainer = BarberiaColors.OnPrimary,
    error = BarberiaColors.Error,
    onError = BarberiaColors.OnPrimary,
    errorContainer = BarberiaColors.Error,
    onErrorContainer = BarberiaColors.OnPrimary,
    background = BarberiaColors.Background,
    onBackground = BarberiaColors.OnBackground,
    surface = BarberiaColors.Surface,
    onSurface = BarberiaColors.OnSurface,
    surfaceVariant = BarberiaColors.SurfaceVariant,
    onSurfaceVariant = BarberiaColors.OnSurfaceVariant,
    outline = BarberiaColors.Border,
    outlineVariant = BarberiaColors.BorderDark,
    scrim = BarberiaColors.Shadow,
    inverseSurface = BarberiaColors.BackgroundDark,
    inversePrimary = BarberiaColors.Primary,
)

// Esquema de colores oscuro
val BarberiaDarkColorScheme = darkColorScheme(
    primary = BarberiaColors.Primary,
    onPrimary = BarberiaColors.OnPrimary,
    primaryContainer = BarberiaColors.PrimaryVariant,
    onPrimaryContainer = BarberiaColors.OnPrimary,
    secondary = BarberiaColors.Secondary,
    onSecondary = BarberiaColors.OnSecondary,
    secondaryContainer = BarberiaColors.SecondaryVariant,
    onSecondaryContainer = BarberiaColors.OnSecondary,
    tertiary = BarberiaColors.Accent,
    onTertiary = BarberiaColors.OnPrimary,
    tertiaryContainer = BarberiaColors.AccentVariant,
    onTertiaryContainer = BarberiaColors.OnPrimary,
    error = BarberiaColors.Error,
    onError = BarberiaColors.OnPrimary,
    errorContainer = BarberiaColors.Error,
    onErrorContainer = BarberiaColors.OnPrimary,
    background = BarberiaColors.BackgroundDark,
    onBackground = BarberiaColors.OnPrimary,
    surface = BarberiaColors.BackgroundDark,
    onSurface = BarberiaColors.OnPrimary,
    surfaceVariant = BarberiaColors.SurfaceVariant,
    onSurfaceVariant = BarberiaColors.OnSurfaceVariant,
    outline = BarberiaColors.Border,
    outlineVariant = BarberiaColors.BorderDark,
    scrim = BarberiaColors.ShadowDark,
    inverseSurface = BarberiaColors.Surface,
    inversePrimary = BarberiaColors.Primary,
)
