package com.example.data.export

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.data.model.MemoryWebItem
import java.io.File
import java.io.FileOutputStream

object WebExporter {

    /**
     * Generates a complete, interactive, self-contained HTML page representing the Memory Web.
     */
    fun generateHtmlWebPage(item: MemoryWebItem): String {
        val title = item.title
        val recipient = item.recipientName
        val sender = item.senderName
        val milestone = item.milestoneNote
        val letter = item.heartfeltLetter.replace("\n", "<br>")
        val secret = item.secretScratchNote

        return """
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>$title</title>
<style>
  :root {
    --primary: #E11D48;
    --accent: #D97706;
    --bg-start: #FFF1F2;
    --bg-end: #FFE4E6;
    --text-main: #1F1218;
  }
  * { box-sizing: border-box; margin: 0; padding: 0; }
  body {
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
    background: linear-gradient(135deg, var(--bg-start), var(--bg-end));
    color: var(--text-main);
    min-height: 100vh;
    padding: 24px 16px;
    display: flex;
    flex-direction: column;
    align-items: center;
    position: relative;
    overflow-x: hidden;
  }
  .container {
    max-width: 600px;
    width: 100%;
    background: rgba(255, 255, 255, 0.92);
    backdrop-filter: blur(12px);
    border-radius: 28px;
    padding: 32px 24px;
    box-shadow: 0 20px 40px rgba(136, 19, 55, 0.12);
    border: 1px solid rgba(225, 29, 72, 0.2);
    text-align: center;
  }
  .badge {
    display: inline-block;
    background: #FFE4E6;
    color: #E11D48;
    font-weight: 700;
    font-size: 14px;
    padding: 6px 16px;
    border-radius: 999px;
    margin-bottom: 12px;
    text-transform: uppercase;
    letter-spacing: 1px;
  }
  h1 {
    font-size: 28px;
    color: #881337;
    margin-bottom: 8px;
  }
  .milestone {
    font-size: 16px;
    color: #D97706;
    font-weight: 600;
    margin-bottom: 24px;
  }
  .envelope-card {
    background: #FFF7F8;
    border: 2px dashed #F43F5E;
    border-radius: 20px;
    padding: 24px;
    margin-bottom: 24px;
    cursor: pointer;
    transition: all 0.3s ease;
  }
  .letter-text {
    font-size: 17px;
    line-height: 1.7;
    color: #374151;
    font-style: italic;
    margin-bottom: 16px;
    text-align: left;
  }
  .signature {
    text-align: right;
    font-weight: 700;
    color: #881337;
    font-size: 18px;
  }
  .scratch-container {
    background: #FEF3C7;
    border-radius: 16px;
    padding: 20px;
    margin: 20px 0;
    border: 1px solid #FDE68A;
  }
  .scratch-title {
    font-size: 15px;
    font-weight: 700;
    color: #B45309;
    margin-bottom: 8px;
  }
  .secret-text {
    font-size: 16px;
    color: #92400E;
    font-weight: 600;
  }
  .floating-hearts-btn {
    background: linear-gradient(135deg, #E11D48, #F43F5E);
    color: white;
    border: none;
    padding: 14px 28px;
    border-radius: 999px;
    font-size: 16px;
    font-weight: 700;
    cursor: pointer;
    box-shadow: 0 8px 20px rgba(225, 29, 72, 0.3);
    margin: 16px 0;
    transition: transform 0.2s;
  }
  .floating-hearts-btn:active {
    transform: scale(0.96);
  }
  .footer {
    margin-top: 32px;
    font-size: 13px;
    color: #9CA3AF;
  }
</style>
</head>
<body>
<div class="container">
  <div class="badge">✨ MEMORY WEB QR ✨</div>
  <h1>$title</h1>
  <div class="milestone">For $recipient • $milestone</div>

  <div class="envelope-card">
    <div class="letter-text">$letter</div>
    <div class="signature">— With love, $sender</div>
  </div>

  <div class="scratch-container">
    <div class="scratch-title">✨ Secret Note ✨</div>
    <div class="secret-text">$secret</div>
  </div>

  <button class="floating-hearts-btn" onclick="spawnHeart()">Tap to Send Love 💖</button>

  <div class="footer">
    Created with Memory Web QR • Scan it today. Relive it tomorrow. ♾️
  </div>
</div>

<script>
function spawnHeart() {
  const heart = document.createElement('div');
  heart.innerHTML = '💖';
  heart.style.position = 'fixed';
  heart.style.bottom = '20px';
  heart.style.left = (Math.random() * 80 + 10) + 'vw';
  heart.style.fontSize = (Math.random() * 24 + 20) + 'px';
  heart.style.transition = 'all 2s ease-out';
  heart.style.pointerEvents = 'none';
  heart.style.opacity = '1';
  document.body.appendChild(heart);
  setTimeout(() => {
    heart.style.transform = 'translateY(-80vh) scale(1.5)';
    heart.style.opacity = '0';
  }, 50);
  setTimeout(() => heart.remove(), 2100);
}
</script>
</body>
</html>
        """.trimIndent()
    }

    /**
     * Exports and shares the standalone HTML web page.
     */
    fun shareHtmlPage(context: Context, item: MemoryWebItem) {
        try {
            val htmlContent = generateHtmlWebPage(item)
            val folder = File(context.cacheDir, "images")
            if (!folder.exists()) folder.mkdirs()
            val file = File(folder, "memory_web_${item.id.take(8)}.html")
            val stream = FileOutputStream(file)
            stream.write(htmlContent.toByteArray())
            stream.flush()
            stream.close()

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/html"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, item.title)
                putExtra(Intent.EXTRA_TEXT, "✨ Check out this interactive Memory Web created for ${item.recipientName}!\n${item.webPageUrl}")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Memory Webpage"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
