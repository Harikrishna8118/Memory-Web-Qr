package com.example.data.model

import java.util.UUID

enum class OccasionType(
    val title: String,
    val emoji: String,
    val subtitle: String,
    val defaultWish: String,
    val defaultTitle: String
) {
    BIRTHDAY(
        title = "Birthdays",
        emoji = "🎂",
        subtitle = "Cakes, wishes & milestone celebrations",
        defaultWish = "Happy Birthday! May your day be surrounded by sunshine, endless laughter, and all the love your heart can hold. Here's to making unforgettable memories this year!",
        defaultTitle = "Happy Birthday, My Star!"
    ),
    ANNIVERSARY(
        title = "Anniversaries",
        emoji = "💍",
        subtitle = "Cherished milestones & enduring love",
        defaultWish = "Happy Anniversary! Every single day with you is my favorite chapter. Thank you for walking through life by my side, through every storm and every sunset. Forever and always.",
        defaultTitle = "Forever With You • Happy Anniversary"
    ),
    LOVE(
        title = "Love & Romance",
        emoji = "❤️",
        subtitle = "Love letters, dates & heartfelt moments",
        defaultWish = "To the one who makes my world brighter simply by being in it. You are my favorite thought, my safest home, and the best thing that ever happened to me.",
        defaultTitle = "To The Love of My Life"
    ),
    FAMILY(
        title = "Family Memories",
        emoji = "👨‍👩‍👧",
        subtitle = "Generations of warmth, laughter & hugs",
        defaultWish = "Family is where life begins and love never ends. Thank you for the countless sacrifices, the warm smiles, and the home you built in all our hearts.",
        defaultTitle = "Our Beautiful Family Story"
    ),
    GRADUATION(
        title = "Graduation",
        emoji = "🎓",
        subtitle = "Honoring hard work & soaring dreams",
        defaultWish = "Congratulations on your graduation! The sky is not the limit—it's only the beginning. Step boldly into the future, and always believe in the brilliance of your dreams.",
        defaultTitle = "Proud Graduate • Class of 2026"
    ),
    SPECIAL_GIFT(
        title = "Special Gifts",
        emoji = "🎁",
        subtitle = "Unboxing surprises & memorable tokens",
        defaultWish = "This is more than just a gift; it is a piece of my heart wrapped up in our favorite memories. May this interactive keepsake remind you how truly special you are.",
        defaultTitle = "A Special Surprise For You"
    ),
    CUSTOM(
        title = "Any Moment",
        emoji = "✨",
        subtitle = "Any memory worth remembering forever",
        defaultWish = "Some memories are etched in golden light forever. Relive this beautiful chapter whenever you scan this QR code.",
        defaultTitle = "A Moment Worth Remembering"
    )
}

enum class ThemeStyle(val displayName: String, val bgStart: Long, val bgEnd: Long, val accent: Long) {
    ROSE_VELVET("Rose Velvet", 0xFFFFF1F2, 0xFFFFE4E6, 0xFFE11D48),
    GOLDEN_LUXE("Golden Luxe", 0xFFFFFBEB, 0xFFFEF3C7, 0xFFD97706),
    MIDNIGHT_STARLIGHT("Midnight Starlight", 0xFF14081F, 0xFF271338, 0xFFF472B6),
    PASTEL_BLOSSOM("Pastel Blossom", 0xFFFAF5FF, 0xFFF3E8FF, 0xFF9333EA),
    SUNSET_GLOW("Sunset Glow", 0xFFFFF7ED, 0xFFFFEDD5, 0xFFEA580C)
}

enum class InteractiveFeature(val label: String, val icon: String, val description: String) {
    ENVELOPE_UNFOLD("Wax Seal Envelope", "✉️", "Tap to break the seal and unfold the handwritten letter"),
    CANDLE_BLOW("Birthday Cake Candle", "🎂", "Tap to blow the candle flame with sparkle explosion"),
    SCRATCH_REVEAL("Scratch-to-Reveal Card", "✨", "Scratch the golden foil to uncover a secret note"),
    FLOATING_HEARTS("Floating Love Shower", "💖", "Tap anywhere to burst glowing floating hearts")
}

