package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldenGlow
import com.example.ui.theme.RomanticRed
import com.example.ui.theme.RosePrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

data class FloatingParticle(
    val id: Long = System.nanoTime(),
    val startX: Float,
    val size: Float,
    val emoji: String = "💖",
    val anim: Animatable<Float, *> = Animatable(0f)
)

/**
 * Floating hearts & sparkles layer that bursts glowing hearts upwards on tap
 */
@Composable
fun FloatingHeartsHost(
    particles: List<FloatingParticle>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        particles.forEach { p ->
            val progress = p.anim.value
            val yOffset = -progress * 600f
            val alpha = (1f - progress).coerceIn(0f, 1f)
            val scale = 0.6f + progress * 0.8f

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = p.startX.roundToInt(),
                            y = (800f + yOffset).roundToInt()
                        )
                    }
                    .scale(scale)
                    .size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = p.emoji,
                    fontSize = (p.size).sp,
                    modifier = Modifier.padding(2.dp)
                )
            }
        }
    }
}

/**
 * Interactive Birthday Cake with Candle: tap or hold to blow the flame out!
 */
@Composable
fun InteractiveBirthdayCandle(
    onCandleBlown: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isBlown by remember { mutableStateOf(false) }
    var sparkActive by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val flameFlicker = rememberInfiniteTransition(label = "flame")
    val flameScale by flameFlicker.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(280, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flameScale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                if (!isBlown) {
                    isBlown = true
                    sparkActive = true
                    onCandleBlown()
                    scope.launch {
                        delay(2400)
                        sparkActive = false
                    }
                } else {
                    // Relight candle
                    isBlown = false
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFEF3C7)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (!isBlown) "🎂 Blow Out Your Birthday Candle!" else "✨ Wish Granted! Candle Blown Out! 🥳",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB45309),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (!isBlown) "Tap the cake to make a wish and blow the candle ✨" else "Tap again to relight the candle",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF92400E),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Candle flame visual
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
            ) {
                if (!isBlown) {
                    // Flame
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .scale(flameScale)
                            .size(24.dp, 36.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 8.dp, bottomEnd = 8.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFFFFEA00), Color(0xFFFF5722))
                                )
                            )
                    )
                } else {
                    // Smoke puff
                    Text(
                        text = "💨 ✨",
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }

                // Candle stem & cake
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp, 32.dp)
                            .background(Color(0xFFE11D48), RoundedCornerShape(4.dp))
                    )
                    Text(text = "🎂", fontSize = 54.sp)
                }
            }

            if (sparkActive) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🎉 ✨ HAPPY BIRTHDAY! ✨ 🎊",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFE11D48),
                    fontSize = 18.sp
                )
            }
        }
    }
}

/**
 * Interactive Scratch Card: Drag your finger across to scratch off the gold foil
 * and reveal the secret note!
 */
@Composable
fun InteractiveScratchCard(
    secretNote: String,
    modifier: Modifier = Modifier
) {
    var scratchPercent by remember { mutableFloatStateOf(0f) }
    val isRevealed = scratchPercent >= 60f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFBEB)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "✨", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Mystery Scratch Card",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB45309)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "✨", fontSize = 20.sp)
            }
            Text(
                text = if (!isRevealed) "Swipe your finger across to scratch & reveal the secret note" else "Secret note revealed! 💖",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF92400E),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color(0xFFFDE68A), RoundedCornerShape(16.dp))
                    .pointerInput(Unit) {
                        detectDragGestures { _, dragAmount ->
                            val distance = dragAmount.getDistance()
                            scratchPercent = (scratchPercent + distance * 0.15f).coerceAtMost(100f)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                // Revealed content underneath
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFFF1F2))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "💌 Secret Message 💌",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE11D48)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = secretNote,
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF881337),
                        textAlign = TextAlign.Center
                    )
                }

                // Scratch foil overlay
                if (!isRevealed) {
                    val alpha = (1f - (scratchPercent / 70f)).coerceIn(0f, 1f)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFFBBF24),
                                        Color(0xFFD97706),
                                        Color(0xFFFDE68A)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked Note",
                                tint = Color(0xFF78350F),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "SCRATCH HERE TO REVEAL ✨",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF78350F),
                                fontSize = 13.sp,
                                letterSpacing = 1.sp
                            )
                            if (scratchPercent > 5f) {
                                Text(
                                    text = "${scratchPercent.toInt()}% scratched",
                                    fontSize = 11.sp,
                                    color = Color(0xFF92400E)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Interactive Wax Seal Love Envelope:
 * Tap to break the wax seal and unfold the heartfelt wish letter!
 */
@Composable
fun InteractiveLoveEnvelope(
    recipientName: String,
    senderName: String,
    letterText: String,
    modifier: Modifier = Modifier
) {
    var isOpen by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable { isOpen = !isOpen },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Envelope Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💌 A Special Letter for $recipientName",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (isOpen) "Tap to fold" else "Tap to open",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            if (!isOpen) {
                // Closed Envelope with Wax Seal
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFFFFE4E6), Color(0xFFFECDD3))
                            ),
                            RoundedCornerShape(16.dp)
                        )
                        .border(1.5.dp, Color(0xFFFDA4AF), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Wax Seal Stamp
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .shadow(8.dp, CircleShape)
                                .background(Color(0xFFBE123C), CircleShape)
                                .border(2.dp, Color(0xFFFFE4E6), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "💖", fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "SEALED WITH LOVE",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF881337),
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Tap to break the seal",
                            fontSize = 12.sp,
                            color = Color(0xFF9F1239)
                        )
                    }
                }
            } else {
                // Open unfolded letter
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(16.dp)
                        )
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "Dearest $recipientName,",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = letterText,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 26.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontStyle = FontStyle.Italic
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Forever with love,\n— $senderName",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
