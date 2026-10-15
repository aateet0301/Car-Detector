package com.example

import com.example.model.CarDataRepository
import com.example.model.CarIdentificationResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testMaruti800SpecificationAndLookup() {
    val maruti = CarDataRepository.findProfileByName("Maruti 800")
    assertNotNull(maruti)
    assertEquals("Maruti Suzuki 800", maruti?.makeAndModel)
    assertEquals("Maruti Suzuki", maruti?.brandMake)
    assertTrue(maruti?.enginePowertrain?.contains("796 cc") == true)
    assertTrue(maruti?.horsepower?.contains("37 bhp") == true)
    assertEquals("5-Door Micro Hatchback", maruti?.bodyStyle)

    // Test estimated identification with override
    val result = CarDataRepository.createEstimatedIdentification(
      detectedColor = "Maruti Solid Red",
      aspectRatio = 1.35f,
      uniqueSeed = 100,
      preferredModelName = "Maruti Suzuki 800"
    )
    assertEquals("Maruti Suzuki 800", result.makeAndModel)
    assertTrue(result.toSystematicReport().contains("Maruti Suzuki 800"))
    assertTrue(result.toSystematicReport().contains("796 cc"))
  }

  @Test
  fun testSystematicReportStructure() {
    val result = CarDataRepository.createEstimatedIdentification(
      detectedColor = "Superior White",
      aspectRatio = 1.33f,
      uniqueSeed = 42
    )
    val report = result.toSystematicReport()

    assertTrue(report.contains("SYSTEMATIC AUTOMOTIVE SPEC SHEET"))
    assertTrue(report.contains("VEHICLE IDENTITY & CLASSIFICATION"))
    assertTrue(report.contains("EXTERIOR COLOR & FACTORY PAINT SPECIFICATION"))
    assertTrue(report.contains("POWERTRAIN & MECHANICAL ARCHITECTURE"))
    assertTrue(report.contains("CHASSIS & DIMENSIONS"))
  }

  @Test
  fun testUnidentifiedCarResponseHandling() {
    val noCarResult = CarIdentificationResult(
      makeAndModel = "Unknown",
      estimatedYearGen = "N/A",
      exteriorColor = "N/A",
      bodyStyle = "N/A",
      enginePowertrain = "N/A",
      drivetrain = "N/A",
      horsepower = "N/A",
      notableFeatures = "N/A",
      confidenceLevel = "Low",
      rawFormattedResponse = "A vehicle could not be detected in this image.",
      isCarDetected = false,
      messageIfNotDetected = "A vehicle could not be detected in this image."
    )
    assertEquals(false, noCarResult.isCarDetected)
    assertEquals("A vehicle could not be detected in this image.", noCarResult.messageIfNotDetected)
  }

  @Test
  fun testGeminiApiSerialization() {
    val request = com.example.data.remote.GeminiRequest(
      contents = listOf(
        com.example.data.remote.GeminiContent(
          parts = listOf(
            com.example.data.remote.GeminiPart(text = "Hello"),
            com.example.data.remote.GeminiPart(
              inlineData = com.example.data.remote.GeminiInlineData(
                mimeType = "image/jpeg",
                data = "dGVzdA=="
              )
            )
          )
        )
      )
    )
    val moshi = com.squareup.moshi.Moshi.Builder()
      .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
      .build()
    val json = moshi.adapter(com.example.data.remote.GeminiRequest::class.java).toJson(request)
    assertNotNull(json)
    assertTrue(json.contains("inlineData"))
    assertTrue(json.contains("mimeType"))
  }

  @Test
  fun testVehicleCatalogIntegrity() {
    val catalog = CarDataRepository.vehicleCatalog
    assertTrue("Catalog should contain multiple vehicles", catalog.size >= 10)
    val first = catalog.first()
    assertEquals("Maruti Suzuki 800", first.makeAndModel)
  }
}
