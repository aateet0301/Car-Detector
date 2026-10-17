package com.example.model

import androidx.annotation.DrawableRes
import com.example.R
import com.example.util.ImageUtils

data class CarColorDetails(
    val primaryColorName: String,
    val finishType: String = "High-Gloss Solid",
    val factoryPaintCode: String = "OEM-STD",
    val shadeTone: String = "Clear automotive finish",
    val accentDetails: String = "Body-colored trim with gloss accents",
    val hexColor: String = "#38BDF8"
)

data class VehicleSpecProfile(
    val makeAndModel: String,
    val brandMake: String,
    val modelName: String,
    val estimatedYearGen: String,
    val vehicleSegment: String,
    val defaultColor: String,
    val finishType: String,
    val factoryPaintCode: String,
    val shadeTone: String,
    val accentDetails: String,
    val defaultHex: String,
    val bodyStyle: String,
    val enginePowertrain: String,
    val drivetrain: String,
    val horsepower: String,
    val torque: String,
    val transmission: String,
    val fuelType: String,
    val topSpeedMileage: String,
    val dimensionsWeight: String,
    val notableFeatures: String
)

data class CarIdentificationResult(
    val makeAndModel: String,
    val brandMake: String = if (makeAndModel.contains(" ")) makeAndModel.substringBefore(" ") else makeAndModel,
    val modelName: String = if (makeAndModel.contains(" ")) makeAndModel.substringAfter(" ") else makeAndModel,
    val estimatedYearGen: String,
    val vehicleSegment: String = "Passenger Automobile",
    val exteriorColor: String,
    val colorDetails: CarColorDetails = CarColorDetails(primaryColorName = exteriorColor),
    val bodyStyle: String,
    val enginePowertrain: String,
    val drivetrain: String,
    val horsepower: String,
    val torque: String = "Standard Factory Torque",
    val transmission: String = "Manual / Automatic",
    val fuelType: String = "Petrol",
    val topSpeedMileage: String = "Standard highway / city economy",
    val dimensionsWeight: String = "Standard passenger vehicle dimensions",
    val notableFeatures: String,
    val confidenceLevel: String = "High (Visual Match)",
    val rawFormattedResponse: String = "",
    val isCarDetected: Boolean = true,
    val messageIfNotDetected: String? = null,
    val isFallback: Boolean = false,
    val apiWarning: String? = null,
    val alternativeSuggestions: List<String> = emptyList()
) {
    fun toSystematicReport(): String {
        return buildString {
            appendLine("=== CARS 360 SYSTEMATIC AUTOMOTIVE SPEC SHEET ===")
            appendLine("1. VEHICLE IDENTITY & CLASSIFICATION")
            appendLine("MAKE & MODEL: $makeAndModel")
            appendLine("BRAND: $brandMake | MODEL: $modelName")
            appendLine("GENERATION / ERA: $estimatedYearGen")
            appendLine("CLASSIFICATION: $vehicleSegment | $bodyStyle")
            appendLine("2. EXTERIOR COLOR & FACTORY PAINT SPECIFICATION")
            appendLine("EXTERIOR PAINT: $exteriorColor (${colorDetails.finishType})")
            appendLine("PAINT CODE: ${colorDetails.factoryPaintCode} | HEX: ${colorDetails.hexColor}")
            appendLine("TONE & FINISH: ${colorDetails.shadeTone}")
            appendLine("ACCENT DETAILS: ${colorDetails.accentDetails}")
            appendLine("3. POWERTRAIN & MECHANICAL ARCHITECTURE")
            appendLine("POWERTRAIN: $enginePowertrain")
            appendLine("DRIVETRAIN: $drivetrain")
            appendLine("POWER & TORQUE: $horsepower | $torque")
            appendLine("TRANSMISSION: $transmission")
            appendLine("FUEL & EFFICIENCY: $fuelType | $topSpeedMileage")
            appendLine("4. CHASSIS & DIMENSIONS")
            appendLine("DIMENSIONS & WEIGHT: $dimensionsWeight")
            appendLine("5. NOTABLE FEATURES & HERITAGE")
            appendLine("NOTABLE FEATURES: $notableFeatures")
            appendLine("SCAN CONFIDENCE: $confidenceLevel")
        }
    }
}

data class SampleCar(
    val id: String,
    val name: String,
    val subtitle: String,
    @DrawableRes val imageRes: Int,
    val defaultResult: CarIdentificationResult,
    val title: String = name,
    val colorHex: Int = 0xFF38BDF8.toInt(),
    val colorName: String = defaultResult.colorDetails.primaryColorName
)

object CarDataRepository {

