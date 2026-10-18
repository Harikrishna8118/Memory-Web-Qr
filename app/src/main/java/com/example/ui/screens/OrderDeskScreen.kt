package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OccasionType
import com.example.ui.viewmodel.MemoryViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun OrderDeskScreen(
    viewModel: MemoryViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val form by viewModel.orderForm.collectAsState()

    BackHandler {
        viewModel.navigateTo(Screen.Home)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.Home) },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "📲 Order Your Memory Web QRs",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Official WhatsApp Concierge • 9880035450",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Promotional Price Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("pricing_banner"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2)),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFDA4AF)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
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
                                text = "SPECIAL OFFER",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                letterSpacing = 1.sp
                            )
                        }

                        Text(
                            text = "₹199 ONLY",
                            fontWeight = FontWeight.Black,
                            fontSize = 24.sp,
                            color = Color(0xFF9F1239)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "2 Personalized Web QRs Included!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color(0xFF1F1218)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    IncludedBenefitItem("2 Custom Interactive Webpages (Accessible anytime)")
                    IncludedBenefitItem("Personalized Printable High-Res QR Codes")
                    IncludedBenefitItem("Interactive Envelope, Candle Blow & Scratch Cards")
                    IncludedBenefitItem("Ambient Melody Audio Integration")
                    IncludedBenefitItem("Fast 24-Hour WhatsApp Delivery")

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { viewModel.navigateTo(Screen.OrderForm) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("open_streamlined_order_form_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("⚡ Fill Streamlined Order Form", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // WhatsApp Direct Actions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { viewModel.launchQuickWhatsAppInquiry(context) },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("whatsapp_chat_direct_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Chat on WhatsApp", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = { viewModel.callDirect(context) },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("call_direct_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call 9880035450", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        // Order Details Pre-fill Form
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Pre-fill Your 2 Web QRs",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "We will send your finalized designs directly to your WhatsApp!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = form.customerName,
                        onValueChange = { name -> viewModel.updateOrderForm { it.copy(customerName = name) } },
                        label = { Text("Your Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("order_name_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = form.customerPhone,
                        onValueChange = { ph -> viewModel.updateOrderForm { it.copy(customerPhone = ph) } },
                        label = { Text("Your WhatsApp Number") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("order_phone_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    // Web QR 1
                    Text(
                        text = "🌐 Web QR 1 Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = form.web1Recipient,
                        onValueChange = { r -> viewModel.updateOrderForm { it.copy(web1Recipient = r) } },
                        label = { Text("Recipient 1 Name (e.g. Priya)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(OccasionType.BIRTHDAY, OccasionType.ANNIVERSARY, OccasionType.LOVE).forEach { occ ->
                            FilterChip(
                                selected = form.web1Occasion == occ,
                                onClick = { viewModel.updateOrderForm { it.copy(web1Occasion = occ) } },
                                label = { Text("${occ.emoji} ${occ.title}", fontSize = 11.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    // Web QR 2
                    Text(
                        text = "🌐 Web QR 2 Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = form.web2Recipient,
                        onValueChange = { r -> viewModel.updateOrderForm { it.copy(web2Recipient = r) } },
                        label = { Text("Recipient 2 Name (e.g. Mom & Dad)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(OccasionType.FAMILY, OccasionType.GRADUATION, OccasionType.SPECIAL_GIFT).forEach { occ ->
                            FilterChip(
                                selected = form.web2Occasion == occ,
                                onClick = { viewModel.updateOrderForm { it.copy(web2Occasion = occ) } },
                                label = { Text("${occ.emoji} ${occ.title}", fontSize = 11.sp) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = form.specialNotes,
                        onValueChange = { n -> viewModel.updateOrderForm { it.copy(specialNotes = n) } },
                        label = { Text("Special Requests / Delivery Notes") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.sendOrderToWhatsApp(context) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("submit_whatsapp_order_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Send Order via WhatsApp (₹199)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }

        // FAQs
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Frequently Asked Questions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    FaqItem(
                        question = "How will I share photos and messages?",
                        answer = "Once you send the order on WhatsApp, you can share all your high-res photos, songs, and notes directly on the chat!"
                    )
                    FaqItem(
                        question = "How long does it take?",
                        answer = "Your custom interactive Web QRs are crafted within 24 to 48 hours and delivered via WhatsApp."
                    )
                    FaqItem(
                        question = "How long does the QR code webpage last?",
                        answer = "Your Memory Web link is active indefinitely, so you and your loved ones can relive the moment anytime, forever!"
                    )
                }
            }
        }
    }
}

@Composable
private fun IncludedBenefitItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF16A34A),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = Color(0xFF374151),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun FaqItem(question: String, answer: String) {
    Column {
        Text(
            text = "Q: $question",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = answer,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
