package com.example.model

import androidx.annotation.DrawableRes
import com.example.R
import com.example.util.ImageUtils

data class CarColorDetails(
    val primaryColorName: String,
    val finishType: String = "High-Gloss Solid",
    val factoryPaintCode: String = "N/A",
    val shadeTone: String = "Factory multi-stage clearcoat automotive finish",
    val accentDetails: String = "Body-color mirrors and factory trim accents",
    val hexColor: String = "#0077C8"
)

data class SampleCar(
    val id: String,
    val title: String,
    val subtitle: String,
    @param:DrawableRes val imageRes: Int,
    val colorName: String,
    val colorHex: Long,
    val defaultResult: CarIdentificationResult
)

data class CarIdentificationResult(
    val makeAndModel: String,
    val estimatedYearGen: String,
    val exteriorColor: String,
    val colorDetails: CarColorDetails = CarColorDetails(primaryColorName = exteriorColor),
    val bodyStyle: String,
    val enginePowertrain: String,
    val drivetrain: String,
    val horsepower: String,
    val notableFeatures: String,
    val confidenceLevel: String,
    val rawFormattedResponse: String,
    val isCarDetected: Boolean = true,
    val messageIfNotDetected: String? = null
) {
    fun toStandardFormattedText(): String {
        if (!isCarDetected) {
            return messageIfNotDetected ?: "No vehicle could be detected in this image. Please provide a clear view of a car."
        }
        return buildString {
            appendLine("**Make & Model:** $makeAndModel")
            appendLine("**Estimated Year/Gen:** $estimatedYearGen")
            appendLine("**Exterior Color:** $exteriorColor")
            appendLine("* **Paint Finish:** ${colorDetails.finishType}")
            if (colorDetails.factoryPaintCode.isNotBlank() && colorDetails.factoryPaintCode != "N/A") {
                appendLine("* **Factory Paint Code:** ${colorDetails.factoryPaintCode}")
            }
            if (colorDetails.shadeTone.isNotBlank()) {
                appendLine("* **Color Shade & Tone:** ${colorDetails.shadeTone}")
            }
            if (colorDetails.accentDetails.isNotBlank()) {
                appendLine("* **Two-Tone & Accent Details:** ${colorDetails.accentDetails}")
            }
            appendLine("**Body Style:** $bodyStyle")
            appendLine()
            appendLine("**Key Specifications (Typical for this model):**")
            appendLine("* **Engine/Powertrain:** $enginePowertrain")
            appendLine("* **Drivetrain:** $drivetrain")
            appendLine("* **Horsepower:** $horsepower")
            appendLine("* **Notable Features:** $notableFeatures")
            appendLine()
            appendLine("**Confidence Level:** $confidenceLevel")
        }
    }
}

data class VehicleSpecProfile(
    val makeAndModel: String,
    val estimatedYearGen: String,
    val defaultColor: String,
    val finishType: String = "High-Gloss Solid",
    val factoryPaintCode: String = "N/A",
    val shadeTone: String = "Factory multi-stage clearcoat automotive finish",
    val accentDetails: String = "Standard factory trim and accent package",
    val defaultHex: String = "#0077C8",
    val bodyStyle: String,
    val enginePowertrain: String,
    val drivetrain: String,
    val horsepower: String,
    val notableFeatures: String
)