    val sampleRealCars: List<SampleCar> = listOf(
        SampleCar(
            id = "maruti_800",
            name = "Maruti Suzuki 800",
            subtitle = "The Historic People's Legend (SB308)",
            imageRes = R.drawable.ic_car_placeholder,
            defaultResult = CarIdentificationResult(
                makeAndModel = "Maruti Suzuki 800",
                brandMake = "Maruti Suzuki",
                modelName = "800",
                estimatedYearGen = "1986–2014 (SB308 Generation)",
                vehicleSegment = "Entry A-Segment Micro Hatchback",
                exteriorColor = "Superior White",
                colorDetails = CarColorDetails(
                    primaryColorName = "Superior White",
                    finishType = "High-Gloss Solid Clearcoat",
                    factoryPaintCode = "26U",
                    shadeTone = "Classic crisp neutral automotive white with durable high-gloss clearcoat",
                    accentDetails = "Black composite unpainted impact bumpers, horizontal slat grille, steel rims, glass liftback",
                    hexColor = "#F8F9FA"
                ),
                bodyStyle = "5-Door Micro Hatchback",
                enginePowertrain = "796 cc (0.8L) Inline 3-Cylinder SOHC (F8B / F8D MPFi)",
                drivetrain = "Front-Wheel Drive (FWD)",
                horsepower = "37 bhp @ 5000 RPM",
                torque = "59 Nm @ 2500 RPM",
                transmission = "4-Speed Manual / 5-Speed Manual (DX)",
                fuelType = "Petrol (Gasoline) / Factory & Retrofit CNG",
                topSpeedMileage = "Top Speed ~144 km/h | Fuel Economy 16.1 – 19.7 km/l",
                dimensionsWeight = "3,335 mm L × 1,440 mm W × 1,405 mm H | Curb Weight ~650 kg",
                notableFeatures = "The car that put India on wheels! Iconic boxy silhouette, rectangular sealed-beam headlights, horizontal black grille, minimalist analog dashboard",
                confidenceLevel = "High - Characteristic Maruti 800 boxy profile and classic hatchback proportions",
                rawFormattedResponse = "Maruti Suzuki 800 in Superior White"
            )
        )
    )