enum class AmbientMusic(val displayName: String, val emoji: String, val description: String) {
    ROMANTIC_MELODY("Romantic Melody", "🎹", "Gentle romantic acoustic piano vibes"),
    BIRTHDAY_CHIME("Birthday Joy", "🎉", "Cheerful celebratory chimes"),
    NOSTALGIC_ACOUSTIC("Acoustic Nostalgia", "🎸", "Warm soulful string picking"),
    LOFI_CHILL("Lofi Dream", "🎧", "Soft relaxing lofi ambience"),
    NONE("No Music", "🔇", "Silent keepsake")
}

data class MemoryPhoto(
    val id: String = UUID.randomUUID().toString(),
    val uriString: String = "",
    val caption: String = "",
    val milestoneYear: String = "",
    val presetDrawable: String = ""
)

data class QrStyleConfig(
    val styleName: String = "Rose Gold",
    val primaryColorHex: Long = 0xFF881337,
    val secondaryColorHex: Long = 0xFFFFF1F2,
    val centerEmblem: String = "💖"
)

data class MemoryWebItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val recipientName: String,
    val senderName: String,
    val occasion: OccasionType,
    val themeStyle: ThemeStyle = ThemeStyle.ROSE_VELVET,
    val milestoneNote: String = "",
    val heartfeltLetter: String,
    val photos: List<MemoryPhoto> = emptyList(),
    val ambientMusic: AmbientMusic = AmbientMusic.ROMANTIC_MELODY,
    val interactiveFeatures: Set<InteractiveFeature> = setOf(
        InteractiveFeature.ENVELOPE_UNFOLD,
        InteractiveFeature.FLOATING_HEARTS,
        InteractiveFeature.SCRATCH_REVEAL
    ),
    val secretScratchNote: String = "P.S. You are the greatest gift life has given me. Forever yours! 🥹💕",
    val qrStyle: QrStyleConfig = QrStyleConfig(),
    val createdAt: Long = System.currentTimeMillis(),
    val isSample: Boolean = false
) {
    // Generates a mock/shareable Web URL identifier for this memory web
    val webPageUrl: String
        get() = "https://memorywebqr.app/v/${id.take(8)}"
}

