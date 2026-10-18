package com.example.ui.screens

import android.util.Patterns
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.OccasionType
import com.example.ui.viewmodel.MemoryViewModel
import com.example.ui.viewmodel.OrderFormState
import com.example.ui.viewmodel.Screen
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * OrderFormScreen captures the user's name, email, phone, and the type of event
 * (Birthday, Anniversary, etc.) to streamline the ordering process.
 * Now features a Lottie-style success celebration animation and a Material 3 Snackbar.
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OrderFormScreen(
    viewModel: MemoryViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val form by viewModel.orderForm.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showValidationErrors by remember { mutableStateOf(false) }
    var showSuccessModal by remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    val systemDark = androidx.compose.foundation.isSystemInDarkTheme()
    val userDarkTheme by viewModel.isDarkTheme.collectAsState()
    val isDark = userDarkTheme ?: systemDark

    val isNameValid = form.customerName.trim().isNotBlank()
    val isEmailValid = form.customerEmail.trim().isNotBlank() &&
            Patterns.EMAIL_ADDRESS.matcher(form.customerEmail.trim()).matches()
    val isPhoneValid = form.customerPhone.trim().isNotBlank()

    BackHandler {
        if (showSuccessModal) {
            showSuccessModal = false
        } else {
            viewModel.navigateTo(Screen.Home)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Navigation Header with Theme Toggle
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.Home) },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            color = if (isDark) Color(0xFF4C0519) else Color(0xFFFFE4E6),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "⚡ STREAMLINED ORDERING",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isDark) Color(0xFFFDA4AF) else Color(0xFFBE123C),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Order Memory Web QR",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    com.example.ui.components.ThemeToggleIconButton(
                        isDark = isDark,
                        onToggle = { viewModel.toggleDarkTheme(systemDark) }
                    )
                }
            }

            // Promotional Offer Pill
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("streamlined_order_offer_banner"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) Color(0xFF2C1631) else Color(0xFFFFF1F2)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (isDark) Color(0xFF881337) else Color(0xFFFDA4AF)
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "🎁 Special Offer: ₹199 for 2 Web QRs",
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isDark) Color(0xFFFDA4AF) else Color(0xFF9F1239),
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Includes 2 interactive websites, personalized QR codes & lifetime hosting.",
                                color = if (isDark) Color(0xFFFCE7F3) else Color(0xFF4C0519),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                        Surface(
                            color = Color(0xFFBE123C),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "₹199",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Section 1: Customer Contact Information (Name & Email & Phone)
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "1. Your Contact Details",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "We will send order confirmation and your completed Web QRs here.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Customer Name
                        OutlinedTextField(
                            value = form.customerName,
                            onValueChange = { name -> viewModel.updateOrderForm { it.copy(customerName = name) } },
                            label = { Text("Your Full Name *") },
                            placeholder = { Text("e.g. Rahul Sharma") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            },
                            isError = showValidationErrors && !isNameValid,
                            supportingText = {
                                if (showValidationErrors && !isNameValid) {
                                    Text("Please enter your name", color = MaterialTheme.colorScheme.error)
                                }
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_customer_name"),
                            shape = RoundedCornerShape(14.dp)
                        )

                        // Customer Email
                        OutlinedTextField(
                            value = form.customerEmail,
                            onValueChange = { email -> viewModel.updateOrderForm { it.copy(customerEmail = email) } },
                            label = { Text("Email Address *") },
                            placeholder = { Text("e.g. rahul@example.com") },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            isError = showValidationErrors && !isEmailValid,
                            supportingText = {
                                if (showValidationErrors && !isEmailValid) {
                                    Text("Please enter a valid email address", color = MaterialTheme.colorScheme.error)
                                }
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_customer_email"),
                            shape = RoundedCornerShape(14.dp)
                        )

                        // WhatsApp / Phone
                        OutlinedTextField(
                            value = form.customerPhone,
                            onValueChange = { phone -> viewModel.updateOrderForm { it.copy(customerPhone = phone) } },
                            label = { Text("WhatsApp Phone Number *") },
                            placeholder = { Text("e.g. 9880035450") },
                            leadingIcon = {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_customer_phone"),
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                }
            }

            // Section 2: Type of Event (Occasion Selection)
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Celebration,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "2. Select Event Type",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "Choose the celebration type to customize your webpage theme and animations.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OccasionType.values().forEach { occ ->
                                val isSelected = form.eventType == occ
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        viewModel.updateOrderForm {
                                            it.copy(
                                                eventType = occ,
                                                web1Occasion = occ,
                                                specialMessage = if (it.specialMessage.isBlank()) occ.defaultWish else it.specialMessage
                                            )
                                        }
                                    },
                                    label = {
                                        Text(
                                            text = "${occ.emoji} ${occ.title}",
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 13.sp
                                        )
                                    },
                                    leadingIcon = if (isSelected) {
                                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                    } else null,
                                    modifier = Modifier.testTag("event_chip_${occ.name.lowercase()}")
                                )
                            }
                        }

                        // Event Highlights Box
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = form.eventType.emoji, fontSize = 28.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = form.eventType.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = form.eventType.subtitle,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Section 3: Event & Memory Details
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "3. Personalization Details",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedTextField(
                            value = form.recipientName,
                            onValueChange = { r -> viewModel.updateOrderForm { it.copy(recipientName = r, web1Recipient = r) } },
                            label = { Text("Recipient Name (Who is this for?)") },
                            placeholder = { Text("e.g. Priya / Dad / Best Friend") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_recipient_name"),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            OutlinedTextField(
                                value = form.eventDate,
                                onValueChange = { d -> viewModel.updateOrderForm { it.copy(eventDate = d) } },
                                label = { Text("Event Date Commemorated *") },
                                placeholder = { Text("Select date or type (e.g. October 24, 2026)") },
                                leadingIcon = {
                                    IconButton(
                                        onClick = { showDatePickerDialog = true },
                                        modifier = Modifier.testTag("open_date_picker_icon_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "Pick Date",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                },
                                trailingIcon = {
                                    Button(
                                        onClick = { showDatePickerDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFE4E6)),
                                        shape = RoundedCornerShape(10.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier
                                            .padding(end = 6.dp)
                                            .height(34.dp)
                                            .testTag("open_date_picker_button")
                                    ) {
                                        Text("Pick 📅", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFBE123C))
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_event_date"),
                                shape = RoundedCornerShape(14.dp)
                            )

                            // Quick Date Selector Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Quick:", fontSize = 11.sp, color = Color.Gray)

                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            val today = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())
                                            viewModel.updateOrderForm { it.copy(eventDate = today) }
                                        }
                                        .testTag("date_chip_today"),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Today",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { showDatePickerDialog = true }
                                        .testTag("date_chip_calendar"),
                                    color = Color(0xFFFEF3C7),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Choose Calendar 📅",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFB45309),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                if (form.eventDate.isNotBlank()) {
                                    Surface(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { viewModel.updateOrderForm { it.copy(eventDate = "") } }
                                            .testTag("date_chip_clear"),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "Clear",
                                            fontSize = 11.sp,
                                            color = Color.Red,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        OutlinedTextField(
                            value = form.specialMessage,
                            onValueChange = { msg -> viewModel.updateOrderForm { it.copy(specialMessage = msg) } },
                            label = { Text("Heartfelt Message or Story (Optional)") },
                            placeholder = { Text("Write your wish, or share photos on WhatsApp after ordering...") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                }
            }

            // Section 4: Package Selection
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            text = "4. Select Package",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val packages = listOf(
                            "2 Web QRs (Special Offer ₹199)" to "🎁 Most Popular • ₹199 for 2 Pages",
                            "1 Web QR Single (₹149)" to "✨ 1 Interactive Page • ₹149",
                            "3 Web QRs Family Bundle (₹299)" to "🌟 3 Pages Bundle • ₹299"
                        )

                        packages.forEach { (pkgName, pkgDesc) ->
                            val isSelected = form.packageOption == pkgName
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { viewModel.updateOrderForm { it.copy(packageOption = pkgName) } },
                                color = if (isSelected) Color(0xFFFFF1F2) else MaterialTheme.colorScheme.surfaceVariant,
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE11D48)) else null,
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = pkgDesc,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = if (isSelected) Color(0xFF9F1239) else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = if (pkgName.contains("199")) "Includes lifetime hosting & printable QR cards" else "Full interactive experience",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            tint = Color(0xFFE11D48)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Live Order Summary Card
            item {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("order_summary_card"),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = if (isDark) Color(0xFF24102A) else Color(0xFFFFF9FA)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = Color(0xFFE11D48))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Order Summary",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = if (isDark) Color(0xFFF472B6) else Color(0xFF881337)
                                )
                            }

                            Text(
                                text = if (form.packageOption.contains("299")) "₹299" else if (form.packageOption.contains("149")) "₹149" else "₹199",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = if (isDark) Color(0xFFFDA4AF) else Color(0xFFBE123C)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        SummaryRow("Customer Name", form.customerName.ifBlank { "—" })
                        SummaryRow("Email", form.customerEmail.ifBlank { "—" })
                        SummaryRow("Event Occasion", "${form.eventType.emoji} ${form.eventType.title}")
                        SummaryRow("Recipient", form.recipientName.ifBlank { "—" })
                        SummaryRow("Package", form.packageOption)

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "💬 Photos, audio & special requests can be shared immediately on WhatsApp after placing the order.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // Submission & WhatsApp Actions
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            if (!isNameValid || !isEmailValid) {
                                showValidationErrors = true
                            } else {
                                // Trigger Lottie-style success animation celebration modal!
                                showSuccessModal = true

                                // Show celebratory Material 3 Snackbar!
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "🎉 Order submitted for ${form.customerName}! WhatsApp concierge opened.",
                                        actionLabel = "OK",
                                        duration = SnackbarDuration.Short
                                    )
                                }

                                // Launch WhatsApp with prefilled order details!
                                viewModel.sendOrderToWhatsApp(context)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("submit_order_form_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Submit & Order via WhatsApp",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.launchQuickWhatsAppInquiry(context) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("order_form_quick_chat_button"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF15803D))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Chat with Us", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { viewModel.callDirect(context) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("order_form_call_button"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call 9880035450", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Material 3 Celebratory Snackbar Host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .testTag("order_success_snackbar_host"),
            snackbar = { snackbarData ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1F1218)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "✨", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = snackbarData.visuals.message,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        snackbarData.visuals.actionLabel?.let { action ->
                            Text(
                                text = action,
                                color = Color(0xFFF472B6),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier
                                    .clickable { snackbarData.performAction() }
                                    .padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        )

        // Lottie-Style Celebratory Success Dialog
        if (showSuccessModal) {
            OrderSuccessCelebrationDialog(
                form = form,
                onDismiss = { showSuccessModal = false },
                onContinueWhatsApp = {
                    showSuccessModal = false
                    viewModel.sendOrderToWhatsApp(context)
                },
                onBackHome = {
                    showSuccessModal = false
                    viewModel.navigateTo(Screen.Home)
                }
            )
        }

        // Material 3 DatePicker Dialog for selecting the commemorated event date
        if (showDatePickerDialog) {
            DatePickerDialog(
                onDismissRequest = { showDatePickerDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).apply {
                                    timeZone = TimeZone.getTimeZone("UTC")
                                }
                                val formattedDate = formatter.format(Date(millis))
                                viewModel.updateOrderForm { it.copy(eventDate = formattedDate) }
                            }
                            showDatePickerDialog = false
                        },
                        modifier = Modifier.testTag("date_picker_confirm_button")
                    ) {
                        Text("Confirm Date", fontWeight = FontWeight.Bold, color = Color(0xFFE11D48))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePickerDialog = false },
                        modifier = Modifier.testTag("date_picker_cancel_button")
                    ) {
                        Text("Cancel")
                    }
                },
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                DatePicker(
                    state = datePickerState,
                    title = {
                        Text(
                            text = "Select Event Date (${form.eventType.title})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF881337),
                            modifier = Modifier.padding(start = 24.dp, top = 16.dp)
                        )
                    },
                    headline = {
                        Text(
                            text = "Pick the specific date you are commemorating 💖",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFB45309),
                            modifier = Modifier.padding(start = 24.dp, bottom = 12.dp)
                        )
                    }
                )
            }
        }
    }
}