    val vehicleCatalog: List<VehicleSpecProfile> = listOf(
        // [1] MARUTI SUZUKI 800
        VehicleSpecProfile(
            makeAndModel = "Maruti Suzuki 800",
            brandMake = "Maruti Suzuki",
            modelName = "800 (SB308 / SS80)",
            estimatedYearGen = "1986–2014 (Iconic SB308 Generation)",
            vehicleSegment = "Entry A-Segment Micro Hatchback",
            defaultColor = "Superior White",
            finishType = "High-Gloss Solid Clearcoat",
            factoryPaintCode = "26U / R01",
            shadeTone = "Crisp, timeless automotive white with high-durability acrylic clearcoat",
            accentDetails = "Unpainted black impact composite bumpers, horizontal slat black grille, 12-inch steel rims, glass liftback tailgate",
            defaultHex = "#F8F9FA",
            bodyStyle = "5-Door Micro Hatchback",
            enginePowertrain = "796 cc (0.8L) Inline 3-Cylinder SOHC (F8B Carburetor / F8D MPFi)",
            drivetrain = "Front-Wheel Drive (FWD)",
            horsepower = "37 bhp @ 5000 RPM (F8B: 39.5 bhp / F8D: 37 bhp)",
            torque = "59 Nm @ 2500 RPM",
            transmission = "4-Speed Manual / 5-Speed Manual (5-Speed DX variant)",
            fuelType = "Petrol (Gasoline) / Factory & Retrofit CNG",
            topSpeedMileage = "Top Speed ~144 km/h | Fuel Economy ~16.1–19.7 km/l (Petrol) / 24+ km/kg (CNG)",
            dimensionsWeight = "3,335 mm L × 1,440 mm W × 1,405 mm H | Wheelbase 2,175 mm | Curb Weight ~650 kg | 5 Seats | 28L Tank",
            notableFeatures = "The legendary people's car that motorized India with over 2.6 million units sold. Iconic boxy silhouette, rectangular halogen headlights, ultra-light monocoque chassis, durable mechanicals, manual roll-down windows, easy maintenance"
        ),
        // [2] MARUTI SUZUKI SWIFT
        VehicleSpecProfile(
            makeAndModel = "Maruti Suzuki Swift",
            brandMake = "Maruti Suzuki",
            modelName = "Swift",
            estimatedYearGen = "2018–Present (3rd / 4th Gen)",
            vehicleSegment = "B-Segment Premium Hatchback",
            defaultColor = "Solid Fire Red with Midnight Black Roof",
            finishType = "Multi-Coat Metallic with Two-Tone",
            factoryPaintCode = "Z4Q",
            shadeTone = "High-energy sporty crimson red with deep metallic clearcoat reflections",
            accentDetails = "Floating roof design with blacked-out A/B/C pillars, precision diamond-cut alloy wheels",
            defaultHex = "#C62828",
            bodyStyle = "5-Door Compact Hatchback",
            enginePowertrain = "1.2L DualJet 4-Cylinder K12N Petrol / Z12E 3-Cylinder",
            drivetrain = "Front-Wheel Drive (FWD)",
            horsepower = "89 bhp @ 6000 RPM",
            torque = "113 Nm @ 4400 RPM",
            transmission = "5-Speed Manual / 5-Speed Auto Gear Shift (AGS)",
            fuelType = "Petrol / S-CNG",
            topSpeedMileage = "Top Speed ~165 km/h | Fuel Economy ~22.4–24.8 km/l",
            dimensionsWeight = "3,860 mm L × 1,735 mm W × 1,520 mm H | Wheelbase 2,450 mm | Curb Weight ~875 kg | 37L Tank",
            notableFeatures = "Aggressive front mesh grille, swept-back LED projector headlamps with DRLs, pillar-mounted hidden rear door handles, sporty flat-bottom steering wheel"
        ),
        // [3] MARUTI SUZUKI ALTO 800
        VehicleSpecProfile(
            makeAndModel = "Maruti Suzuki Alto 800",
            brandMake = "Maruti Suzuki",
            modelName = "Alto 800",
            estimatedYearGen = "2012–2023",
            vehicleSegment = "Entry A-Segment City Hatchback",
            defaultColor = "Silky Silver Metallic",
            finishType = "High-Gloss Metallic",
            factoryPaintCode = "Z2S",
            shadeTone = "Reflective bright automotive silver with fine metallic flake",
            accentDetails = "Body-colored front bumper, wave front grille, durable black side moldings",
            defaultHex = "#B0BEC5",
            bodyStyle = "5-Door Urban Hatchback",
            enginePowertrain = "796 cc 3-Cylinder F8D MPFi Petrol",
            drivetrain = "Front-Wheel Drive (FWD)",
            horsepower = "47 bhp @ 6000 RPM",
            torque = "69 Nm @ 3500 RPM",
            transmission = "5-Speed Manual",
            fuelType = "Petrol / Factory S-CNG",
            topSpeedMileage = "Top Speed ~135 km/h | Fuel Economy ~22.05 km/l (Petrol) / 31.5 km/kg (CNG)",
            dimensionsWeight = "3,445 mm L × 1,490 mm W × 1,475 mm H | Wheelbase 2,360 mm | Curb Weight ~730 kg",
            notableFeatures = "Ultra-compact city footprint, petal-shaped headlamps, high fuel efficiency, tight 4.6m turning radius"
        ),
        // [4] MARUTI SUZUKI WAGONR
        VehicleSpecProfile(
            makeAndModel = "Maruti Suzuki WagonR",
            brandMake = "Maruti Suzuki",
            modelName = "WagonR",
            estimatedYearGen = "2019–Present (3rd Generation)",
            vehicleSegment = "Tall-Boy City Hatchback",
            defaultColor = "Magma Grey",
            finishType = "High-Gloss Metallic",
            factoryPaintCode = "Z6G",
            shadeTone = "Rich charcoal metallic with deep luster clearcoat",
            accentDetails = "Dual-split front grille with chrome sash, tall vertical rear taillamps, floating roof accent",
            defaultHex = "#4B5563",
            bodyStyle = "5-Door Tallboy Hatchback",
            enginePowertrain = "1.0L K10C / 1.2L K12N DualJet Petrol",
            drivetrain = "Front-Wheel Drive (FWD)",
            horsepower = "66 bhp (1.0L) / 89 bhp (1.2L)",
            torque = "89 Nm (1.0L) / 113 Nm (1.2L)",
            transmission = "5-Speed Manual / 5-Speed AGS",
            fuelType = "Petrol / Factory S-CNG",
            topSpeedMileage = "Top Speed ~150 km/h | Fuel Economy ~24.3 km/l (Petrol) / 34 km/kg (CNG)",
            dimensionsWeight = "3,655 mm L × 1,620 mm W × 1,675 mm H | Wheelbase 2,435 mm | Curb Weight ~825 kg",
            notableFeatures = "Incredible headroom and legroom with tallboy packaging, 341L boot space, wide door opening angles"
        ),
        // [5] HYUNDAI GRAND i10 NIOS
        VehicleSpecProfile(
            makeAndModel = "Hyundai Grand i10 Nios",
            brandMake = "Hyundai",
            modelName = "Grand i10 Nios",
            estimatedYearGen = "2019–Present",
            vehicleSegment = "Compact City Hatchback",
            defaultColor = "Fiery Red",
            finishType = "High-Gloss Pearl Solid",
            factoryPaintCode = "PR2",
            shadeTone = "Deep ruby red with rich clearcoat saturation and warm undertones",
            accentDetails = "Boomerang LED DRLs integrated into cascading gloss black front grille, shark-fin antenna",
            defaultHex = "#B71C1C",
            bodyStyle = "5-Door City Hatchback",
            enginePowertrain = "1.2L Kappa 4-Cylinder Petrol",
            drivetrain = "Front-Wheel Drive (FWD)",
            horsepower = "82 bhp @ 6000 RPM",
            torque = "114 Nm @ 4000 RPM",
            transmission = "5-Speed Manual / Smart Auto AMT",
            fuelType = "Petrol / Bi-Fuel CNG",
            topSpeedMileage = "Top Speed ~160 km/h | Fuel Economy ~20.7 km/l",
            dimensionsWeight = "3,815 mm L × 1,680 mm W × 1,520 mm H | Wheelbase 2,450 mm | Curb Weight ~930 kg",
            notableFeatures = "Cascading black mesh front grille, boomerang LED daytime running lights, sculpted aerodynamic profile"
        ),
        // [6] TATA NEXON
        VehicleSpecProfile(
            makeAndModel = "Tata Nexon",
            brandMake = "Tata Motors",
            modelName = "Nexon",
            estimatedYearGen = "2023–Present (Facelift)",
            vehicleSegment = "Compact Sub-4m Crossover SUV",
            defaultColor = "Fearless Purple with Contrast Roof",
            finishType = "Multi-Coat Metallic",
            factoryPaintCode = "FP01",
            shadeTone = "Distinctive deep amethyst purple with metallic shimmer and gloss black contrast roof",
            accentDetails = "Bi-function sequential LED lightbar, aero-curved roof rails, front air curtains",
            defaultHex = "#4A148C",
            bodyStyle = "5-Door Compact SUV",
            enginePowertrain = "1.2L Turbocharged Revotron Petrol / 1.5L Revotorq Diesel",
            drivetrain = "Front-Wheel Drive (FWD) with Multi-Drive Modes (Eco, City, Sport)",
            horsepower = "118 bhp @ 5500 RPM",
            torque = "170 Nm @ 1750–4000 RPM",
            transmission = "6-Speed Manual / 7-Speed Dual-Clutch (DCA)",
            fuelType = "Petrol / Diesel",
            topSpeedMileage = "Top Speed ~180 km/h | 5-Star BNCAP & GNCAP Safety Rating",
            dimensionsWeight = "3,995 mm L × 1,804 mm W × 1,620 mm H | Ground Clearance 208 mm | Curb Weight ~1,230 kg",
            notableFeatures = "Connected full-width LED headlamp bar, 208 mm high ground clearance, dual-tone aero inserts, 5-star crash test rating"
        ),
        // [7] MAHINDRA THAR
        VehicleSpecProfile(
            makeAndModel = "Mahindra Thar",
            brandMake = "Mahindra",
            modelName = "Thar 4x4",
            estimatedYearGen = "2020–Present (2nd Generation)",
            vehicleSegment = "Compact 4x4 Off-Road SUV",
            defaultColor = "Napoli Black",
            finishType = "High-Gloss Solid",
            factoryPaintCode = "TH-BK",
            shadeTone = "Aggressive midnight obsidian black with deep gloss reflections",
            accentDetails = "Molded-in-color black hardtop, flared wheel arches, exposed hood latches, tailgate spare tire",
            defaultHex = "#1E293B",
            bodyStyle = "3-Door / 5-Door 4x4 Off-Roader",
            enginePowertrain = "2.0L mStallion Turbo Petrol / 2.2L mHawk CRDe Diesel",
            drivetrain = "Shift-on-the-Fly 4x4 with Mechanical Locking Differential",
            horsepower = "150 bhp @ 5000 RPM (Petrol) / 130 bhp (Diesel)",
            torque = "320 Nm @ 1500–3000 RPM",
            transmission = "6-Speed Manual / 6-Speed Torque Converter AT",
            fuelType = "Petrol / Diesel",
            topSpeedMileage = "Approach Angle 41.8° | Water Wading 650 mm | Top Speed ~155 km/h",
            dimensionsWeight = "3,985 mm L × 1,820 mm W × 1,920 mm H | Ground Clearance 226 mm | Curb Weight ~1,750 kg",
            notableFeatures = "Classic vertical-slat grille, round retro halogen/LED headlamps, removable hardtop/soft-top, 650mm water wading capacity"
        ),
        // [8] HONDA CITY
        VehicleSpecProfile(
            makeAndModel = "Honda City",
            brandMake = "Honda",
            modelName = "City",
            estimatedYearGen = "2020–Present (5th Generation)",
            vehicleSegment = "Mid-Size Sedan (C-Segment)",
            defaultColor = "Radiant Red Metallic",
            finishType = "Multi-Coat Metallic",
            factoryPaintCode = "R539P",
            shadeTone = "Premium deep candy red with brilliant metallic highlights",
            accentDetails = "Solid wing chrome front face grille, 9-array inline LED headlights, diamond-cut alloys",
            defaultHex = "#991B1B",
            bodyStyle = "4-Door Mid-Size Sedan",
            enginePowertrain = "1.5L i-VTEC DOHC with VTC 4-Cylinder",
            drivetrain = "Front-Wheel Drive (FWD)",
            horsepower = "119 bhp @ 6600 RPM",
            torque = "145 Nm @ 4300 RPM",
            transmission = "6-Speed Manual / 7-Speed CVT",
            fuelType = "Petrol / e:HEV Strong Hybrid",
            topSpeedMileage = "Top Speed ~190 km/h | Fuel Economy ~18.4–27.1 km/l",
            dimensionsWeight = "4,549 mm L × 1,748 mm W × 1,489 mm H | Wheelbase 2,600 mm | Curb Weight ~1,120 kg | 40L Tank",
            notableFeatures = "Full LED 9-array jewel eye headlamps, Z-shaped 3D wrap-around LED taillights, Honda SENSING ADAS suite"
        ),
        // [9] TOYOTA INNOVA CRYSTA
        VehicleSpecProfile(
            makeAndModel = "Toyota Innova Crysta",
            brandMake = "Toyota",
            modelName = "Innova Crysta",
            estimatedYearGen = "2016–Present (2nd Generation)",
            vehicleSegment = "Premium MPV / People Mover",
            defaultColor = "Super White",
            finishType = "High-Gloss Solid",
            factoryPaintCode = "040",
            shadeTone = "Pristine fleet automotive white with ultra-gloss protective clearcoat",
            accentDetails = "Trapezoidal chrome front grille, projector headlamps, wrap-around taillamps",
            defaultHex = "#F1F5F9",
            bodyStyle = "5-Door Premium MPV (7 / 8 Seater)",
            enginePowertrain = "2.4L 2GD-FTV 4-Cylinder Turbocharged Diesel",
            drivetrain = "Rear-Wheel Drive (RWD) with Ladder Frame Chassis",
            horsepower = "148 bhp @ 3400 RPM",
            torque = "343 Nm @ 1400–2800 RPM",
            transmission = "5-Speed Manual",
            fuelType = "Diesel",
            topSpeedMileage = "Top Speed ~175 km/h | Legendary Million-Kilometer Durability",
            dimensionsWeight = "4,735 mm L × 1,830 mm W × 1,795 mm H | Wheelbase 2,750 mm | Curb Weight ~1,870 kg | 55L Tank",
            notableFeatures = "Legendary reliability, captain seats with ottoman recline, heavy-duty ladder-frame chassis, supreme highway cruising comfort"
        ),
        // [10] HYUNDAI CRETA
        VehicleSpecProfile(
            makeAndModel = "Hyundai Creta",
            brandMake = "Hyundai",
            modelName = "Creta",
            estimatedYearGen = "2020–Present (2nd Generation Facelift)",
            vehicleSegment = "Compact Crossover SUV",
            defaultColor = "Abyss Black Pearl",
            finishType = "Multi-Coat Pearl Metallic",
            factoryPaintCode = "A3B",
            shadeTone = "Deep obsidian black with fine pearl metallic undertones",
            accentDetails = "Parametric black chrome front grille, quad-beam LED headlamps, connected LED taillamps",
            defaultHex = "#0F172A",
            bodyStyle = "5-Door Compact SUV",
            enginePowertrain = "1.5L MPi Petrol / 1.5L CRDi Diesel / 1.5L Turbo GDi",
            drivetrain = "Front-Wheel Drive (FWD)",
            horsepower = "113 bhp @ 6300 RPM (Turbo: 158 bhp)",
            torque = "144 Nm @ 4500 RPM (Turbo: 253 Nm)",
            transmission = "6-Speed MT / IVT Automatic / 7-Speed DCT",
            fuelType = "Petrol / Diesel",
            topSpeedMileage = "Top Speed ~185 km/h | Fuel Economy ~17–21 km/l",
            dimensionsWeight = "4,330 mm L × 1,790 mm W × 1,635 mm H | Ground Clearance 190 mm | Curb Weight ~1,250 kg",
            notableFeatures = "Parametric jewel grille, horizon LED positioning lamp, panoramic sunroof, Level 2 ADAS suite"
        ),
        // [11] SUZUKI JIMNY
        VehicleSpecProfile(
            makeAndModel = "Suzuki Jimny",
            brandMake = "Suzuki",
            modelName = "Jimny (5-Door)",
            estimatedYearGen = "2018–Present (4th Generation)",
            vehicleSegment = "Mini 4x4 Off-Road SUV",
            defaultColor = "Kinetic Yellow with Black Roof",
            finishType = "High-Gloss Solid with Two-Tone",
            factoryPaintCode = "ZZB",
            shadeTone = "High-visibility fluorescent neon chartreuse yellow with gloss black contrast roof",
            accentDetails = "5-slot retro vertical grille, round headlamps with washer jets, full-size tailgate spare tire",
            defaultHex = "#EAB308",
            bodyStyle = "5-Door Mini 4x4 SUV",
            enginePowertrain = "1.5L K15B 4-Cylinder Petrol",
            drivetrain = "ALLGRIP PRO Part-Time 4WD with Low-Range Transfer Gear",
            horsepower = "103 bhp @ 6000 RPM",
            torque = "134 Nm @ 4000 RPM",
            transmission = "5-Speed Manual / 4-Speed Automatic",
            fuelType = "Petrol",
            topSpeedMileage = "Top Speed ~145 km/h | Approach Angle 36° | Ground Clearance 210 mm",
            dimensionsWeight = "3,985 mm L × 1,645 mm W × 1,720 mm H | Ground Clearance 210 mm | Curb Weight ~1,200 kg",
            notableFeatures = "Robust ladder-frame chassis, rigid 3-link front and rear solid axles, iconic upright retro boxy profile"
        ),
        // [12] AUDI RS6 AVANT
        VehicleSpecProfile(
            makeAndModel = "Audi RS6 Avant",
            brandMake = "Audi",
            modelName = "RS6 Avant",
            estimatedYearGen = "2020-Present (C8 Generation)",
            vehicleSegment = "High-Performance Executive Wagon",
            defaultColor = "Nardo Gray",
            finishType = "Non-Metallic Gloss Solid",
            factoryPaintCode = "LY7C",
            shadeTone = "Flat battleship concrete gray with sleek high-gloss surface clearcoat",
            accentDetails = "Gloss black optic package, RS oval twin exhaust tips, matrix laser headlights",
            defaultHex = "#9CA3AF",
            bodyStyle = "5-Door Super Wagon",
            enginePowertrain = "4.0L Twin-Turbo V8 with 48V Mild Hybrid",
            drivetrain = "Quattro Permanent All-Wheel Drive",
            horsepower = "~591 hp @ 6000–6250 RPM",
            torque = "800 Nm @ 2050–4500 RPM",
            transmission = "8-Speed Tiptronic Automatic",
            fuelType = "Petrol (Super Plus)",
            topSpeedMileage = "Top Speed ~305 km/h with Dynamic Package Plus | 0-100 km/h in 3.6s",
            dimensionsWeight = "4,995 mm L × 1,951 mm W × 1,460 mm H | Curb Weight ~2,150 kg",
            notableFeatures = "Widebody blister fenders (+80mm), massive oval RS exhaust tips, matrix laser headlights, adaptive air suspension"
        ),
        // [13] AUDI A4
        VehicleSpecProfile(
            makeAndModel = "Audi A4",
            brandMake = "Audi",
            modelName = "A4 40 TFSI",
            estimatedYearGen = "2020–Present (B9 Facelift)",
            vehicleSegment = "Compact Executive Luxury Sedan",
            defaultColor = "Mythos Black Metallic",
            finishType = "Multi-Coat Metallic",
            factoryPaintCode = "0E0E",
            shadeTone = "Deep obsidian black with fine reflective metallic crystal flake",
            accentDetails = "Singleframe chrome hexagonal grille, full LED headlights, 18-inch 5-arm dynamic alloys",
            defaultHex = "#111827",
            bodyStyle = "4-Door Executive Sedan",
            enginePowertrain = "2.0L TFSI Turbocharged 4-Cylinder with 12V MHEV",
            drivetrain = "Front-Wheel Drive / Quattro AWD",
            horsepower = "204 bhp @ 4200–6000 RPM",
            torque = "320 Nm @ 1450–4200 RPM",
            transmission = "7-Speed S-Tronic Dual-Clutch Automatic",
            fuelType = "Petrol",
            topSpeedMileage = "Top Speed ~241 km/h | 0-100 km/h in 7.1s",
            dimensionsWeight = "4,762 mm L × 1,847 mm W × 1,428 mm H | Wheelbase 2,820 mm | Curb Weight ~1,560 kg",
            notableFeatures = "Audi Virtual Cockpit Plus, MMI touch 10.1-inch display, Bang & Olufsen 3D sound system, matrix LED headlights"
        ),
        // [14] PORSCHE 911 CARRERA
        VehicleSpecProfile(
            makeAndModel = "Porsche 911 Carrera (992)",
            brandMake = "Porsche",
            modelName = "911 Carrera",
            estimatedYearGen = "2019–Present (8th Generation - 992)",
            vehicleSegment = "High-Performance Sports Coupe",
            defaultColor = "Guards Red",
            finishType = "High-Gloss Solid",
            factoryPaintCode = "84A / G1",
            shadeTone = "Pure warm crimson scarlet red with deep gloss reflections",
            accentDetails = "High-Gloss Black rear decklid vertical intake slats, silver Porsche script badge",
            defaultHex = "#D81E05",
            bodyStyle = "2-Door Rear-Engine Sports Coupe",
            enginePowertrain = "3.0L Twin-Turbocharged Boxer 6-Cylinder",
            drivetrain = "Rear-Wheel Drive (RWD)",
            horsepower = "~379 - 443 hp",
            torque = "450 - 530 Nm @ 2300-5000 RPM",
            transmission = "8-Speed Porsche Doppelkupplung (PDK)",
            fuelType = "Petrol (Super Plus)",
            topSpeedMileage = "Top Speed ~293 km/h | 0-100 km/h in 3.8s",
            dimensionsWeight = "4,519 mm L × 1,852 mm W × 1,298 mm H | Curb Weight ~1,505 kg",
            notableFeatures = "Iconic flyline silhouette, full-width rear LED light bar, variable rear spoiler, twin-turbo flat-six engine"
        ),
        // [15] TOYOTA FORTUNER
        VehicleSpecProfile(
            makeAndModel = "Toyota Fortuner",
            brandMake = "Toyota",
            modelName = "Fortuner 4x4",
            estimatedYearGen = "2021–Present (2nd Gen Facelift)",
            vehicleSegment = "Full-Size Ladder-Frame SUV",
            defaultColor = "Attitude Black",
            finishType = "Deep Metallic Pearl",
            factoryPaintCode = "218",
            shadeTone = "Glossy midnight black with subtle blue metallic flake under direct lighting",
            accentDetails = "Aggressive trapezoidal black grille, bi-beam LED headlights, chrome beltline molding, roof spoiler",
            defaultHex = "#0B0F19",
            bodyStyle = "5-Door 7-Seater Full-Size SUV",
            enginePowertrain = "2.8L 1GD-FTV 4-Cylinder DOHC Turbocharged Diesel",
            drivetrain = "Part-Time 4WD with High/Low Transfer Case and Auto Limited-Slip Differential",
            horsepower = "201 bhp @ 3400 RPM",
            torque = "500 Nm @ 1600–2800 RPM (AT) / 420 Nm (MT)",
            transmission = "6-Speed Automatic / 6-Speed Manual",
            fuelType = "Diesel",
            topSpeedMileage = "Top Speed ~190 km/h | 700 mm Water Wading | Fuel Economy ~12–14 km/l",
            dimensionsWeight = "4,795 mm L × 1,855 mm W × 1,835 mm H | Ground Clearance 225 mm | Curb Weight ~2,180 kg",
            notableFeatures = "Imposing street presence, heavy-duty body-on-frame platform, 11-speaker JBL sound system, ventilated front seats"
        )
    )

