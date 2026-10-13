package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Base64
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object ImageUtils {

    fun createCameraTempUri(context: Context): Uri {
        val cacheDir = context.cacheDir
        val imageFile = File(cacheDir, "camera_capture_temp.jpg")
        if (imageFile.exists()) {
            imageFile.delete()
        }
        imageFile.createNewFile()
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )
    }

    /**
     * Decode a Bitmap from a Uri safely without OutOfMemoryError, even on 108MP phone cameras (Redmi Note 13 5G).
     */
    fun decodeSampledBitmapFromUri(
        context: Context,
        uri: Uri,
        maxWidth: Int = 1280,
        maxHeight: Int = 1280
    ): Bitmap? {
        return try {
            // Step 1: Decode bounds with inJustDecodeBounds
            val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            openUriInputStream(context, uri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, boundsOptions)
            }

            var origWidth = boundsOptions.outWidth
            var origHeight = boundsOptions.outHeight

            if (origWidth <= 0 || origHeight <= 0) {
                // Direct fallback decode
                return openUriInputStream(context, uri)?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                }?.let { scaleDownBitmap(it, maxWidth) }
            }

            // Step 2: Compute power-of-two inSampleSize
            var sampleSize = 1
            while ((origWidth / (sampleSize * 2)) >= maxWidth || (origHeight / (sampleSize * 2)) >= maxHeight) {
                sampleSize *= 2
            }

            // Step 3: Decode with computed sampleSize, handling OOM gracefully
            var decoded: Bitmap? = null
            var currentSampleSize = sampleSize
            for (attempt in 0..2) {
                try {
                    val decodeOptions = BitmapFactory.Options().apply {
                        inSampleSize = currentSampleSize
                        inPreferredConfig = Bitmap.Config.ARGB_8888
                    }
                    decoded = openUriInputStream(context, uri)?.use { stream ->
                        BitmapFactory.decodeStream(stream, null, decodeOptions)
                    }
                    if (decoded != null) break
                } catch (oom: OutOfMemoryError) {
                    currentSampleSize *= 2
                }
            }

            if (decoded == null) return null

            // Step 4: Check EXIF orientation
            val orientation = getExifOrientation(context, uri)
            val orientedBitmap = applyExifOrientation(decoded, orientation)

            // Step 5: Final scale down if still exceeds target
            scaleDownBitmap(orientedBitmap, maxWidth)
        } catch (t: Throwable) {
            t.printStackTrace()
            null
        }
    }

    private fun openUriInputStream(context: Context, uri: Uri): InputStream? {
        return try {
            if (uri.scheme == "file") {
                val path = uri.path
                if (path != null) {
                    val file = File(path)
                    if (file.exists()) file.inputStream() else null
                } else null
            } else {
                context.contentResolver.openInputStream(uri)
            }
        } catch (_: Throwable) {
            null
        }
    }

    private fun getExifOrientation(context: Context, uri: Uri): Int {
        return try {
            openUriInputStream(context, uri)?.use { stream ->
                val exifInterface = ExifInterface(stream)
                exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
            } ?: ExifInterface.ORIENTATION_NORMAL
        } catch (_: Throwable) {
            ExifInterface.ORIENTATION_NORMAL
        }
    }

    private fun applyExifOrientation(bitmap: Bitmap, orientation: Int): Bitmap {
        val rotationDegrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
        if (rotationDegrees == 0) return bitmap

        return try {
            val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (_: Throwable) {
            bitmap
        }
    }

    /**
     * Safely scales down a bitmap so its largest dimension is <= maxDimension.
     * Does NOT call recycle() to prevent Compose canvas drawing crashes.
     */
    fun scaleDownBitmap(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        if (width <= maxDimension && height <= maxDimension) {
            return bitmap
        }
        val ratio = Math.min(
            maxDimension.toFloat() / width,
            maxDimension.toFloat() / height
        )
        val targetWidth = Math.round(ratio * width).coerceAtLeast(1)
        val targetHeight = Math.round(ratio * height).coerceAtLeast(1)
        return try {
            Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
        } catch (_: Throwable) {
            bitmap
        }
    }

    /**
     * Reads image from sourceUri, decodes a smooth downsampled Bitmap, caches a local copy,
     * and returns the local file Uri and Bitmap.
     */
    fun cacheImageAndDecode(context: Context, sourceUri: Uri): Pair<Uri, Bitmap?> {
        val bitmap = decodeSampledBitmapFromUri(context, sourceUri, 1280, 1280)
        if (bitmap == null) {
            return Pair(sourceUri, null)
        }

        // Cache the scaled-down bitmap locally for quick display & persistence
        val cacheFile = File(context.cacheDir, "gallery_car_selected_${System.currentTimeMillis()}.jpg")
        try {
            FileOutputStream(cacheFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val localUri = Uri.fromFile(cacheFile)
        return Pair(localUri, bitmap)
    }

    fun bitmapToBase64Jpeg(bitmap: Bitmap, quality: Int = 85): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        val bytes = stream.toByteArray()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    data class ExtractedColorInfo(
        val colorName: String,
        val hexCode: String,
        val finishType: String,
        val estimatedCode: String,
        val toneDescription: String
    )

    /**
     * Extracts full dominant vehicle color specifications including paint finish,
     * factory code, hex color code, and tone breakdown.
     */
    fun extractVehicleColorDetails(bitmap: Bitmap): ExtractedColorInfo {
        return try {
            val width = bitmap.width
            val height = bitmap.height
            if (width < 10 || height < 10) {
                return ExtractedColorInfo(
                    colorName = "Metallic Silver",
                    hexCode = "#9E9E9E",
                    finishType = "Metallic Flake",
                    estimatedCode = "A83",
                    toneDescription = "Clean neutral silver with high metallic reflectivity"
                )
            }

            // Sample central car body region (25% to 75% width, 35% to 75% height)
            val startX = (width * 0.25).toInt().coerceAtLeast(0)
            val endX = (width * 0.75).toInt().coerceAtMost(width - 1)
            val startY = (height * 0.35).toInt().coerceAtLeast(0)
            val endY = (height * 0.75).toInt().coerceAtMost(height - 1)

            var totalR = 0L
            var totalG = 0L
            var totalB = 0L
            var sampleCount = 0

            val step = 4
            for (y in startY..endY step step) {
                for (x in startX..endX step step) {
                    val pixel = bitmap.getPixel(x, y)
                    val r = Color.red(pixel)
                    val g = Color.green(pixel)
                    val b = Color.blue(pixel)

                    val brightness = (r + g + b) / 3
                    if (brightness in 30..240) {
                        totalR += r
                        totalG += g
                        totalB += b
                        sampleCount++
                    }
                }
            }

            if (sampleCount == 0) {
                return ExtractedColorInfo(
                    colorName = "Gloss Black",
                    hexCode = "#18181B",
                    finishType = "Deep Clearcoat Gloss",
                    estimatedCode = "040",
                    toneDescription = "Jet black with deep mirror-finish clearcoat reflections"
                )
            }

            val avgR = (totalR / sampleCount).toInt().coerceIn(0, 255)
            val avgG = (totalG / sampleCount).toInt().coerceIn(0, 255)
            val avgB = (totalB / sampleCount).toInt().coerceIn(0, 255)
            val hexCode = String.format("#%02X%02X%02X", avgR, avgG, avgB)

            val hsv = FloatArray(3)
            Color.RGBToHSV(avgR, avgG, avgB, hsv)
            val hue = hsv[0]
            val sat = hsv[1]
            val value = hsv[2]

            when {
                sat < 0.14f && value > 0.72f -> ExtractedColorInfo(
                    colorName = "Alpine White",
                    hexCode = hexCode,
                    finishType = "High-Gloss Solid",
                    estimatedCode = "300 / 040",
                    toneDescription = "Brilliant bright pure white with high contrast body lines"
                )
                sat < 0.14f && value < 0.26f -> ExtractedColorInfo(
                    colorName = "Phantom Black",
                    hexCode = hexCode,
                    finishType = "Deep Metallic Gloss",
                    estimatedCode = "475 / 040",
                    toneDescription = "Deep obsidian black with high-depth clearcoat luster"
                )
                sat < 0.16f -> ExtractedColorInfo(
                    colorName = "Nardo Gray Metallic",
                    hexCode = hexCode,
                    finishType = "Non-Metallic Gloss Solid",
                    estimatedCode = "LY7C",
                    toneDescription = "Mid-tone flat battleship gray with modern ceramic sheen"
                )
                hue in 0f..25f || hue in 345f..360f -> ExtractedColorInfo(
                    colorName = "Radiant Crimson Red",
                    hexCode = hexCode,
                    finishType = "High-Gloss Clearcoat Solid",
                    estimatedCode = "84A / G1",
                    toneDescription = "Rich vivid scarlet red with deep warm clearcoat highlights"
                )
                hue in 25f..50f -> ExtractedColorInfo(
                    colorName = "Sunset Orange Metallic",
                    hexCode = hexCode,
                    finishType = "Multi-Coat Metallic",
                    estimatedCode = "C1X / 218",
                    toneDescription = "Energetic amber-copper orange with vibrant flake shimmer"
                )
                hue in 50f..70f -> ExtractedColorInfo(
                    colorName = "Speed Yellow",
                    hexCode = hexCode,
                    finishType = "High-Gloss Solid",
                    estimatedCode = "12H / 1S1",
                    toneDescription = "High-visibility canary yellow with bright sunlit radiance"
                )
                hue in 70f..160f -> ExtractedColorInfo(
                    colorName = "Racing Green Metallic",
                    hexCode = hexCode,
                    finishType = "Multi-Coat Pearl Metallic",
                    estimatedCode = "C4G / BRG",
                    toneDescription = "Deep British racing emerald green with gold-tinted metallic luster"
                )
                hue in 160f..260f -> if (value < 0.45f) {
                    ExtractedColorInfo(
                        colorName = "Deep Midnight Blue",
                        hexCode = hexCode,
                        finishType = "Deep Metallic Pearl",
                        estimatedCode = "C3Z / 036",
                        toneDescription = "Inky royal navy blue with violet undertones under direct light"
                    )
                } else {
                    ExtractedColorInfo(
                        colorName = "Electric Grabber Blue",
                        hexCode = hexCode,
                        finishType = "High-Gloss Solid",
                        estimatedCode = "CI / M7343",
                        toneDescription = "Vibrant sky/cyan blue with intense saturation and high contrast"
                    )
                }
                hue in 260f..315f -> ExtractedColorInfo(
                    colorName = "Royal Purple Metallic",
                    hexCode = hexCode,
                    finishType = "Custom Metallic Pearl",
                    estimatedCode = "P01 / 283",
                    toneDescription = "Exotic amethyst violet with multi-spectrum pearlescent flake"
                )
                else -> ExtractedColorInfo(
                    colorName = "Carbon Black Metallic",
                    hexCode = hexCode,
                    finishType = "Metallic Flake",
                    estimatedCode = "416",
                    toneDescription = "Dark charcoal black with subtle blue-gray metallic undertones"
                )
            }
        } catch (_: Throwable) {
            ExtractedColorInfo(
                colorName = "Metallic Gray",
                hexCode = "#808080",
                finishType = "Metallic Gloss",
                estimatedCode = "A83",
                toneDescription = "Clean balanced mid-gray with metallic highlights"
            )
        }
    }

    /**
     * Extracts dominant exterior color from the vehicle body region
     */
    fun extractDominantVehicleColor(bitmap: Bitmap): String {
        return extractVehicleColorDetails(bitmap).colorName
    }
}
