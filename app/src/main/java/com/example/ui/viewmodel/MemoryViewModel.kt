package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.MemoryRepository
import com.example.data.model.AmbientMusic
import com.example.data.model.InteractiveFeature
import com.example.data.model.MemoryPhoto
import com.example.data.model.MemoryWebItem
import com.example.data.model.OccasionType
import com.example.data.model.QrStyleConfig
import com.example.data.model.SampleMemoryData
import com.example.data.model.ThemeStyle
import com.example.ui.audio.AmbientPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.URLEncoder

sealed class Screen {
    object Home : Screen()
    object Creator : Screen()
    object Viewer : Screen()
    object QrStudio : Screen()
    object OrderDesk : Screen()
    object OrderForm : Screen()
}

data class OrderFormState(
    val customerName: String = "",
    val customerEmail: String = "",
    val customerPhone: String = "",
    val eventType: OccasionType = OccasionType.BIRTHDAY,
    val recipientName: String = "",
    val eventDate: String = "",
    val specialMessage: String = "",
    val packageOption: String = "2 Web QRs (Special Offer ₹199)",
    val web1Recipient: String = "",
    val web1Occasion: OccasionType = OccasionType.BIRTHDAY,
    val web1Details: String = "",
    val web2Recipient: String = "",
    val web2Occasion: OccasionType = OccasionType.ANNIVERSARY,
    val web2Details: String = "",
    val specialNotes: String = ""
)

class MemoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MemoryRepository(application)

    val memoryWebs: StateFlow<List<MemoryWebItem>> = repository.memoryWebs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SampleMemoryData.sampleList
    )

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _selectedWeb = MutableStateFlow<MemoryWebItem?>(SampleMemoryData.sampleList.firstOrNull())
    val selectedWeb: StateFlow<MemoryWebItem?> = _selectedWeb.asStateFlow()

    private val _draftWeb = MutableStateFlow(
        createDefaultDraft(OccasionType.LOVE)
    )
    val draftWeb: StateFlow<MemoryWebItem> = _draftWeb.asStateFlow()

    private val _orderForm = MutableStateFlow(OrderFormState())
    val orderForm: StateFlow<OrderFormState> = _orderForm.asStateFlow()

    private val _isMusicPlaying = MutableStateFlow(false)
    val isMusicPlaying: StateFlow<Boolean> = _isMusicPlaying.asStateFlow()

    private val _isDarkTheme = MutableStateFlow<Boolean?>(null)
    val isDarkTheme: StateFlow<Boolean?> = _isDarkTheme.asStateFlow()

    fun toggleDarkTheme(isSystemDark: Boolean) {
        val current = _isDarkTheme.value ?: isSystemDark
        _isDarkTheme.value = !current
    }

    fun setDarkTheme(enabled: Boolean) {
        _isDarkTheme.value = enabled
    }

    fun navigateTo(screen: Screen) {
        if (screen != Screen.Viewer) {
            stopMusic()
        }
        _currentScreen.value = screen
    }

    fun openWebForViewing(item: MemoryWebItem) {
        _selectedWeb.value = item
        _currentScreen.value = Screen.Viewer
        if (item.ambientMusic != AmbientMusic.NONE) {
            startMusic(item.ambientMusic)
        }
    }

    fun openQrStudio(item: MemoryWebItem) {
        _selectedWeb.value = item
        _currentScreen.value = Screen.QrStudio
    }

    fun startNewDraft(occasion: OccasionType = OccasionType.BIRTHDAY) {
        _draftWeb.value = createDefaultDraft(occasion)
        _currentScreen.value = Screen.Creator
    }

    fun editExistingWeb(item: MemoryWebItem) {
        _draftWeb.value = item
        _currentScreen.value = Screen.Creator
    }

    fun updateDraft(updater: (MemoryWebItem) -> MemoryWebItem) {
        _draftWeb.value = updater(_draftWeb.value)
    }

    fun saveDraftAndPreview() {
        val draft = _draftWeb.value
        viewModelScope.launch {
            repository.saveMemoryWeb(draft)
            _selectedWeb.value = draft
            _currentScreen.value = Screen.Viewer
            if (draft.ambientMusic != AmbientMusic.NONE) {
                startMusic(draft.ambientMusic)
            }
        }
    }

    fun toggleMusic() {
        val currentItem = _selectedWeb.value ?: return
        if (_isMusicPlaying.value) {
            stopMusic()
        } else {
            startMusic(currentItem.ambientMusic)
        }
    }

    private fun startMusic(music: AmbientMusic) {
        AmbientPlayer.play(music, viewModelScope)
        _isMusicPlaying.value = true
    }

    fun stopMusic() {
        AmbientPlayer.stop()
        _isMusicPlaying.value = false
    }

    override fun onCleared() {
        super.onCleared()
        AmbientPlayer.stop()
    }

    fun updateOrderForm(updater: (OrderFormState) -> OrderFormState) {
        _orderForm.value = updater(_orderForm.value)
    }

    /**
     * WhatsApp ordering integration with official number: 9880035450
     */
    fun sendOrderToWhatsApp(context: Context) {
        val form = _orderForm.value
        val message = """
✨ *MEMORY WEB QR ORDER* ✨
🎁 Offer: ${form.packageOption}

👤 *Customer Name:* ${form.customerName.ifBlank { "Valued Customer" }}
📧 *Email:* ${form.customerEmail.ifBlank { "Not specified" }}
📱 *Phone:* ${form.customerPhone.ifBlank { "Not specified" }}

🎉 *Event Type:* ${form.eventType.emoji} ${form.eventType.title}
💝 *For (Recipient):* ${form.recipientName.ifBlank { if (form.web1Recipient.isNotBlank()) form.web1Recipient else "Special Someone" }}
📅 *Event Date:* ${form.eventDate.ifBlank { "Upcoming Celebration" }}
💌 *Heartfelt Message/Wish:* ${form.specialMessage.ifBlank { "Standard memories & photos" }}

🌐 *Web QR 1:* ${form.web1Recipient.ifBlank { form.recipientName.ifBlank { "Recipient 1" } }} (${form.eventType.title})
🌐 *Web QR 2:* ${form.web2Recipient.ifBlank { "Second Memory Web (TBD on WhatsApp)" }}

💬 *Special Notes:*
${form.specialNotes.ifBlank { "Please guide me on photo uploading!" }}

💖 *Your Memories. Your Story. Your QR.*
        """.trimIndent()

        launchWhatsAppIntent(context, message)
    }

    fun launchQuickWhatsAppInquiry(context: Context, customNote: String? = null) {
        val note = customNote ?: "Hi! I would like to order Memory Web QRs (Special Offer ₹199 for 2 Web QRs). Please share the details! ✨💖"
        launchWhatsAppIntent(context, note)
    }

    fun callDirect(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:9880035450")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun launchWhatsAppIntent(context: Context, message: String) {
        try {
            val encoded = URLEncoder.encode(message, "UTF-8")
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=919880035450&text=$encoded")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to general dial or browser
            val fallbackUri = Uri.parse("https://wa.me/919880035450")
            context.startActivity(Intent(Intent.ACTION_VIEW, fallbackUri))
        }
    }

    companion object {
        fun createDefaultDraft(occasion: OccasionType): MemoryWebItem {
            return MemoryWebItem(
                title = occasion.defaultTitle,
                recipientName = "Anya",
                senderName = "With All My Love",
                occasion = occasion,
                themeStyle = when (occasion) {
                    OccasionType.BIRTHDAY -> ThemeStyle.GOLDEN_LUXE
                    OccasionType.ANNIVERSARY -> ThemeStyle.ROSE_VELVET
                    OccasionType.LOVE -> ThemeStyle.ROSE_VELVET
                    OccasionType.FAMILY -> ThemeStyle.SUNSET_GLOW
                    OccasionType.GRADUATION -> ThemeStyle.PASTEL_BLOSSOM
                    else -> ThemeStyle.ROSE_VELVET
                },
                milestoneNote = "A Special Wish 💖",
                heartfeltLetter = occasion.defaultWish,
                photos = listOf(
                    MemoryPhoto(caption = "Our favorite laughter", milestoneYear = "Today")
                ),
                ambientMusic = when (occasion) {
                    OccasionType.BIRTHDAY -> AmbientMusic.BIRTHDAY_CHIME
                    else -> AmbientMusic.ROMANTIC_MELODY
                },
                interactiveFeatures = setOf(
                    InteractiveFeature.ENVELOPE_UNFOLD,
                    InteractiveFeature.FLOATING_HEARTS,
                    InteractiveFeature.SCRATCH_REVEAL
                ),
                secretScratchNote = "P.S. You mean more to me than words can ever say! 🥹💕"
            )
        }
    }
}