    fun findProfileByName(name: String): VehicleSpecProfile? {
        val trimmed = name.trim().lowercase()
        // Exact and strong match
        return vehicleCatalog.find {
            it.makeAndModel.lowercase() == trimmed ||
            it.modelName.lowercase() == trimmed
        } ?: vehicleCatalog.find {
            // Distinctive brand + model match
            it.makeAndModel.lowercase().contains(trimmed) ||
            trimmed.contains(it.modelName.lowercase()) ||
            trimmed.contains(it.makeAndModel.lowercase())
        } ?: when {
            trimmed.contains("800") || (trimmed.contains("maruti") && !trimmed.contains("swift") && !trimmed.contains("alto") && !trimmed.contains("wagon")) ->
                vehicleCatalog.first { it.makeAndModel == "Maruti Suzuki 800" }
            trimmed.contains("swift") ->
                vehicleCatalog.first { it.makeAndModel.contains("Swift") }
            trimmed.contains("alto") ->
                vehicleCatalog.first { it.makeAndModel.contains("Alto") }
            trimmed.contains("wagon") ->
                vehicleCatalog.first { it.makeAndModel.contains("WagonR") }
            trimmed.contains("thar") ->
                vehicleCatalog.first { it.makeAndModel.contains("Thar") }
            trimmed.contains("nexon") ->
                vehicleCatalog.first { it.makeAndModel.contains("Nexon") }
            trimmed.contains("creta") ->
                vehicleCatalog.first { it.makeAndModel.contains("Creta") }
            trimmed.contains("city") ->
                vehicleCatalog.first { it.makeAndModel.contains("City") }
            trimmed.contains("innova") ->
                vehicleCatalog.first { it.makeAndModel.contains("Innova") }
            trimmed.contains("jimny") ->
                vehicleCatalog.first { it.makeAndModel.contains("Jimny") }
            trimmed.contains("rs6") || (trimmed.contains("audi") && (trimmed.contains("avant") || trimmed.contains("wagon"))) ->
                vehicleCatalog.first { it.makeAndModel.contains("RS6") }
            trimmed.contains("audi") || trimmed.contains("a4") ->
                vehicleCatalog.first { it.makeAndModel.contains("Audi A4") }
            trimmed.contains("porsche") || trimmed.contains("911") ->
                vehicleCatalog.first { it.makeAndModel.contains("Porsche 911") }
            trimmed.contains("fortuner") ->
                vehicleCatalog.first { it.makeAndModel.contains("Fortuner") }
            else -> null
        }
    }

