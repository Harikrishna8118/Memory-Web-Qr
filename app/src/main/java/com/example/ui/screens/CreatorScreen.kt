package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AmbientMusic
import com.example.data.model.InteractiveFeature
import com.example.data.model.MemoryPhoto
import com.example.data.model.OccasionType
import com.example.data.model.ThemeStyle
import com.example.ui.viewmodel.MemoryViewModel
import com.example.ui.viewmodel.Screen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreatorScreen(
    viewModel: MemoryViewModel,
    modifier: Modifier = Modifier
) {
    val draft by viewModel.draftWeb.collectAsState()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val newPhotos = draft.photos + MemoryPhoto(
                uriString = uri.toString(),
                caption = "Special Memory",
                milestoneYear = "2026"
            )
            viewModel.updateDraft { it.copy(photos = newPhotos) }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Studio Header
        item {
            Column {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "🌐 MEMORY WEB STUDIO",
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Craft Your Personalized Web QR",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Fill in the details below to generate your interactive memory experience & printable QR code.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 1. Select Occasion
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Choose Occasion",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OccasionType.values().forEach { occ ->
                            val isSelected = draft.occasion == occ
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    viewModel.updateDraft {
                                        it.copy(
                                            occasion = occ,
                                            title = if (it.title == it.occasion.defaultTitle) occ.defaultTitle else it.title,
                                            heartfeltLetter = if (it.heartfeltLetter == it.occasion.defaultWish) occ.defaultWish else it.heartfeltLetter
                                        )
                                    }
                                },
                                label = { Text("${occ.emoji} ${occ.title}") }
                            )
                        }
                    }
                }
            }
        }

        // 2. Who is this for?
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
                        text = "2. Recipient & Title",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = draft.title,
                        onValueChange = { newTitle -> viewModel.updateDraft { it.copy(title = newTitle) } },
                        label = { Text("Web Page Title") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("title_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = draft.recipientName,
                            onValueChange = { r -> viewModel.updateDraft { it.copy(recipientName = r) } },
                            label = { Text("For (Recipient)") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("recipient_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = draft.senderName,
                            onValueChange = { s -> viewModel.updateDraft { it.copy(senderName = s) } },
                            label = { Text("From (Sender)") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("sender_input"),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    OutlinedTextField(
                        value = draft.milestoneNote,
                        onValueChange = { m -> viewModel.updateDraft { it.copy(milestoneNote = m) } },
                        label = { Text("Milestone Note (e.g. 1,095 Days Together)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // 3. Heartfelt Letter / Wish
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "3. Heartfelt Message",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = {
                            viewModel.updateDraft { it.copy(heartfeltLetter = it.occasion.defaultWish) }
                        }) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "Use template", tint = MaterialTheme.colorScheme.primary)
                        }
                    }

                    OutlinedTextField(
                        value = draft.heartfeltLetter,
                        onValueChange = { msg -> viewModel.updateDraft { it.copy(heartfeltLetter = msg) } },
                        label = { Text("Your Message / Love Letter") },
                        minLines = 4,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("letter_input"),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // 4. Interactive Magic Features
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "4. Interactive Magic Features",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Recipients can interact with these when viewing your Web QR!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    InteractiveFeature.values().forEach { feature ->
                        val isChecked = draft.interactiveFeatures.contains(feature)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    val current = draft.interactiveFeatures.toMutableSet()
                                    if (isChecked) current.remove(feature) else current.add(feature)
                                    viewModel.updateDraft { it.copy(interactiveFeatures = current) }
                                }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { checked ->
                                    val current = draft.interactiveFeatures.toMutableSet()
                                    if (checked) current.add(feature) else current.remove(feature)
                                    viewModel.updateDraft { it.copy(interactiveFeatures = current) }
                                }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "${feature.icon} ${feature.label}",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = feature.description,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    if (draft.interactiveFeatures.contains(InteractiveFeature.SCRATCH_REVEAL)) {
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = draft.secretScratchNote,
                            onValueChange = { note -> viewModel.updateDraft { it.copy(secretScratchNote = note) } },
                            label = { Text("Secret Scratch Card Message") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        // 5. Photos Section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "5. Memory Photos (${draft.photos.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Button(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Photo", fontSize = 12.sp)
                        }
                    }

                    if (draft.photos.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No photos added yet. Tap 'Add Photo' or choose sample polaroids!",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(draft.photos) { photo ->
                                Surface(
                                    modifier = Modifier.width(160.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(text = "📸 Photo Memory", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text(text = photo.caption, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        IconButton(
                                            onClick = {
                                                val remaining = draft.photos.filter { it.id != photo.id }
                                                viewModel.updateDraft { it.copy(photos = remaining) }
                                            },
                                            modifier = Modifier.align(Alignment.End)
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 6. Ambient Music & Theme Style
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "6. Ambient Melody",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(AmbientMusic.values()) { music ->
                            val isSelected = draft.ambientMusic == music
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.updateDraft { it.copy(ambientMusic = music) } },
                                label = { Text("${music.emoji} ${music.displayName}") }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Theme Palette",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(ThemeStyle.values()) { style ->
                            val isSelected = draft.themeStyle == style
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.updateDraft { it.copy(themeStyle = style) } },
                                label = { Text(style.displayName) }
                            )
                        }
                    }
                }
            }
        }

        // Action Buttons
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { viewModel.saveDraftAndPreview() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("preview_interactive_web_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Preview Interactive Webpage", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.openQrStudio(draft) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("generate_qr_card_button"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Get QR Card", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            viewModel.updateOrderForm { form ->
                                form.copy(
                                    eventType = draft.occasion,
                                    recipientName = draft.recipientName,
                                    web1Recipient = draft.recipientName,
                                    web1Occasion = draft.occasion,
                                    specialMessage = draft.heartfeltLetter
                                )
                            }
                            viewModel.navigateTo(Screen.OrderForm)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Order ₹199 (2 QRs)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
