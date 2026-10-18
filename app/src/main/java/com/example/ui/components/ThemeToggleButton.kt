package com.example.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Animated Theme Toggle Button to switch between Day Mode (Light) and Night Mode (Dark).
 */
@Composable
fun ThemeToggleIconButton(
    isDark: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isDark) 180f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "theme_rotation"
    )

    IconButton(
        onClick = onToggle,
        modifier = modifier
            .testTag("theme_toggle_icon_button")
            .shadow(2.dp, CircleShape)
            .background(
                if (isDark) Color(0xFF2C1631) else Color.White.copy(alpha = 0.9f),
                CircleShape
            )
    ) {
        Crossfade(targetState = isDark, label = "theme_crossfade") { dark ->
            if (dark) {
                Icon(
                    imageVector = Icons.Default.LightMode,
                    contentDescription = "Switch to Day Mode",
                    tint = Color(0xFFFFD54F),
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(rotation)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.DarkMode,
                    contentDescription = "Switch to Night Mode",
                    tint = Color(0xFF881337),
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(rotation)
                )
            }
        }
    }
}

/**
 * Pill-style theme toggle badge with label for headers.
 */
@Composable
fun ThemeTogglePill(
    isDark: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onToggle)
            .testTag("theme_toggle_pill"),
        color = if (isDark) Color(0xFF2C1631) else Color.White.copy(alpha = 0.9f),
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isDark) Color(0xFF4A2A54) else Color(0xFFFFD1DC)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                contentDescription = null,
                tint = if (isDark) Color(0xFFFFD54F) else Color(0xFF881337),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (isDark) "Night 🌙" else "Day ☀️",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color(0xFFFCE7F3) else Color(0xFF881337)
            )
        }
    }
}
