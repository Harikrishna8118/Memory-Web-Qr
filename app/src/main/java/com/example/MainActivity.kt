package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.CreatorScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MemoryWebViewerScreen
import com.example.ui.screens.OrderDeskScreen
import com.example.ui.screens.QrCardStudioScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MemoryViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    private val viewModel: MemoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemDark = androidx.compose.foundation.isSystemInDarkTheme()
            val userDarkTheme by viewModel.isDarkTheme.collectAsState()
            val isDark = userDarkTheme ?: systemDark

            MyApplicationTheme(darkTheme = isDark) {
                MainAppScaffold(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScaffold(viewModel: MemoryViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            // Hide bottom bar in full-screen interactive viewer for immersion
            if (currentScreen != Screen.Viewer) {
                NavigationBar(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("main_bottom_nav"),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    NavigationBarItem(
                        selected = currentScreen is Screen.Home,
                        onClick = { viewModel.navigateTo(Screen.Home) },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen is Screen.Home) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Home"
                            )
                        },
                        label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.testTag("nav_home")
                    )

                    NavigationBarItem(
                        selected = currentScreen is Screen.Creator,
                        onClick = { viewModel.navigateTo(Screen.Creator) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = "Create Web"
                            )
                        },
                        label = { Text("Studio", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.testTag("nav_creator")
                    )

                    NavigationBarItem(
                        selected = currentScreen is Screen.QrStudio,
                        onClick = { viewModel.navigateTo(Screen.QrStudio) },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen is Screen.QrStudio) Icons.Filled.QrCode else Icons.Outlined.QrCode,
                                contentDescription = "QR Studio"
                            )
                        },
                        label = { Text("QR Card", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.testTag("nav_qr")
                    )

                    NavigationBarItem(
                        selected = currentScreen is Screen.OrderDesk || currentScreen is Screen.OrderForm,
                        onClick = { viewModel.navigateTo(Screen.OrderForm) },
                        icon = {
                            BadgedBox(badge = {
                                Badge(containerColor = Color(0xFFE11D48)) {
                                    Text("₹199", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }) {
                                Icon(
                                    imageVector = if (currentScreen is Screen.OrderDesk || currentScreen is Screen.OrderForm) Icons.Filled.Chat else Icons.Outlined.Chat,
                                    contentDescription = "Order"
                                )
                            }
                        },
                        label = { Text("Order", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.testTag("nav_order")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                is Screen.Home -> HomeScreen(viewModel = viewModel)
                is Screen.Creator -> CreatorScreen(viewModel = viewModel)
                is Screen.Viewer -> MemoryWebViewerScreen(viewModel = viewModel)
                is Screen.QrStudio -> QrCardStudioScreen(viewModel = viewModel)
                is Screen.OrderDesk -> OrderDeskScreen(viewModel = viewModel)
                is Screen.OrderForm -> com.example.ui.screens.OrderFormScreen(viewModel = viewModel)
            }
        }
    }
}
