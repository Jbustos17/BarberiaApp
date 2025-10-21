package com.example.barberia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberia.ui.theme.BarberiaColors
import com.example.barberia.ui.theme.BarberiaShapes

// Bottom Navigation moderno
@Composable
fun ModernBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val navigationItems = listOf(
        NavigationItem(
            route = "inicio",
            icon = Icons.Default.Home,
            label = "Inicio"
        ),
        NavigationItem(
            route = "servicios",
            icon = Icons.Default.Build,
            label = "Servicios"
        ),
        NavigationItem(
            route = "reservas",
            icon = Icons.Default.CalendarToday,
            label = "Reservas"
        ),
        NavigationItem(
            route = "perfil",
            icon = Icons.Default.Person,
            label = "Perfil"
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                ambientColor = BarberiaColors.Shadow,
                spotColor = BarberiaColors.ShadowDark
            ),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = BarberiaColors.Surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationItems.forEach { item ->
                ModernBottomNavItem(
                    item = item,
                    isSelected = currentRoute == item.route,
                    onClick = { onNavigate(item.route) }
                )
            }
        }
    }
}

@Composable
private fun ModernBottomNavItem(
    item: NavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = if (isSelected) {
                        BarberiaColors.Primary.copy(alpha = 0.1f)
                    } else {
                        Color.Transparent
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = if (isSelected) {
                    BarberiaColors.Primary
                } else {
                    BarberiaColors.OnSurfaceVariant
                },
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = item.label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) {
                BarberiaColors.Primary
            } else {
                BarberiaColors.OnSurfaceVariant
            },
            textAlign = TextAlign.Center
        )
    }
}

data class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

// Floating Action Button moderno
@Composable
fun ModernFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    contentDescription: String = "Agregar"
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .size(64.dp)
            .shadow(
                elevation = 12.dp,
                shape = CircleShape,
                ambientColor = BarberiaColors.Shadow,
                spotColor = BarberiaColors.ShadowDark
            ),
        containerColor = BarberiaColors.Primary,
        contentColor = BarberiaColors.OnPrimary,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(28.dp)
        )
    }
}

// App Bar moderno
@Composable
fun ModernAppBar(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                ambientColor = BarberiaColors.Shadow,
                spotColor = BarberiaColors.ShadowDark
            ),
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = BarberiaColors.Surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBackClick != null) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = BarberiaColors.Primary.copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = BarberiaColors.Primary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = BarberiaColors.OnSurface,
                    fontWeight = FontWeight.Bold
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = BarberiaColors.OnSurfaceVariant
                    )
                }
            }
            
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                actions()
            }
        }
    }
}

// Tab moderno
@Composable
fun ModernTabRow(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = BarberiaShapes.medium,
                ambientColor = BarberiaColors.Shadow,
                spotColor = BarberiaColors.ShadowDark
            ),
        shape = BarberiaShapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = BarberiaColors.Surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            contentColor = BarberiaColors.Primary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = BarberiaColors.Primary,
                    height = 3.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) {
                                FontWeight.SemiBold
                            } else {
                                FontWeight.Normal
                            },
                            color = if (selectedTabIndex == index) {
                                BarberiaColors.Primary
                            } else {
                                BarberiaColors.OnSurfaceVariant
                            }
                        )
                    }
                )
            }
        }
    }
}

// Drawer moderno
@Composable
fun ModernDrawerContent(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val drawerItems = listOf(
        DrawerItem(
            route = "inicio",
            icon = Icons.Default.Home,
            label = "Inicio"
        ),
        DrawerItem(
            route = "servicios",
            icon = Icons.Default.Build,
            label = "Servicios"
        ),
        DrawerItem(
            route = "reservas",
            icon = Icons.Default.CalendarToday,
            label = "Mis Reservas"
        ),
        DrawerItem(
            route = "perfil",
            icon = Icons.Default.Person,
            label = "Mi Perfil"
        ),
        DrawerItem(
            route = "admin",
            icon = Icons.Default.AdminPanelSettings,
            label = "Panel Admin"
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BarberiaColors.Surface)
    ) {
        // Header del drawer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            BarberiaColors.Primary,
                            BarberiaColors.PrimaryVariant
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "Barbería App",
                    style = MaterialTheme.typography.headlineMedium,
                    color = BarberiaColors.OnPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tu barbería de confianza",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BarberiaColors.OnPrimary.copy(alpha = 0.8f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Items del drawer
        drawerItems.forEach { item ->
            ModernDrawerItem(
                item = item,
                isSelected = currentRoute == item.route,
                onClick = {
                    onNavigate(item.route)
                    onCloseDrawer()
                }
            )
        }
    }
}

@Composable
private fun ModernDrawerItem(
    item: DrawerItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = BarberiaShapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                BarberiaColors.Primary.copy(alpha = 0.1f)
            } else {
                Color.Transparent
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = if (isSelected) {
                    BarberiaColors.Primary
                } else {
                    BarberiaColors.OnSurfaceVariant
                },
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = item.label,
                style = MaterialTheme.typography.titleMedium,
                color = if (isSelected) {
                    BarberiaColors.Primary
                } else {
                    BarberiaColors.OnSurface
                },
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

data class DrawerItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

