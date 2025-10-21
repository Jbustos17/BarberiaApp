package com.example.barberia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberia.ui.theme.BarberiaColors
import com.example.barberia.ui.theme.BarberiaShapes

// Card moderno con gradiente y sombra
@Composable
fun ModernCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = BarberiaShapes.large,
                ambientColor = BarberiaColors.Shadow,
                spotColor = BarberiaColors.ShadowDark
            )
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        shape = BarberiaShapes.large,
        colors = CardDefaults.cardColors(
            containerColor = BarberiaColors.Surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        content()
    }
}

// Card con gradiente
@Composable
fun GradientCard(
    modifier: Modifier = Modifier,
    gradientColors: List<Color> = listOf(
        BarberiaColors.Primary,
        BarberiaColors.PrimaryVariant
    ),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = BarberiaShapes.large,
                ambientColor = BarberiaColors.Shadow,
                spotColor = BarberiaColors.ShadowDark
            )
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        shape = BarberiaShapes.large,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(gradientColors),
                    shape = BarberiaShapes.large
                )
        ) {
            Column {
                content()
            }
        }
    }
}

// Botón moderno con gradiente
@Composable
fun ModernButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    gradientColors: List<Color> = listOf(
        BarberiaColors.Primary,
        BarberiaColors.PrimaryVariant
    )
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .height(56.dp)
            .shadow(
                elevation = if (enabled) 8.dp else 0.dp,
                shape = BarberiaShapes.medium,
                ambientColor = BarberiaColors.Shadow,
                spotColor = BarberiaColors.ShadowDark
            ),
        shape = BarberiaShapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = if (enabled) gradientColors else listOf(
                            BarberiaColors.Border,
                            BarberiaColors.BorderDark
                        )
                    ),
                    shape = BarberiaShapes.medium
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = BarberiaColors.OnPrimary,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = BarberiaColors.OnPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = text,
                        color = BarberiaColors.OnPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// Botón secundario moderno
@Composable
fun ModernOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(56.dp)
            .border(
                width = 2.dp,
                color = BarberiaColors.Primary,
                shape = BarberiaShapes.medium
            ),
        shape = BarberiaShapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = BarberiaColors.Primary
        ),
        border = null
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = BarberiaColors.Primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = BarberiaColors.Primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// Campo de texto moderno
@Composable
fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = leadingIcon?.let { 
                { 
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = BarberiaColors.Primary
                    ) 
                } 
            },
            trailingIcon = trailingIcon?.let { 
                { 
                    IconButton(onClick = { onTrailingIconClick?.invoke() }) {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            tint = BarberiaColors.Primary
                        )
                    }
                } 
            },
            isError = isError,
            enabled = enabled,
            singleLine = singleLine,
            modifier = Modifier.fillMaxWidth(),
            shape = BarberiaShapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BarberiaColors.Primary,
                unfocusedBorderColor = BarberiaColors.Border,
                focusedLabelColor = BarberiaColors.Primary,
                unfocusedLabelColor = BarberiaColors.OnSurfaceVariant,
                errorBorderColor = BarberiaColors.Error,
                errorLabelColor = BarberiaColors.Error
            )
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = BarberiaColors.Error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

// Item de lista moderno
@Composable
fun ModernListItem(
    title: String,
    subtitle: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        shape = BarberiaShapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = BarberiaColors.Surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = BarberiaColors.Primary.copy(alpha = 0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = BarberiaColors.Primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = BarberiaColors.OnSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = BarberiaColors.OnSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            if (trailingIcon != null) {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = BarberiaColors.OnSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// Header moderno con gradiente
@Composable
fun ModernHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        BarberiaColors.Primary,
                        BarberiaColors.PrimaryVariant
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            if (onBackClick != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = BarberiaColors.OnPrimary.copy(alpha = 0.2f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = BarberiaColors.OnPrimary
                        )
                    }
                }
            }
            
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = BarberiaColors.OnPrimary,
                fontWeight = FontWeight.Bold
            )
            
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = BarberiaColors.OnPrimary.copy(alpha = 0.8f)
                )
            }
        }
    }
}

// Indicador de carga moderno
@Composable
fun ModernLoadingIndicator(
    modifier: Modifier = Modifier,
    text: String = "Cargando..."
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = BarberiaColors.Primary,
            modifier = Modifier.size(48.dp),
            strokeWidth = 4.dp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = BarberiaColors.OnSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

// Badge moderno
@Composable
fun ModernBadge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BarberiaColors.Primary,
    textColor: Color = BarberiaColors.OnPrimary
) {
    Box(
        modifier = modifier
            .background(
                color = color,
                shape = BarberiaShapes.small
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}

// Divider moderno
@Composable
fun ModernDivider(
    modifier: Modifier = Modifier,
    color: Color = BarberiaColors.Border
) {
    Divider(
        modifier = modifier,
        color = color,
        thickness = 1.dp
    )
}
