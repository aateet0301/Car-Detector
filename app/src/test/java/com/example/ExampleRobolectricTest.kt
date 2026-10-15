package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.CarDataRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("AutoLens AI", appName)
  }

  @Test
  fun testImageUtilsColorExtraction() {
    val bitmap = android.graphics.Bitmap.createBitmap(100, 100, android.graphics.Bitmap.Config.ARGB_8888)
    bitmap.eraseColor(android.graphics.Color.rgb(0, 119, 200)) // Grabber Blue

    val color = com.example.util.ImageUtils.extractDominantVehicleColor(bitmap)
    assertNotNull(color)
    assertTrue(color.isNotBlank())
  }

  @Test
  fun testImageUtilsColorDetailsExtraction() {
    val bitmap = android.graphics.Bitmap.createBitmap(100, 100, android.graphics.Bitmap.Config.ARGB_8888)
    bitmap.eraseColor(android.graphics.Color.rgb(0, 119, 200)) // Grabber Blue

    val info = com.example.util.ImageUtils.extractVehicleColorDetails(bitmap)
    assertNotNull(info)
    assertTrue(info.colorName.isNotBlank())
    assertTrue(info.hexCode.startsWith("#"))
    assertTrue(info.finishType.isNotBlank())
    assertTrue(info.toneDescription.isNotBlank())
  }

  @Test
  fun testMaruti800SpecificationIntegrity() {
    val profile = CarDataRepository.findProfileByName("Maruti Suzuki 800")
    assertNotNull("Maruti 800 profile should exist in catalog", profile)
    assertEquals("Maruti Suzuki 800", profile?.makeAndModel)
    assertEquals("Maruti Suzuki", profile?.brandMake)
    assertTrue(profile?.enginePowertrain?.contains("796 cc") == true)
    assertTrue(profile?.horsepower?.contains("37 bhp") == true)
    assertTrue(profile?.topSpeedMileage?.contains("144 km/h") == true)
  }

  @Test
  fun testDefaultIdentificationPrioritizesMaruti800() {
    val result = CarDataRepository.createEstimatedIdentification(
      detectedColor = "Superior White",
      aspectRatio = 1.33f,
      uniqueSeed = 12345
    )
    assertNotNull(result)
    assertEquals("Maruti Suzuki 800", result.makeAndModel)
    assertEquals("Superior White", result.colorDetails.primaryColorName)
    assertTrue(result.enginePowertrain.contains("796 cc"))
    assertTrue(result.toSystematicReport().contains("SYSTEMATIC AUTOMOTIVE SPEC SHEET"))
    assertTrue(result.toSystematicReport().contains("Maruti Suzuki 800"))
  }

  @Test
  fun testCreateCameraTempUri() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val uri = com.example.util.ImageUtils.createCameraTempUri(context)
    assertNotNull(uri)
    assertTrue(uri.toString().startsWith("content://"))
  }
}
