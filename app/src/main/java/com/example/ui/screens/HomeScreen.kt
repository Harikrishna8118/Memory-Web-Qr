package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.ui.components.ThemeTogglePill
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.MemoryWebItem
import com.example.data.model.OccasionType
import com.example.ui.theme.GoldenGlow
import com.example.ui.theme.RosePrimary
import com.example.ui.viewmodel.MemoryViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun HomeScreen(
    viewModel: MemoryViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val memoryWebs by viewModel.memoryWebs.collectAsState()
    val systemDark = isSystemInDarkTheme()
    val userDarkTheme by viewModel.isDarkTheme.collectAsState()
    val isDark = userDarkTheme ?: systemDark

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Hero Header Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hero_memory_web),
                    contentDescription = "Memory Web QR Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay for readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.5f),
                                    Color.Black.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                // Night/Day Theme Toggle Button at top right
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    ThemeTogglePill(
                        isDark = isDark,
                        onToggle = { viewModel.toggleDarkTheme(systemDark) }
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Surface(
                        color = Color(0xFFE11D48),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "✨ MEMORY WEB QR ✨",
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Turn Your Memories Into a Wish That Lasts Forever!",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp,
                        lineHeight = 26.sp
                    )
                    Text(
                        text = "Scan it today. Relive it tomorrow. ♾️",
                        color = Color(0xFFFFD1DC),
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Promotional Offer Banner (₹199 for 2 Web QRs)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .testTag("special_offer_card"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF2A1024) else Color(0xFFFFF1F2)
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, if (isDark) Color(0xFF881337) else Color(0xFFFDA4AF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color(0xFFBE123C),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "🎁 LIMITED TIME OFFER",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Text(
                            text = "₹199 for 2 Web QRs",
                            fontWeight = FontWeight.Black,
                            color = if (isDark) Color(0xFFF472B6) else Color(0xFF9F1239),
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Get 2 Personalized Interactive Webpages with Custom Printable QR Codes! Complete with heartfelt messages, music, photos & unboxing effects.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isDark) Color(0xFFFCE7F3) else Color(0xFF4C0519),
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.navigateTo(Screen.OrderForm) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("order_now_offer_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE11D48)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Order on WhatsApp", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        OutlinedButton(
                            onClick = { viewModel.startNewDraft(OccasionType.LOVE) },
                            modifier = Modifier.testTag("create_draft_offer_button"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Design Web", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // How It Works (4-Step Flow)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "How Memory Web QR Works",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StepItem(step = "1", title = "Share Photos", emoji = "📸", modifier = Modifier.weight(1f))
                    StepItem(step = "2", title = "We Craft Web", emoji = "💻", modifier = Modifier.weight(1f))
                    StepItem(step = "3", title = "Get Your QR", emoji = "📱", modifier = Modifier.weight(1f))
                    StepItem(step = "4", title = "Relive Forever", emoji = "💖", modifier = Modifier.weight(1f))
                }
            }
        }

        // "Perfect For" Categories Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "💝 Perfect For Every Occasion",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Tap any category to design a custom Memory Web",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(OccasionType.values()) { occasion ->
                        OccasionChipCard(
                            occasion = occasion,
                            onClick = { viewModel.startNewDraft(occasion) }
                        )
                    }
                }
            }
        }

        // Live Inspiration Showcase
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "✨ Interactive Live Showcase",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Tap to experience sample interactive memory pages",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                memoryWebs.take(4).forEach { sampleItem ->
                    SampleWebCard(
                        item = sampleItem,
                        onViewWeb = { viewModel.openWebForViewing(sampleItem) },
                        onViewQr = { viewModel.openQrStudio(sampleItem) },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }

        // Quick WhatsApp Concierge Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF14532D)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "💬 WhatsApp Concierge",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Call or chat: 9880035450\nInstant setup for your special day",
                            color = Color(0xFFBBF7D0),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                    IconButton(
                        onClick = { viewModel.launchQuickWhatsAppInquiry(context) },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF22C55E), CircleShape)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = "WhatsApp", tint = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun StepItem(step: String, title: String, emoji: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$step. $title",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun OccasionChipCard(
    occasion: OccasionType,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = occasion.emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = occasion.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Tap to design",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun SampleWebCard(
    item: MemoryWebItem,
    onViewWeb: () -> Unit,
    onViewQr: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .testTag("sample_web_${item.id}"),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = item.occasion.emoji, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = item.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "For ${item.recipientName} • ${item.milestoneNote}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    color = Color(0xFFFFE4E6),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = item.occasion.title,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE11D48),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = item.heartfeltLetter,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onViewWeb,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Interactive Web", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onViewQr,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("QR Gift Card", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
