package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
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
    org.junit.Assert.assertNotNull(color)
    org.junit.Assert.assertTrue(color.isNotBlank())
  }

  @Test
  fun testImageUtilsColorDetailsExtraction() {
    val bitmap = android.graphics.Bitmap.createBitmap(100, 100, android.graphics.Bitmap.Config.ARGB_8888)
    bitmap.eraseColor(android.graphics.Color.rgb(0, 119, 200)) // Grabber Blue

    val info = com.example.util.ImageUtils.extractVehicleColorDetails(bitmap)
    org.junit.Assert.assertNotNull(info)
    org.junit.Assert.assertTrue(info.colorName.isNotBlank())
    org.junit.Assert.assertTrue(info.hexCode.startsWith("#"))
    org.junit.Assert.assertTrue(info.finishType.isNotBlank())
    org.junit.Assert.assertTrue(info.toneDescription.isNotBlank())
  }
}
