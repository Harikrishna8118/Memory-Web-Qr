package com.example.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Html
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.export.WebExporter
import com.example.data.qr.QrGenerator
import com.example.ui.viewmodel.MemoryViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun QrCardStudioScreen(
    viewModel: MemoryViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val item = viewModel.selectedWeb.collectAsState().value ?: return

    BackHandler {
        viewModel.navigateTo(Screen.Home)
    }

    var selectedEmblem by remember { mutableStateOf(item.qrStyle.centerEmblem) }
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var giftCardBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(item, selectedEmblem) {
        val updatedItem = item.copy(qrStyle = item.qrStyle.copy(centerEmblem = selectedEmblem))
        qrBitmap = QrGenerator.generateQrBitmap(
            content = updatedItem.webPageUrl,
            size = 512,
            foregroundColor = android.graphics.Color.parseColor("#881337"),
            centerEmblem = selectedEmblem
        )
        giftCardBitmap = QrGenerator.createPrintableGiftCard(updatedItem)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Navigation Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.Home) },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "📱 QR Gift Card Studio",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "High-Res Printable Keepsake for ${item.recipientName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Luxury Gift Card Showcase Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(18.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gift_qr_showcase),
                    contentDescription = "Gift Card Showcase",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.45f))
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Text(
                        text = "🎁 Ready to Print, Frame or Gift",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Scan with any phone camera to unlock the memory web",
                        color = Color(0xFFFFD1DC),
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Center Emblem Customization
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Center QR Emblem",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val emblems = listOf("💖", "🎂", "💍", "⭐", "🎁", "✨", "💌")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(emblems) { emblem ->
                            FilterChip(
                                selected = selectedEmblem == emblem,
                                onClick = { selectedEmblem = emblem },
                                label = { Text(emblem, fontSize = 16.sp) }
                            )
                        }
                    }
                }
            }
        }

        // Printable Gift Card Card Preview
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp))
                    .testTag("printable_card_preview"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7F8)),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFE11D48)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "✨ MEMORY WEB QR ✨",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF881337),
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "A wish that lasts forever 💖",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9F1239),
                        fontStyle = FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F1218),
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Specially for ${item.recipientName}",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE11D48),
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // QR Code Image
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .shadow(6.dp, RoundedCornerShape(16.dp))
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (qrBitmap != null) {
                            Image(
                                bitmap = qrBitmap!!.asImageBitmap(),
                                contentDescription = "Generated QR Code",
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "📱 Scan with phone camera to open",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4C0519),
                        fontSize = 13.sp
                    )
                    Text(
                        text = "With all my love, — ${item.senderName}",
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF881337),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "💖 Your Memories. Your Story. Your QR. ♾️",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFBE123C),
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Action Buttons: Share, Print, Test Open, Export
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        giftCardBitmap?.let { bmp ->
                            val uri = QrGenerator.saveBitmapToCache(context, bmp, "memory_web_card_${item.id.take(6)}.png")
                            if (uri != null) {
                                QrGenerator.shareGiftCard(
                                    context,
                                    uri,
                                    "✨ Check out this Memory Web QR created for ${item.recipientName}! Scan to relive our memories forever 💖\n${item.webPageUrl}"
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("share_qr_card_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share / Save Keepsake Card", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.openWebForViewing(item) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("test_scan_web_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Test Scan", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { WebExporter.shareHtmlPage(context, item) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Html, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export Web", fontWeight = FontWeight.Bold)
                    }
                }

                Button(
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Order Professional Cards (WhatsApp)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
