package com.example.data.qr

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.data.model.MemoryWebItem
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.File
import java.io.FileOutputStream

object QrGenerator {

    /**
     * Generates a stylized QR code Bitmap from data using ZXing.
     */
    fun generateQrBitmap(
        content: String,
        size: Int = 512,
        foregroundColor: Int = Color.parseColor("#881337"),
        backgroundColor: Int = Color.WHITE,
        centerEmblem: String = "💖"
    ): Bitmap {
        val hints = HashMap<EncodeHintType, Any>()
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        hints[EncodeHintType.MARGIN] = 2
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size, hints)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        canvas.drawColor(backgroundColor)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = foregroundColor
        }

        val width = bitMatrix.width
        val height = bitMatrix.height
        val cellWidth = size.toFloat() / width
        val cellHeight = size.toFloat() / height

        val centerStart = (width * 0.38f).toInt()
        val centerEnd = (width * 0.62f).toInt()

        for (x in 0 until width) {
            for (y in 0 until height) {
                // Keep center clear for emblem if set
                if (centerEmblem.isNotEmpty() &&
                    x in centerStart..centerEnd &&
                    y in centerStart..centerEnd
                ) {
                    continue
                }

                if (bitMatrix.get(x, y)) {
                    val left = x * cellWidth
                    val top = y * cellHeight
                    val right = left + cellWidth
                    val bottom = top + cellHeight
                    // Rounded dot style
                    canvas.drawRoundRect(
                        RectF(left + 1f, top + 1f, right - 1f, bottom - 1f),
                        cellWidth * 0.4f,
                        cellWidth * 0.4f,
                        paint
                    )
                }
            }
        }

        // Draw center badge with emblem
        if (centerEmblem.isNotEmpty()) {
            val centerBadgeSize = size * 0.22f
            val badgeLeft = (size - centerBadgeSize) / 2f
            val badgeTop = (size - centerBadgeSize) / 2f

            val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = backgroundColor
                setShadowLayer(8f, 0f, 4f, Color.argb(60, 0, 0, 0))
            }
            canvas.drawRoundRect(
                RectF(badgeLeft, badgeTop, badgeLeft + centerBadgeSize, badgeTop + centerBadgeSize),
                centerBadgeSize * 0.3f,
                centerBadgeSize * 0.3f,
                badgePaint
            )

            val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = foregroundColor
                style = Paint.Style.STROKE
                strokeWidth = 3f
            }
            canvas.drawRoundRect(
                RectF(badgeLeft + 2f, badgeTop + 2f, badgeLeft + centerBadgeSize - 2f, badgeTop + centerBadgeSize - 2f),
                centerBadgeSize * 0.3f,
                centerBadgeSize * 0.3f,
                borderPaint
            )

            val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = centerBadgeSize * 0.5f
                textAlign = Paint.Align.CENTER
            }
            val textY = badgeTop + (centerBadgeSize / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f)
            canvas.drawText(centerEmblem, size / 2f, textY, textPaint)
        }

        return bitmap
    }

    /**
     * Generates a high-resolution, printable Keepsake Gift Card bitmap
     * ready for printing, sharing on WhatsApp, or framing.
     */
    fun createPrintableGiftCard(item: MemoryWebItem): Bitmap {
        val width = 1080
        val height = 1520
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Gradient Background
        val bgPaint = Paint().apply {
            shader = LinearGradient(
                0f, 0f, 0f, height.toFloat(),
                Color.parseColor("#FFF7F8"),
                Color.parseColor("#FFE4E8"),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Elegant Card Border
        val cardMargin = 48f
        val cardRect = RectF(cardMargin, cardMargin, width - cardMargin, height - cardMargin)
        val cardBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#E11D48")
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }
        canvas.drawRoundRect(cardRect, 40f, 40f, cardBorderPaint)

        // Inner dashed/thin border
        val innerMargin = cardMargin + 16f
        val innerRect = RectF(innerMargin, innerMargin, width - innerMargin, height - innerMargin)
        val innerBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#D97706")
            style = Paint.Style.STROKE
            strokeWidth = 1.5f
        }
        canvas.drawRoundRect(innerRect, 32f, 32f, innerBorderPaint)

        // Header Title
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#881337")
            textSize = 52f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("✨ MEMORY WEB QR ✨", width / 2f, 160f, titlePaint)

        // Subtitle
        val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#9F1239")
            textSize = 30f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("Turn Your Memories Into a Wish That Lasts Forever 💖", width / 2f, 220f, subPaint)

        // Recipient Name Banner
        val namePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#1F1218")
            textSize = 58f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("Specially For: ${item.recipientName}", width / 2f, 320f, namePaint)

        if (item.milestoneNote.isNotBlank()) {
            val milestonePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#D97706")
                textSize = 34f
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText("★ ${item.milestoneNote} ★", width / 2f, 380f, milestonePaint)
        }

        // Draw QR Code in Center
        val qrSize = 560
        val qrBitmap = generateQrBitmap(
            content = item.webPageUrl,
            size = qrSize,
            foregroundColor = Color.parseColor("#881337"),
            backgroundColor = Color.WHITE,
            centerEmblem = item.qrStyle.centerEmblem
        )

        val qrLeft = (width - qrSize) / 2f
        val qrTop = 440f

        // White shadow card behind QR
        val qrCardRect = RectF(qrLeft - 24f, qrTop - 24f, qrLeft + qrSize + 24f, qrTop + qrSize + 24f)
        val qrCardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            setShadowLayer(16f, 0f, 6f, Color.argb(40, 0, 0, 0))
        }
        canvas.drawRoundRect(qrCardRect, 30f, 30f, qrCardPaint)
        canvas.drawBitmap(qrBitmap, qrLeft, qrTop, null)

        // Instructions below QR
        val instructionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#4C0519")
            textSize = 36f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("📱 Scan with your phone camera", width / 2f, 1090f, instructionPaint)

        val relivenPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#881337")
            textSize = 30f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("To open your personalized interactive memory web!", width / 2f, 1145f, relivenPaint)

        if (item.senderName.isNotBlank()) {
            val fromPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#1F1218")
                textSize = 34f
                typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText("With all my love, — ${item.senderName}", width / 2f, 1230f, fromPaint)
        }

        // Footer Brand Mantra
        val footerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#9F1239")
            textSize = 28f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("💖 Your Memories. Your Story. Your QR. ♾️", width / 2f, 1370f, footerPaint)

        val orderDeskPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#D97706")
            textSize = 24f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("Order Your Web QRs: WhatsApp 9880035450 (₹199 for 2)", width / 2f, 1420f, orderDeskPaint)

        return bitmap
    }

    /**
     * Saves bitmap to app cache and provides shareable URI via FileProvider.
     */
    fun saveBitmapToCache(context: Context, bitmap: Bitmap, filename: String = "memory_web_qr_card.png"): Uri? {
        return try {
            val imagesFolder = File(context.cacheDir, "images")
            if (!imagesFolder.exists()) {
                imagesFolder.mkdirs()
            }
            val file = File(imagesFolder, filename)
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Shares the generated gift card or QR code via Android share sheet.
     */
    fun shareGiftCard(context: Context, uri: Uri, message: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TEXT, message)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share Memory Web QR Card"))
    }
}
