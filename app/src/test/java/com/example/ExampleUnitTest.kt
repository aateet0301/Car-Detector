package com.example

import com.example.model.CarDataRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testSampleCarsAvailable() {
    val sampleCars = CarDataRepository.sampleRealCars
    assertEquals(4, sampleCars.size)
    assertNotNull(sampleCars.find { it.id == "mustang_gt" })
    assertNotNull(sampleCars.find { it.id == "porsche_911" })
    assertNotNull(sampleCars.find { it.id == "corvette_c8" })
    assertNotNull(sampleCars.find { it.id == "bmw_m4" })
  }

  @Test
  fun testFormattingTemplateMatchesExpectedStructure() {
    val mustang = CarDataRepository.sampleRealCars.first { it.id == "mustang_gt" }
    val formattedText = mustang.defaultResult.toStandardFormattedText()

    assertTrue(formattedText.contains("**Make & Model:** Ford Mustang GT"))
    assertTrue(formattedText.contains("**Estimated Year/Gen:** 2018-2023 (6th Generation - S550 Facelift)"))
    assertTrue(formattedText.contains("**Exterior Color:** Grabber Blue"))
    assertTrue(formattedText.contains("**Body Style:** 2-Door Fastback Coupe"))
    assertTrue(formattedText.contains("**Key Specifications (Typical for this model):**"))
    assertTrue(formattedText.contains("* **Engine/Powertrain:** 5.0L Ti-VCT Coyote V8"))
    assertTrue(formattedText.contains("* **Drivetrain:** Rear-Wheel Drive (RWD)"))
    assertTrue(formattedText.contains("* **Horsepower:** ~450-460 hp"))
    assertTrue(formattedText.contains("* **Notable Features:**"))
    assertTrue(formattedText.contains("**Confidence Level:** High"))
  }

  @Test
  fun testUnidentifiedCarResponseHandling() {
    val noCarResult = com.example.model.CarIdentificationResult(
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
    assertTrue(noCarResult.toStandardFormattedText().contains("could not be detected"))
  }

  @Test
  fun testCreateEstimatedIdentification() {
    val estimated = CarDataRepository.createEstimatedIdentification(
      detectedColor = "Electric Blue",
      aspectRatio = 1.7f,
      uniqueSeed = 42
    )
    assertTrue(estimated.isCarDetected)
    assertTrue(estimated.makeAndModel.isNotBlank())
    assertEquals("Electric Blue", estimated.exteriorColor)
    assertTrue(estimated.toStandardFormattedText().contains("**Make & Model:**"))
    assertTrue(estimated.toStandardFormattedText().contains("**Key Specifications"))
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
    println("Serialized JSON: $json")
    assertTrue(json.contains("inlineData"))
    assertTrue(json.contains("mimeType"))
  }

  @Test
  fun testColorDetailsStructure() {
    val mustang = CarDataRepository.sampleRealCars.first { it.id == "mustang_gt" }
    val colorDetails = mustang.defaultResult.colorDetails
    assertEquals("Grabber Blue", colorDetails.primaryColorName)
    assertTrue(colorDetails.finishType.contains("Solid") || colorDetails.finishType.contains("Clearcoat"))
    assertEquals("CI / M7343", colorDetails.factoryPaintCode)
    assertEquals("#0077C8", colorDetails.hexColor)
    assertTrue(mustang.defaultResult.toStandardFormattedText().contains("* **Paint Finish:**"))
    assertTrue(mustang.defaultResult.toStandardFormattedText().contains("* **Factory Paint Code:** CI / M7343"))
  }
}