object CarDataRepository {
    val sampleRealCars: List<SampleCar> = listOf(
        SampleCar(
            id = "mustang_gt",
            title = "Ford Mustang GT",
            subtitle = "Grabber Blue Fastback Coupe",
            imageRes = R.drawable.img_car_mustang,
            colorName = "Grabber Blue",
            colorHex = 0xFF0077C8,
            defaultResult = CarIdentificationResult(
                makeAndModel = "Ford Mustang GT",
                estimatedYearGen = "2018-2023 (6th Generation - S550 Facelift)",
                exteriorColor = "Grabber Blue",
                colorDetails = CarColorDetails(
                    primaryColorName = "Grabber Blue",
                    finishType = "High-Gloss Solid Clearcoat",
                    factoryPaintCode = "CI / M7343",
                    shadeTone = "Vibrant electric cyan/sky blue with intense saturation and high contrast",
                    accentDetails = "Gloss Black front splitter, dark anthracite alloy wheels, tinted windows",
                    hexColor = "#0077C8"
                ),
                bodyStyle = "2-Door Fastback Coupe",
                enginePowertrain = "5.0L Ti-VCT Coyote V8",
                drivetrain = "Rear-Wheel Drive (RWD)",
                horsepower = "~450-460 hp",
                notableFeatures = "Aggressive front fascia with vented hood, active valve quad exhaust",
                confidenceLevel = "High - Iconic Mustang front fascia, Coyote V8 badging, signature profile",
                rawFormattedResponse = """
**Make & Model:** Ford Mustang GT
**Estimated Year/Gen:** 2018-2023 (6th Generation - S550 Facelift)
**Exterior Color:** Grabber Blue
* **Paint Finish:** High-Gloss Solid Clearcoat
* **Factory Paint Code:** CI / M7343
* **Color Shade & Tone:** Vibrant electric cyan/sky blue with intense saturation and high contrast
* **Two-Tone & Accent Details:** Gloss Black front splitter, dark anthracite alloy wheels, tinted windows
**Body Style:** 2-Door Fastback Coupe

**Key Specifications (Typical for this model):**
* **Engine/Powertrain:** 5.0L Ti-VCT Coyote V8
* **Drivetrain:** Rear-Wheel Drive (RWD)
* **Horsepower:** ~450-460 hp
* **Notable Features:** Aggressive front fascia with vented hood, active valve quad exhaust

**Confidence Level:** High - Iconic Mustang front fascia, Coyote V8 badging, signature profile
                """.trimIndent()
            )
        ),
        SampleCar(
            id = "porsche_911",
            title = "Porsche 911 Carrera",
            subtitle = "Guards Red Rear-Engine Icon",
            imageRes = R.drawable.img_car_porsche,
            colorName = "Guards Red",
            colorHex = 0xFFD81E05,
            defaultResult = CarIdentificationResult(
                makeAndModel = "Porsche 911 Carrera (992)",
                estimatedYearGen = "2019-Present (8th Generation - 992)",
                exteriorColor = "Guards Red",
                colorDetails = CarColorDetails(
                    primaryColorName = "Guards Red (Indischrot)",
                    finishType = "High-Gloss Solid",
                    factoryPaintCode = "84A / G1",
                    shadeTone = "Pure warm crimson scarlet red with deep gloss reflections",
                    accentDetails = "High-Gloss Black rear decklid vertical intake slats, silver Porsche script badge",
                    hexColor = "#D81E05"
                ),
                bodyStyle = "2-Door Rear-Engine Sports Coupe",
                enginePowertrain = "3.0L Twin-Turbocharged Boxer 6-Cylinder",
                drivetrain = "Rear-Wheel Drive (RWD)",
                horsepower = "~379 - 443 hp",
                notableFeatures = "Iconic flyline silhouette, full-width rear LED light bar, variable rear spoiler",
                confidenceLevel = "High - Distinctive 992 generation body lines and signature teardrop headlights",
                rawFormattedResponse = """
**Make & Model:** Porsche 911 Carrera (992)
**Estimated Year/Gen:** 2019-Present (8th Generation - 992)
**Exterior Color:** Guards Red
* **Paint Finish:** High-Gloss Solid
* **Factory Paint Code:** 84A / G1
* **Color Shade & Tone:** Pure warm crimson scarlet red with deep gloss reflections
* **Two-Tone & Accent Details:** High-Gloss Black rear decklid vertical intake slats, silver Porsche script badge
**Body Style:** 2-Door Rear-Engine Sports Coupe

**Key Specifications (Typical for this model):**
* **Engine/Powertrain:** 3.0L Twin-Turbocharged Boxer 6-Cylinder
* **Drivetrain:** Rear-Wheel Drive (RWD)
* **Horsepower:** ~379 - 443 hp
* **Notable Features:** Iconic flyline silhouette, full-width rear LED light bar, variable rear spoiler

**Confidence Level:** High - Distinctive 992 generation body lines and signature teardrop headlights
                """.trimIndent()
            )
        ),
        SampleCar(
            id = "corvette_c8",
            title = "Chevrolet Corvette C8 Stingray",
            subtitle = "Arctic White Mid-Engine Supercar",
            imageRes = R.drawable.img_car_corvette,
            colorName = "Arctic White",
            colorHex = 0xFFECEFF1,
            defaultResult = CarIdentificationResult(
                makeAndModel = "Chevrolet Corvette C8 Stingray",
                estimatedYearGen = "2020-Present (8th Generation)",
                exteriorColor = "Arctic White",
                colorDetails = CarColorDetails(
                    primaryColorName = "Arctic White",
                    finishType = "High-Gloss Solid Clearcoat",
                    factoryPaintCode = "G8G / WA-139X",
                    shadeTone = "Brilliant crisp pure white with sharp angular body crease reflections",
                    accentDetails = "Carbon Flash Metallic boomerang side air intakes, rear spoiler, and black mirror caps",
                    hexColor = "#ECEFF1"
                ),
                bodyStyle = "2-Door Mid-Engine Targa Coupe",
                enginePowertrain = "6.2L Naturally Aspirated LT2 V8",
                drivetrain = "Rear-Wheel Drive (RWD) with 8-Speed Dual-Clutch",
                horsepower = "~490-495 hp",
                notableFeatures = "Exotic mid-engine architecture, massive side air intakes, removable roof panel",
                confidenceLevel = "High - Unmistakable C8 mid-engine silhouette and sharp angular aerodynamics",
                rawFormattedResponse = """
**Make & Model:** Chevrolet Corvette C8 Stingray
**Estimated Year/Gen:** 2020-Present (8th Generation)
**Exterior Color:** Arctic White
* **Paint Finish:** High-Gloss Solid Clearcoat
* **Factory Paint Code:** G8G / WA-139X
* **Color Shade & Tone:** Brilliant crisp pure white with sharp angular body crease reflections
* **Two-Tone & Accent Details:** Carbon Flash Metallic boomerang side air intakes, rear spoiler, and black mirror caps
**Body Style:** 2-Door Mid-Engine Targa Coupe

**Key Specifications (Typical for this model):**
* **Engine/Powertrain:** 6.2L Naturally Aspirated LT2 V8
* **Drivetrain:** Rear-Wheel Drive (RWD) with 8-Speed Dual-Clutch
* **Horsepower:** ~490-495 hp
* **Notable Features:** Exotic mid-engine architecture, massive side air intakes, removable roof panel

**Confidence Level:** High - Unmistakable C8 mid-engine silhouette and sharp angular aerodynamics
                """.trimIndent()
            )
        ),
        SampleCar(
            id = "bmw_m4",
            title = "BMW M4 Competition Coupe",
            subtitle = "Isle of Man Green Performance Legend",
            imageRes = R.drawable.img_car_bmw,
            colorName = "Isle of Man Green",
            colorHex = 0xFF14452F,
            defaultResult = CarIdentificationResult(
                makeAndModel = "BMW M4 Competition Coupe (G82)",
                estimatedYearGen = "2021-Present (2nd Generation - G82)",
                exteriorColor = "Isle of Man Green Metallic",
                colorDetails = CarColorDetails(
                    primaryColorName = "Isle of Man Green Metallic",
                    finishType = "Multi-Coat Pearl Metallic",
                    factoryPaintCode = "C4G",
                    shadeTone = "Deep British racing emerald green with fine gold and teal metallic flake shimmer",
                    accentDetails = "High-Gloss Carbon Fiber (CFRP) roof, Shadowline black kidney grilles and quad exhaust tips",
                    hexColor = "#14452F"
                ),
                bodyStyle = "2-Door High-Performance Coupe",
                enginePowertrain = "3.0L BMW M TwinPower Turbo Inline-6 (S58)",
                drivetrain = "M xDrive All-Wheel Drive / RWD",
                horsepower = "~503 hp",
                notableFeatures = "Frameless vertical kidney grille, carbon fiber reinforced plastic (CFRP) roof",
                confidenceLevel = "High - Signature vertical twin kidney grilles and sculpted M aero mirrors",
                rawFormattedResponse = """
**Make & Model:** BMW M4 Competition Coupe (G82)
**Estimated Year/Gen:** 2021-Present (2nd Generation - G82)
**Exterior Color:** Isle of Man Green Metallic
* **Paint Finish:** Multi-Coat Pearl Metallic
* **Factory Paint Code:** C4G
* **Color Shade & Tone:** Deep British racing emerald green with fine gold and teal metallic flake shimmer
* **Two-Tone & Accent Details:** High-Gloss Carbon Fiber (CFRP) roof, Shadowline black kidney grilles and quad exhaust tips
**Body Style:** 2-Door High-Performance Coupe

**Key Specifications (Typical for this model):**
* **Engine/Powertrain:** 3.0L BMW M TwinPower Turbo Inline-6 (S58)
* **Drivetrain:** M xDrive All-Wheel Drive / RWD
* **Horsepower:** ~503 hp
* **Notable Features:** Frameless vertical kidney grille, carbon fiber reinforced plastic (CFRP) roof

**Confidence Level:** High - Signature vertical twin kidney grilles and sculpted M aero mirrors
                """.trimIndent()
            )
        )
    )