object SampleMemoryData {
    val sampleList: List<MemoryWebItem> = listOf(
        MemoryWebItem(
            id = "sample-anniversary-01",
            title = "3 Years of Magic & Laughter",
            recipientName = "Priya",
            senderName = "Rahul",
            occasion = OccasionType.ANNIVERSARY,
            themeStyle = ThemeStyle.ROSE_VELVET,
            milestoneNote = "1,095 Days Together • 1 Forever",
            heartfeltLetter = "Happy 3rd Anniversary, my love! From that rainy evening in Bangalore to building our cozy little world together, every second with you has been a dream come true. Thank you for your warmth, your silly jokes, and the endless love you shower on me every single day. Here's to our lifetime of tomorrows. ♾️💖",
            photos = listOf(
                MemoryPhoto(id = "p1", caption = "Where our story began", milestoneYear = "Year 1"),
                MemoryPhoto(id = "p2", caption = "Our favorite hill trip", milestoneYear = "Year 2"),
                MemoryPhoto(id = "p3", caption = "Still falling for you every day", milestoneYear = "Year 3")
            ),
            ambientMusic = AmbientMusic.ROMANTIC_MELODY,
            interactiveFeatures = setOf(
                InteractiveFeature.ENVELOPE_UNFOLD,
                InteractiveFeature.FLOATING_HEARTS,
                InteractiveFeature.SCRATCH_REVEAL
            ),
            secretScratchNote = "P.S. Surprise! Dinner reservations at your favorite rooftop tonight at 8 PM! 🍷✨",
            isSample = true
        ),
        MemoryWebItem(
            id = "sample-birthday-02",
            title = "Happy 21st Birthday, Aarav!",
            recipientName = "Aarav",
            senderName = "Your Besties",
            occasion = OccasionType.BIRTHDAY,
            themeStyle = ThemeStyle.GOLDEN_LUXE,
            milestoneNote = "Chapter 21 • Unstoppable",
            heartfeltLetter = "To our resident legend Aarav! Happy 21st Birthday, brother! May this milestone year bring massive wins, wild adventures, and memories that make you smile when you're 80. Keep shining bright!",
            photos = listOf(
                MemoryPhoto(id = "p4", caption = "College day one", milestoneYear = "2023"),
                MemoryPhoto(id = "p5", caption = "Goa trip sunset", milestoneYear = "2024"),
                MemoryPhoto(id = "p6", caption = "The dream team forever", milestoneYear = "2025")
            ),
            ambientMusic = AmbientMusic.BIRTHDAY_CHIME,
            interactiveFeatures = setOf(
                InteractiveFeature.CANDLE_BLOW,
                InteractiveFeature.FLOATING_HEARTS,
                InteractiveFeature.SCRATCH_REVEAL
            ),
            secretScratchNote = "P.S. Cake smash protocol activated. You cannot escape! 🎂😎",
            isSample = true
        ),
        MemoryWebItem(
            id = "sample-love-03",
            title = "A Lifetime of Us",
            recipientName = "Sneha",
            senderName = "Kabir",
            occasion = OccasionType.LOVE,
            themeStyle = ThemeStyle.MIDNIGHT_STARLIGHT,
            milestoneNote = "Across All Lifetimes",
            heartfeltLetter = "I knew I loved you the moment I stopped looking for anything else. You are my calm in every storm, my home in any city, and my forever favorite person. Always yours.",
            photos = listOf(
                MemoryPhoto(id = "p7", caption = "That first coffee date", milestoneYear = "Nov 2022"),
                MemoryPhoto(id = "p8", caption = "Stargazing in Ladakh", milestoneYear = "May 2024")
            ),
            ambientMusic = AmbientMusic.LOFI_CHILL,
            interactiveFeatures = setOf(
                InteractiveFeature.ENVELOPE_UNFOLD,
                InteractiveFeature.FLOATING_HEARTS,
                InteractiveFeature.SCRATCH_REVEAL
            ),
            secretScratchNote = "P.S. I love you more than words, stars, or lifetimes can hold. 💕",
            isSample = true
        ),
        MemoryWebItem(
            id = "sample-family-04",
            title = "Mom & Dad's Golden Journey",
            recipientName = "Mom & Dad",
            senderName = "With Love, Sneha & Rohan",
            occasion = OccasionType.FAMILY,
            themeStyle = ThemeStyle.SUNSET_GLOW,
            milestoneNote = "25 Golden Years of Love",
            heartfeltLetter = "Dearest Mom & Dad, watching you both love, support, and care for our family through 25 beautiful years has been our greatest inspiration. Thank you for giving us a home filled with unconditional love and joy.",
            photos = listOf(
                MemoryPhoto(id = "p9", caption = "Wedding Day - 2001", milestoneYear = "2001"),
                MemoryPhoto(id = "p10", caption = "Silver Jubilee celebration", milestoneYear = "2026")
            ),
            ambientMusic = AmbientMusic.NOSTALGIC_ACOUSTIC,
            interactiveFeatures = setOf(
                InteractiveFeature.ENVELOPE_UNFOLD,
                InteractiveFeature.FLOATING_HEARTS
            ),
            secretScratchNote = "P.S. We booked your dream weekend retreat in Kerala! Pack your bags! 🌴❤️",
            isSample = true
        )
    )
}