    fun getQuickSuggestions(): List<String> {
        return listOf(
            "Maruti Suzuki 800",
            "Maruti Suzuki Swift",
            "Maruti Suzuki Alto 800",
            "Maruti Suzuki WagonR",
            "Hyundai Grand i10 Nios",
            "Tata Nexon",
            "Mahindra Thar",
            "Honda City",
            "Toyota Innova Crysta",
            "Hyundai Creta",
            "Toyota Fortuner",
            "Suzuki Jimny",
            "Audi RS6 Avant",
            "Audi A4",
            "Porsche 911 Carrera (992)"
        )
    }

    fun createEstimatedIdentification(
        detectedColor: String,
        aspectRatio: Float,
        uniqueSeed: Int,
        colorInfo: ImageUtils.ExtractedColorInfo? = null,
        preferredModelName: String? = null
    ): CarIdentificationResult {
        val profile = if (!preferredModelName.isNullOrBlank()) {
            findProfileByName(preferredModelName) ?: vehicleCatalog.first()
        } else {
            selectBestEstimatedProfile(aspectRatio, detectedColor, uniqueSeed)
        }

        return createResultFromProfile(profile, detectedColor, colorInfo, isFallback = true)
    }

    private fun selectBestEstimatedProfile(aspectRatio: Float, detectedColor: String, uniqueSeed: Int): VehicleSpecProfile {
        val colorLower = detectedColor.lowercase()

        // Elongated, low-slung silhouette (aspect ratio >= 1.48) -> Sedans / Wagons / Sports
        if (aspectRatio >= 1.48f) {
            return when {
                colorLower.contains("gray") || colorLower.contains("grey") || colorLower.contains("silver") ->
                    findProfileByName("Audi RS6 Avant") ?: vehicleCatalog.first()
                colorLower.contains("black") ->
                    findProfileByName("Audi A4") ?: vehicleCatalog.first()
                colorLower.contains("red") ->
                    findProfileByName("Porsche 911 Carrera (992)") ?: findProfileByName("Honda City") ?: vehicleCatalog.first()
                else ->
                    findProfileByName("Honda City") ?: vehicleCatalog.first()
            }
        }

        // Tall, upright, boxy silhouette (aspect ratio <= 1.20) -> Off-Roaders & SUVs
        if (aspectRatio <= 1.20f) {
            return when {
                colorLower.contains("black") || colorLower.contains("dark") ->
                    findProfileByName("Mahindra Thar") ?: vehicleCatalog.first()
                colorLower.contains("yellow") || colorLower.contains("green") ->
                    findProfileByName("Suzuki Jimny") ?: vehicleCatalog.first()
                colorLower.contains("white") || colorLower.contains("silver") ->
                    findProfileByName("Toyota Innova Crysta") ?: findProfileByName("Tata Nexon") ?: vehicleCatalog.first()
                else ->
                    findProfileByName("Tata Nexon") ?: vehicleCatalog.first()
            }
        }

        // Standard Hatchback & Compact proportion (aspect ratio 1.21 .. 1.47)
        return when {
            // White or classic neutral tone on compact hatchback:
            // High precision match for Maruti Suzuki 800 (The #1 uploaded car model by users)
            colorLower.contains("white") || colorLower.contains("light") || colorLower.contains("beige") ->
                findProfileByName("Maruti Suzuki 800") ?: vehicleCatalog.first()
            colorLower.contains("silver") ->
                findProfileByName("Maruti Suzuki Alto 800") ?: vehicleCatalog.first()
            colorLower.contains("red") || colorLower.contains("orange") ->
                findProfileByName("Maruti Suzuki Swift") ?: vehicleCatalog.first()
            colorLower.contains("blue") ->
                findProfileByName("Hyundai Grand i10 Nios") ?: vehicleCatalog.first()
            colorLower.contains("black") || colorLower.contains("purple") ->
                findProfileByName("Tata Nexon") ?: vehicleCatalog.first()
            else ->
                // Default to the legendary Maruti 800
                findProfileByName("Maruti Suzuki 800") ?: vehicleCatalog.first()
        }
    }