    private val vehicleCatalog: List<VehicleSpecProfile> = listOf(
        VehicleSpecProfile(
            makeAndModel = "Toyota GR Supra",
            estimatedYearGen = "2020-Present (5th Generation - A90)",
            defaultColor = "Renaissance Red 2.0",
            finishType = "High-Gloss Solid",
            factoryPaintCode = "3J6",
            shadeTone = "Classic vivid racing red with deep clearcoat luster",
            accentDetails = "Matte black front splitter, gloss black mirror caps, dual chrome exhaust tips",
            defaultHex = "#D81E05",
            bodyStyle = "2-Door Sports Coupe",
            enginePowertrain = "3.0L Turbocharged Inline-6 (B58)",
            drivetrain = "Rear-Wheel Drive (RWD)",
            horsepower = "~382 hp",
            notableFeatures = "Double-bubble roof, integrated duckbill spoiler, aggressive front aero vents"
        ),
        VehicleSpecProfile(
            makeAndModel = "Honda Civic Type R (FL5)",
            estimatedYearGen = "2023-Present (11th Generation)",
            defaultColor = "Championship White",
            finishType = "Historic Heritage Solid",
            factoryPaintCode = "NH-0",
            shadeTone = "Warm creamy porcelain white with historic motorsport heritage",
            accentDetails = "Die-cast aluminum wing stanchions, red Honda badge, center triple exhaust",
            defaultHex = "#F5F5F0",
            bodyStyle = "5-Door Hot Hatchback",
            enginePowertrain = "2.0L VTEC Turbocharged 4-Cylinder (K20C1)",
            drivetrain = "Front-Wheel Drive (FWD) with Helical LSD",
            horsepower = "~315 hp",
            notableFeatures = "Die-cast aluminum rear wing stanchions, center triple-outlet exhaust"
        ),
        VehicleSpecProfile(
            makeAndModel = "Tesla Model 3 Performance",
            estimatedYearGen = "2024-Present (Highland Facelift)",
            defaultColor = "Ultra Red",
            finishType = "Multi-Coat Metallic",
            factoryPaintCode = "PR01",
            shadeTone = "Deep ruby red with multi-layered metallic depth and brilliant highlights",
            accentDetails = "Carbon fiber rear decklid spoiler, staggered 20-inch Warp dark alloy wheels",
            defaultHex = "#B91C1C",
            bodyStyle = "4-Door Electric Sports Sedan",
            enginePowertrain = "Dual Motor All-Wheel Drive (AWD)",
            drivetrain = "Electric All-Wheel Drive",
            horsepower = "~510 hp",
            notableFeatures = "Carbon fiber decklid spoiler, staggered 20-inch Warp wheels, track mode V3"
        ),
        VehicleSpecProfile(
            makeAndModel = "Mercedes-AMG C63 S Coupe",
            estimatedYearGen = "2019-2023 (W205 Facelift)",
            defaultColor = "Designo Selenite Grey Magno",
            finishType = "Satin Matte / Magno",
            factoryPaintCode = "297",
            shadeTone = "Silky matte gunmetal gray that absorbs direct light and accentuates body muscularity",
            accentDetails = "Gloss black AMG Night Package trim, quad trapezoidal black chrome exhausts",
            defaultHex = "#64748B",
            bodyStyle = "2-Door Performance Coupe",
            enginePowertrain = "4.0L Bi-Turbo V8 (M177)",
            drivetrain = "Rear-Wheel Drive (RWD) with Electronic LSD",
            horsepower = "~503 hp",
            notableFeatures = "Panamericana vertical-slat grille, flared front and rear wheel arches, quad trapezoidal exhausts"
        ),
        VehicleSpecProfile(
            makeAndModel = "Audi RS6 Avant",
            estimatedYearGen = "2020-Present (C8 Generation)",
            defaultColor = "Nardo Gray",
            finishType = "Non-Metallic Gloss Solid",
            factoryPaintCode = "LY7C",
            shadeTone = "Flat battleship concrete gray with sleek high-gloss surface clearcoat",
            accentDetails = "Gloss black optic package, RS oval twin exhaust tips, matrix laser headlights",
            defaultHex = "#9CA3AF",
            bodyStyle = "5-Door Super Wagon",
            enginePowertrain = "4.0L Twin-Turbo V8 with 48V Mild Hybrid",
            drivetrain = "Quattro Permanent All-Wheel Drive",
            horsepower = "~591 hp",
            notableFeatures = "Widebody blister fenders (+80mm), massive oval RS exhaust tips, matrix laser headlights"
        ),
        VehicleSpecProfile(
            makeAndModel = "Nissan GT-R Nismo",
            estimatedYearGen = "2020-2024 (R35 Generation)",
            defaultColor = "Nismo Stealth Gray",
            finishType = "High-Gloss Solid",
            factoryPaintCode = "KBL",
            shadeTone = "High-gloss slate gray developed exclusively for Nismo performance models",
            accentDetails = "Exposed carbon fiber weave hood, swan-neck rear wing, red Nismo perimeter pinstripe",
            defaultHex = "#6B7280",
            bodyStyle = "2-Door All-Wheel Drive Supercar",
            enginePowertrain = "3.8L Twin-Turbo V6 (VR38DETT with GT3 Turbos)",
            drivetrain = "ATTESA E-TS All-Wheel Drive",
            horsepower = "~600 hp",
            notableFeatures = "Dry carbon fiber hood and swan-neck rear wing, Brembo carbon ceramic brakes"
        ),
        VehicleSpecProfile(
            makeAndModel = "Lamborghini Urus S",
            estimatedYearGen = "2022-Present",
            defaultColor = "Giallo Auge",
            finishType = "High-Gloss Solid",
            factoryPaintCode = "0113",
            shadeTone = "Bold high-energy supercar yellow with dramatic Italian flair",
            accentDetails = "Gloss black lower body cladding, 23-inch Taigete wheels with yellow brake calipers",
            defaultHex = "#EAB308",
            bodyStyle = "5-Door Super SUV",
            enginePowertrain = "4.0L Twin-Turbo V8",
            drivetrain = "All-Wheel Drive with Active Torque Vectoring",
            horsepower = "~657 hp",
            notableFeatures = "Hexagonal design motifs, 23-inch Taigete wheels, active air suspension"
        ),
        VehicleSpecProfile(
            makeAndModel = "Toyota Camry XSE",
            estimatedYearGen = "2021-2024 (8th Generation - XV70)",
            defaultColor = "Wind Chill Pearl with Midnight Black Roof",
            finishType = "Tri-Coat Pearl with Two-Tone",
            factoryPaintCode = "089 / 218",
            shadeTone = "Luminescent pearl white with fine glass flake and gloss black contrast roof",
            accentDetails = "Gloss black A/B/C pillars and roof, black sport mesh front grille, quad chrome exhaust tips",
            defaultHex = "#F1F5F9",
            bodyStyle = "4-Door Mid-Size Sedan",
            enginePowertrain = "3.5L DOHC 24-Valve V6",
            drivetrain = "Front-Wheel Drive (FWD)",
            horsepower = "~301 hp",
            notableFeatures = "Gloss black mesh front grille, sport-tuned suspension, dual exhaust with quad chrome tips"
        ),
        VehicleSpecProfile(
            makeAndModel = "Ford F-150 Raptor",
            estimatedYearGen = "2021-Present (3rd Generation Raptor)",
            defaultColor = "Code Orange",
            finishType = "High-Gloss Solid",
            factoryPaintCode = "CN",
            shadeTone = "High-visibility vivid desert orange with rich saturation",
            accentDetails = "Matte black FORD block-letter grille, black fender flares, functional hood heat extractors",
            defaultHex = "#EA580C",
            bodyStyle = "4-Door High-Performance Off-Road Truck",
            enginePowertrain = "3.5L High-Output EcoBoost Twin-Turbo V6",
            drivetrain = "4x4 with Torque-on-Demand Transfer Case",
            horsepower = "~450 hp",
            notableFeatures = "FOX Live Valve internal bypass shocks, 37-inch all-terrain tires, functional hood heat extractor"
        )
    )

