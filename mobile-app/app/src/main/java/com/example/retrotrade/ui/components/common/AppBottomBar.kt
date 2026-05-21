package com.example.retrotrade.ui.components.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.retrotrade.ui.navigation.Screen
import com.example.retrotrade.ui.theme.RetroIcon
import com.example.retrotrade.ui.theme.RetroOrange

@Composable
@Preview(showBackground = true)
fun AppBottomBar(
    navController: NavHostController = NavHostController(LocalContext.current)
) {

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        bottomNavTabs.forEachIndexed { index, tab ->
            val isSelected = currentRoute == tab.screen.route
            val isScanTab = tab.screen.route == Screen.Scan.route

            val animatedScale by animateFloatAsState(
                targetValue   = if (isSelected) 1.15f else 1f,
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
                label         = "tabScale"
            )

            val animatedColor by animateColorAsState(
                targetValue = if (isSelected) RetroOrange else RetroIcon,
                label       = "tabColor"
            )

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(tab.screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },

                icon = {
                    if (isScanTab) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .scale(animatedScale)
                                .shadow(
                                    elevation = if (isSelected) 8.dp else 4.dp,
                                    shape = CircleShape
                                )
                                .background(
                                    brush = Brush.linearGradient(
                                        listOf(Color(0xFFD35400), Color(0xFFE67E22))
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.label,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = tab.label,
                            tint = animatedColor,
                            modifier = Modifier
                                .size(24.dp)
                                .scale(animatedScale)
                        )
                    }
                },

                label = {
                    if (!isScanTab) {
                        Text(
                            text = tab.label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = animatedColor
                        )
                    }
                },

                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}


private data class BottomNavTab(
    val label: String,
    val screen : Screen,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val bottomNavTabs = listOf(
    BottomNavTab("Home", Screen.Home, Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavTab("Collection", Screen.Collection,  Icons.Filled.GridView, Icons.Outlined.GridView),
    BottomNavTab("Scan", Screen.Scan, Icons.Filled.CameraAlt, Icons.Outlined.CameraAlt),
    BottomNavTab("Trades", Screen.Trades, Icons.Filled.SwapHoriz, Icons.Outlined.SwapHoriz),
    BottomNavTab("Profile", Screen.Profile, Icons.Filled.Person, Icons.Outlined.Person)
)


