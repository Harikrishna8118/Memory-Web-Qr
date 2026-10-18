package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.export.WebExporter
import com.example.data.model.AmbientMusic
import com.example.data.model.InteractiveFeature
import com.example.data.model.MemoryWebItem
import com.example.data.model.OccasionType
import com.example.ui.components.FloatingHeartsHost
import com.example.ui.components.FloatingParticle
import com.example.ui.components.InteractiveBirthdayCandle
import com.example.ui.components.InteractiveLoveEnvelope
import com.example.ui.components.InteractiveScratchCard
import com.example.ui.viewmodel.MemoryViewModel
import com.example.ui.viewmodel.Screen
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun MemoryWebViewerScreen(
    viewModel: MemoryViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val item = viewModel.selectedWeb.collectAsState().value ?: return
    val isMusicPlaying by viewModel.isMusicPlaying.collectAsState()
    val scope = rememberCoroutineScope()

    // Handle Android system back press
    BackHandler {
        viewModel.stopMusic()
        viewModel.navigateTo(Screen.Home)
    }

    val floatingParticles = remember { mutableStateListOf<FloatingParticle>() }

    fun spawnHeartBurst() {
        val emojis = listOf("💖", "💕", "✨", "❤️", "🌸", "⭐")
        repeat(5) {
            val p = FloatingParticle(
                startX = Random.nextFloat() * 600f,
                size = Random.nextFloat() * 16f + 20f,
                emoji = emojis.random()
            )
            floatingParticles.add(p)
            scope.launch {
                p.anim.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
                )
                floatingParticles.remove(p)
            }
        }
    }

    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(item.themeStyle.bgStart),
            Color(item.themeStyle.bgEnd),
            Color(item.themeStyle.bgStart)
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgGradient)
            .clickable { spawnHeartBurst() }
    ) {
        // Floating Hearts Layer
        FloatingHeartsHost(particles = floatingParticles)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Top Bar with Back, Music and Share
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            viewModel.stopMusic()
                            viewModel.navigateTo(Screen.Home)
                        },
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.85f), CircleShape)
                            .shadow(2.dp, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(item.themeStyle.accent)
                        )
                    }

                    Surface(
                        color = Color.White.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(14.dp),
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✨ MEMORY WEB QR",
                                fontWeight = FontWeight.Bold,
                                color = Color(item.themeStyle.accent),
                                fontSize = 11.sp,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (item.ambientMusic != AmbientMusic.NONE) {
                            IconButton(
                                onClick = { viewModel.toggleMusic() },
                                modifier = Modifier
                                    .background(
                                        if (isMusicPlaying) Color(item.themeStyle.accent) else Color.White.copy(alpha = 0.85f),
                                        CircleShape
                                    )
                                    .shadow(2.dp, CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (isMusicPlaying) Icons.Default.MusicNote else Icons.Default.MusicOff,
                                    contentDescription = "Toggle Music",
                                    tint = if (isMusicPlaying) Color.White else Color(item.themeStyle.accent)
                                )
                            }
                        }

                        IconButton(
                            onClick = { WebExporter.shareHtmlPage(context, item) },
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.85f), CircleShape)
                                .shadow(2.dp, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color(item.themeStyle.accent)
                            )
                        }
                    }
                }
            }

            // Audio Visualizer Bar if playing
            if (isMusicPlaying && item.ambientMusic != AmbientMusic.NONE) {
                item {
                    MusicPillIndicator(
                        musicName = item.ambientMusic.displayName,
                        accentColor = Color(item.themeStyle.accent)
                    )
                }
            }

            // Web Header Banner
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = item.occasion.emoji, fontSize = 42.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(item.themeStyle.accent),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Specially for ${item.recipientName}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F1218),
                        textAlign = TextAlign.Center
                    )
                    if (item.milestoneNote.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            color = Color(0xFFFEF3C7),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "★ ${item.milestoneNote} ★",
                                color = Color(0xFFB45309),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Interactive Wax Seal Love Letter
            if (item.interactiveFeatures.contains(InteractiveFeature.ENVELOPE_UNFOLD)) {
                item {
                    InteractiveLoveEnvelope(
                        recipientName = item.recipientName,
                        senderName = item.senderName,
                        letterText = item.heartfeltLetter
                    )
                }
            } else {
                // Standard Card if envelope disabled
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = item.heartfeltLetter,
                                style = MaterialTheme.typography.bodyLarge,
                                fontStyle = FontStyle.Italic,
                                lineHeight = 26.sp,
                                color = Color(0xFF374151)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "— With love, ${item.senderName}",
                                fontWeight = FontWeight.Bold,
                                color = Color(item.themeStyle.accent),
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // Interactive Birthday Candle if enabled or occasion is Birthday
            if (item.interactiveFeatures.contains(InteractiveFeature.CANDLE_BLOW) ||
                item.occasion == OccasionType.BIRTHDAY
            ) {
                item {
                    InteractiveBirthdayCandle(
                        onCandleBlown = { spawnHeartBurst() }
                    )
                }
            }

            // Photo Polaroids Section
            if (item.photos.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "📸 Captured Memories",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(item.themeStyle.accent)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                        ) {
                            items(item.photos) { photo ->
                                PolaroidPhotoCard(
                                    caption = photo.caption,
                                    milestone = photo.milestoneYear
                                )
                            }
                        }
                    }
                }
            }

            // Interactive Scratch Card if enabled
            if (item.interactiveFeatures.contains(InteractiveFeature.SCRATCH_REVEAL)) {
                item {
                    InteractiveScratchCard(secretNote = item.secretScratchNote)
                }
            }

            // Send Love Button
            item {
                Button(
                    onClick = { spawnHeartBurst() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("send_love_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(item.themeStyle.accent)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tap to Send Love 💖",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }

            // QR Gift Card & Ordering Actions
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎁 Your Keepsake QR Code",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(item.themeStyle.accent)
                        )
                        Text(
                            text = "Generate a printable keepsake card to give in person or slip into a gift box!",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.openQrStudio(item) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("open_qr_studio_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(item.themeStyle.accent)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("QR Card", fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.updateOrderForm { form ->
                                        form.copy(
                                            eventType = item.occasion,
                                            recipientName = item.recipientName,
                                            web1Recipient = item.recipientName,
                                            web1Occasion = item.occasion,
                                            specialMessage = item.heartfeltLetter
                                        )
                                    }
                                    viewModel.navigateTo(Screen.OrderForm)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Order QRs", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MusicPillIndicator(
    musicName: String,
    accentColor: Color
) {
    val infiniteTransition = rememberInfiniteTransition(label = "musicBar")
    val barHeight1 by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(tween(400, easing = LinearEasing), RepeatMode.Reverse),
        label = "b1"
    )
    val barHeight2 by infiniteTransition.animateFloat(
        initialValue = 18f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(350, easing = LinearEasing), RepeatMode.Reverse),
        label = "b2"
    )
    val barHeight3 by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 22f,
        animationSpec = infiniteRepeatable(tween(450, easing = LinearEasing), RepeatMode.Reverse),
        label = "b3"
    )

    Surface(
        color = Color.White.copy(alpha = 0.9f),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.MusicNote, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Playing: $musicName",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = accentColor
                )
            }

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.height(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(barHeight1.dp)
                        .background(accentColor, RoundedCornerShape(2.dp))
                )
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(barHeight2.dp)
                        .background(accentColor, RoundedCornerShape(2.dp))
                )
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(barHeight3.dp)
                        .background(accentColor, RoundedCornerShape(2.dp))
                )
            }
        }
    }
}

@Composable
private fun PolaroidPhotoCard(
    caption: String,
    milestone: String
) {
    Surface(
        modifier = Modifier
            .width(180.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp)),
        color = Color.White,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Polaroid Photo Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFFFE4E6), Color(0xFFFECDD3))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "📸 💕", fontSize = 32.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = caption,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF1F1218),
                textAlign = TextAlign.Center
            )
            if (milestone.isNotBlank()) {
                Text(
                    text = milestone,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