/**
 * Lottie-Style Animated Success Celebration Dialog
 * Features spring-bounced checkmark circle, rotating starburst rays,
 * celebratory confetti particle explosion, and order confirmation overview.
 */
@Composable
fun OrderSuccessCelebrationDialog(
    form: OrderFormState,
    onDismiss: () -> Unit,
    onContinueWhatsApp: () -> Unit,
    onBackHome: () -> Unit
) {
    // Spring Animation for checkmark bounce (Lottie feel)
    val checkmarkScale = remember { Animatable(0f) }
    val contentAlpha = remember { Animatable(0f) }

    // Rotating Sunburst Rays
    val infiniteTransition = rememberInfiniteTransition(label = "sunburst")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Pulsing Outer Aura Ring
    val auraScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura"
    )

    LaunchedEffect(Unit) {
        // Trigger spring bounce
        checkmarkScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        contentAlpha.animateTo(1f, animationSpec = tween(350))
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            // Dynamic Confetti Particle Explosion
            ConfettiParticleCanvas(
                modifier = Modifier.fillMaxSize()
            )

            // Main Celebration Card
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .clip(RoundedCornerShape(28.dp))
                    .clickable(enabled = false) {}
                    .testTag("lottie_success_dialog_card"),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Animated Checkmark with Rotating Sunburst Aura
                    Box(
                        modifier = Modifier
                            .size(130.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Rotating Sunburst Rays Background Canvas
                        Canvas(
                            modifier = Modifier
                                .size(130.dp)
                                .scale(auraScale)
                        ) {
                            val center = Offset(size.width / 2f, size.height / 2f)
                            val numRays = 12
                            val rayLength = size.width * 0.48f

                            for (i in 0 until numRays) {
                                val angleDeg = (i * (360f / numRays)) + rotationAngle
                                val angleRad = Math.toRadians(angleDeg.toDouble())
                                val startOffset = Offset(
                                    center.x + (rayLength * 0.45f * cos(angleRad)).toFloat(),
                                    center.y + (rayLength * 0.45f * sin(angleRad)).toFloat()
                                )
                                val endOffset = Offset(
                                    center.x + (rayLength * cos(angleRad)).toFloat(),
                                    center.y + (rayLength * sin(angleRad)).toFloat()
                                )

                                drawLine(
                                    color = Color(0xFFFFD54F).copy(alpha = 0.45f),
                                    start = startOffset,
                                    end = endOffset,
                                    strokeWidth = 3.5.dp.toPx()
                                )
                            }
                        }

                        // Outer Glowing Ring
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .scale(checkmarkScale.value)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(Color(0xFFFDE047).copy(alpha = 0.5f), Color.Transparent)
                                    ),
                                    CircleShape
                                )
                        )

                        // Core Spring Bouncing Checkmark Circle
                        Box(
                            modifier = Modifier
                                .size(78.dp)
                                .scale(checkmarkScale.value)
                                .shadow(12.dp, CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFF22C55E), Color(0xFF16A34A))
                                    ),
                                    CircleShape
                                )
                                .border(3.dp, Color(0xFFDCFCE7), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success Checkmark",
                                tint = Color.White,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Title & Congratulatory Text
                    Surface(
                        color = Color(0xFFDCFCE7),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "✨ ORDER SUBMITTED SUCCESSFULLY! ✨",
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF15803D),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Thank You, ${form.customerName.ifBlank { "Friend" }}! 🎉",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1F1218),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Your order details have been securely logged and forwarded to our official WhatsApp Concierge.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4B5563),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Summary Pill Card
                    Surface(
                        color = Color(0xFFFFF1F2),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDA4AF)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Event Type", fontSize = 12.sp, color = Color(0xFF6B7280))
                                Text(
                                    text = "${form.eventType.emoji} ${form.eventType.title}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color(0xFF881337)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "For (Recipient)", fontSize = 12.sp, color = Color(0xFF6B7280))
                                Text(
                                    text = form.recipientName.ifBlank { "Special Someone" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color(0xFF1F1218)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Email Confirmation", fontSize = 12.sp, color = Color(0xFF6B7280))
                                Text(
                                    text = form.customerEmail.ifBlank { "—" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color(0xFF1F1218)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Package", fontSize = 12.sp, color = Color(0xFF6B7280))
                                Text(
                                    text = if (form.packageOption.contains("199")) "₹199 for 2 Web QRs" else form.packageOption,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.sp,
                                    color = Color(0xFFBE123C)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "📱 WhatsApp: +91 9880035450\nYou can share your favorite photos & songs directly in chat!",
                        fontSize = 11.sp,
                        color = Color(0xFF15803D),
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Dialog Actions
                    Button(
                        onClick = onContinueWhatsApp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("dialog_continue_whatsapp_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Continue to WhatsApp Chat", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = onBackHome,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("dialog_done_home_button"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Done • Explore Sample QRs", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

/**
 * Animated Confetti Canvas simulating Lottie particle bursts
 */
@Composable
fun ConfettiParticleCanvas(modifier: Modifier = Modifier) {
    val particles = remember {
        val colors = listOf(
            Color(0xFFE11D48),
            Color(0xFFF59E0B),
            Color(0xFF10B981),
            Color(0xFF3B82F6),
            Color(0xFFEC4899),
            Color(0xFF8B5CF6),
            Color(0xFFFFD700)
        )
        List(40) {
            ConfettiPiece(
                x = Random.nextFloat(),
                y = Random.nextFloat() * -0.5f,
                speed = Random.nextFloat() * 0.4f + 0.3f,
                size = Random.nextFloat() * 10f + 8f,
                color = colors.random(),
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = Random.nextFloat() * 180f - 90f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        particles.forEach { piece ->
            val currentY = ((piece.y + time * piece.speed) % 1.2f) * canvasHeight
            val currentX = (piece.x * canvasWidth) + (sin((time * 6.28f + piece.x * 10f).toDouble()).toFloat() * 30f)
            val currentRot = piece.rotation + (time * piece.rotationSpeed)

            if (currentY in 0f..canvasHeight) {
                drawCircle(
                    color = piece.color,
                    radius = piece.size / 2f,
                    center = Offset(currentX, currentY)
                )
            }
        }
    }
}

private data class ConfettiPiece(
    val x: Float,
    val y: Float,
    val speed: Float,
    val size: Float,
    val color: Color,
    val rotation: Float,
    val rotationSpeed: Float
)

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}