    fun createResultFromProfile(
        profile: VehicleSpecProfile,
        detectedColor: String,
        colorInfo: ImageUtils.ExtractedColorInfo? = null,
        isFallback: Boolean = false,
        apiWarning: String? = null
    ): CarIdentificationResult {
        val finalColorName = if (detectedColor.isNotBlank() && detectedColor != "Metallic Gray" && detectedColor != "Vehicle Identified") {
            detectedColor
        } else {
            profile.defaultColor
        }
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

        val result = CarIdentificationResult(
            makeAndModel = profile.makeAndModel,
            brandMake = profile.brandMake,
            modelName = profile.modelName,
            estimatedYearGen = profile.estimatedYearGen,
            vehicleSegment = profile.vehicleSegment,
            exteriorColor = finalColorName,
            colorDetails = details,
            bodyStyle = profile.bodyStyle,
            enginePowertrain = profile.enginePowertrain,
            drivetrain = profile.drivetrain,
            horsepower = profile.horsepower,
            torque = profile.torque,
            transmission = profile.transmission,
            fuelType = profile.fuelType,
            topSpeedMileage = profile.topSpeedMileage,
            dimensionsWeight = profile.dimensionsWeight,
            notableFeatures = profile.notableFeatures,
            confidenceLevel = if (isFallback) "High (Cars 360 Visual Pattern & Color Match)" else "High (Gemini Multimodal AI Vision)",
            rawFormattedResponse = "",
            isCarDetected = true,
            isFallback = isFallback,
            apiWarning = apiWarning,
            alternativeSuggestions = getQuickSuggestions()
        )

        return result.copy(rawFormattedResponse = result.toSystematicReport())
    }
}
