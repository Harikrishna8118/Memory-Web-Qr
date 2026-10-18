package com.example

import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testSampleMemoryData_isNotEmpty() {
    val samples = com.example.data.model.SampleMemoryData.sampleList
    assertTrue(samples.isNotEmpty())
    val first = samples.first()
    assertTrue(first.webPageUrl.startsWith("https://memorywebqr.app/v/"))
  }

  @Test
  fun testHtmlExport_containsRecipientAndTitle() {
    val sample = com.example.data.model.SampleMemoryData.sampleList.first()
    val html = com.example.data.export.WebExporter.generateHtmlWebPage(sample)
    assertTrue(html.contains(sample.title))
    assertTrue(html.contains(sample.recipientName))
    assertTrue(html.contains("MEMORY WEB QR"))
  }

  @Test
  fun testOrderFormState_capturesNameEmailAndEventType() {
    val form = com.example.ui.viewmodel.OrderFormState(
      customerName = "Rahul Sharma",
      customerEmail = "rahul@example.com",
      customerPhone = "9880035450",
      eventType = com.example.data.model.OccasionType.BIRTHDAY,
      recipientName = "Priya",
      eventDate = "October 24, 2026"
    )
    assertEquals("Rahul Sharma", form.customerName)
    assertEquals("rahul@example.com", form.customerEmail)
    assertEquals(com.example.data.model.OccasionType.BIRTHDAY, form.eventType)
    assertEquals("Priya", form.recipientName)
    assertEquals("October 24, 2026", form.eventDate)
  }

  @Test
  fun testOccasionTypes_includesAllRequiredEvents() {
    val types = com.example.data.model.OccasionType.values().map { it.name }
    assertTrue(types.contains("BIRTHDAY"))
    assertTrue(types.contains("ANNIVERSARY"))
    assertTrue(types.contains("LOVE"))
    assertTrue(types.contains("FAMILY"))
    assertTrue(types.contains("GRADUATION"))
    assertTrue(types.contains("SPECIAL_GIFT"))
  }

  @Test
  fun testAnniversaryOrderForm_withDate() {
    val form = com.example.ui.viewmodel.OrderFormState(
      customerName = "Anita Roy",
      customerEmail = "anita@example.com",
      customerPhone = "9880035450",
      eventType = com.example.data.model.OccasionType.ANNIVERSARY,
      recipientName = "Karan",
      eventDate = "December 15, 2026",
      packageOption = "2 Web QRs (Special Offer ₹199)"
    )
    assertEquals("Anita Roy", form.customerName)
    assertEquals(com.example.data.model.OccasionType.ANNIVERSARY, form.eventType)
    assertEquals("December 15, 2026", form.eventDate)
    assertTrue(form.packageOption.contains("₹199"))
  }
}