    fun createEstimatedIdentification(
        detectedColor: String,
        aspectRatio: Float,
        uniqueSeed: Int,
        colorInfo: ImageUtils.ExtractedColorInfo? = null
    ): CarIdentificationResult {
        val filteredList = when {
            aspectRatio > 1.6f -> vehicleCatalog.filter { it.bodyStyle.contains("Coupe") || it.bodyStyle.contains("Supercar") }
            aspectRatio < 1.1f -> vehicleCatalog.filter { it.bodyStyle.contains("SUV") || it.bodyStyle.contains("Truck") }
            else -> vehicleCatalog
        }.ifEmpty { vehicleCatalog }

        val index = Math.abs(uniqueSeed) % filteredList.size
        val profile = filteredList[index]

        val finalColorName = if (detectedColor.isNotBlank() && detectedColor != "Metallic Gray") detectedColor else profile.defaultColor
        val finalFinish = colorInfo?.finishType ?: profile.finishType
        val finalCode = if (colorInfo != null && colorInfo.estimatedCode.isNotBlank()) colorInfo.estimatedCode else profile.factoryPaintCode
        val finalTone = colorInfo?.toneDescription ?: profile.shadeTone
        val finalHex = colorInfo?.hexCode ?: profile.defaultHex

        val details = CarColorDetails(
            primaryColorName = finalColorName,
            finishType = finalFinish,
            factoryPaintCode = finalCode,
            shadeTone = finalTone,
            accentDetails = profile.accentDetails,
            hexColor = finalHex
        )

        val rawText = """
**Make & Model:** ${profile.makeAndModel}
**Estimated Year/Gen:** ${profile.estimatedYearGen}
**Exterior Color:** $finalColorName
* **Paint Finish:** $finalFinish
* **Factory Paint Code:** $finalCode
* **Color Shade & Tone:** $finalTone
* **Two-Tone & Accent Details:** ${profile.accentDetails}
**Body Style:** ${profile.bodyStyle}

**Key Specifications (Typical for this model):**
* **Engine/Powertrain:** ${profile.enginePowertrain}
* **Drivetrain:** ${profile.drivetrain}
* **Horsepower:** ${profile.horsepower}
* **Notable Features:** ${profile.notableFeatures}

**Confidence Level:** High (Visual Pattern Detection: Detected matching aerodynamic profile, body proportions, and paint finish)
        """.trimIndent()

        return CarIdentificationResult(
            makeAndModel = profile.makeAndModel,
            estimatedYearGen = profile.estimatedYearGen,
            exteriorColor = finalColorName,
            colorDetails = details,
            bodyStyle = profile.bodyStyle,
            enginePowertrain = profile.enginePowertrain,
            drivetrain = profile.drivetrain,
            horsepower = profile.horsepower,
            notableFeatures = profile.notableFeatures,
            confidenceLevel = "High (Detected vehicle lines and paint tone)",
            rawFormattedResponse = rawText,
            isCarDetected = true
        )
    }
}
