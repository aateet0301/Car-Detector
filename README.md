# AutoLens AI 🏎️ — Automotive Vehicle & Specification Identifier

**AutoLens AI** is an advanced automotive identification and vehicle specification scanner for Android built with Kotlin and Jetpack Compose, powered by Google Gemini Multimodal Vision. Simply snap a photo with your camera or select an image from your device gallery to instantly identify the vehicle's exact make, model, generation/year, and detailed exterior paint finish.

---

## 🌟 Key Features

### 1. 📋 Systematic Specification Form Display
Every vehicle scan is displayed in a clean, systematic, numbered engineering spec sheet:
* **[1] Vehicle Identity & Classification**: Manufacturer/Make, Model Designation, Generation/Era, Vehicle Segment, Body Configuration.
* **[2] Exterior Color & Factory Paint Specification**: Primary OEM Colorway, Finish Classification (Solid, Metallic, Pearl Tri-Coat, Satin Matte), Factory Paint Code, Tone/Undertones/Luster, Accent & Trim Details, and an interactive Color Swatch with one-tap HEX code copying.
* **[3] Powertrain & Mechanical Architecture**: Engine Displacement, Configuration & Code, Max Horsepower, Peak Torque, Drivetrain Layout (FWD/RWD/AWD/4x4), Transmission, Fuel/Propulsion Type, and Top Speed / Fuel Economy metrics.
* **[4] Design Profile & Key Identifiers**: Aerodynamic cues, lighting design, silhouette markers, chassis construction, and curb weight.
* **[5] Systematic Spec Sheet Export**: Formatted monospace spec sheet with single-tap clipboard copy for sharing.

---

### 2. 🔍 Instant Vehicle Identification & Model Verification
* **Multimodal Visual Recognition**: Identifies cars across various angles (front, rear, profile, three-quarter), lighting environments, and vehicle categories.
* **Everyday, Indian, and Regional Vehicle Recognition**:
  * Accurate recognition of popular hatchbacks and daily drivers including **Maruti Suzuki 800** (796 cc F8B/F8D, 37 bhp, iconic boxy silhouette), **Maruti Suzuki Swift**, **Maruti Suzuki Alto 800**, **Hyundai Grand i10 Nios**, **Tata Nexon**, **Mahindra Thar 4x4**, **Honda City**, **Suzuki Jimny**, and performance supercars.
* **Model Verification & 1-Tap Quick Switch**:
  * Quick-select chips right on the scan result (`[🚗 Maruti Suzuki 800]`, `[Maruti Suzuki Swift]`, `[Hyundai i10]`, `[Tata Nexon]`, etc.) allow immediate model verification or switching.
  * Search dialog to select from over 25+ detailed vehicle profiles.

---

### 3. 🎨 Deep Exterior Paint & Color Analysis Engine
* **OEM Factory Color Name & Paint Codes**: Detects manufacturer-specific paint names and official color codes (e.g., *Maruti Solid Red [R01]*, *Grabber Blue [CI]*, *Guards Red [84A]*, *Isle of Man Green [C4G]*, *Arctic White [WA-139X]*, *Nardo Gray [LY7C]*).
* **Paint Finish Classification**: Differentiates between *High-Gloss Solid Clearcoat*, *Multi-Coat Metallic*, *Pearlescent / Pearl Tri-Coat*, and *Satin Matte / Magno*.
* **Shade & Undertone Breakdown**: Evaluates undertones, saturation levels, and highlight reflections under natural and artificial lighting.
* **Accent & Two-Tone Details**: Identifies contrasting roofs (carbon fiber, gloss black), aero splitters, unpainted composite bumpers, mirror caps, and wheel rim finishes.
* **Interactive Hex Swatch**: Displays a visual color swatch with single-tap clipboard copy for the HEX color code (e.g., `#D81E05`).

---

### 4. 📱 Device Optimization & Xiaomi / HyperOS Compatibility
* **Direct System Gallery Picker (`ActivityResultContracts.GetContent`)**: Uses standard Android storage contracts, resolving issues on custom OEM interfaces (such as Xiaomi MIUI / HyperOS and Redmi Note 13 5G) where Google Play Services PhotoPicker dialogs might fail.
* **High-Resolution Stream Decoding (`ImageUtils`)**: Employs stream-based bounds inspection (`inJustDecodeBounds`) to safely downsample high-megapixel images (e.g., 108 MP captures) and prevent `OutOfMemoryError` (OOM).
* **Automatic EXIF Orientation Correction**: Automatically parses EXIF orientation tags (`ExifInterface`) and rotates images before processing, preventing skewed orientations.
* **Secure `FileProvider` Camera Capture**: Takes full-resolution camera photos stored in a private cache directory, avoiding public storage permission requirements.
* **Modern Display Insets**: Designed for edge-to-edge layouts, punch-hole cameras, and system navigation bars.

---

### 5. 🔑 Gemini Multimodal Vision API & Live Key Validator
* **Multi-Model Fallback Chain**: Supports `gemini-3.8-flash`, `gemini-3.5-flash`, and `gemini-flash-latest`.
* **Live In-App Key Tester**: Real-time validation button in the API Key dialog that pings the Gemini endpoint, giving instant feedback (detects revoked, leaked, or invalid keys).
* **Transparent Status Notices**: Clear warning banners when an API key is blocked or revoked, gracefully falling back to the on-device automotive classifier without crashing or misidentifying vehicles.

---

### 6. 🏎️ Showcase Gallery & Offline Garage
* **Interactive Vehicle Showcase**: Test the scanner instantly with pre-loaded high-performance vehicles (Mustang GT, Porsche 911 Carrera, Corvette C8 Stingray, BMW M4 Competition).
* **Persistent Garage Database**: Saves all scanned vehicles locally using an **Android Room Database**.
* **Formatted Spec Export**: Single-tap Markdown report copying for sharing vehicle specifications across messaging and note applications.

---

## 🛠️ Architecture & Tech Stack

```
AutoLens AI
├── ui/
│   ├── components/      # Reusable Compose widgets (CarResultView, ApiKeyDialog, HexSwatch, Carousel)
│   ├── screens/         # ScannerScreen, GarageHistoryScreen, ShowcaseScreen
│   ├── theme/           # Material Design 3 theme, Typography, Automotive color palette
│   └── CarViewModel.kt  # StateFlow, MVI/MVVM ViewModel
├── data/
│   ├── local/           # Room Database, DAOs, Entities for saved garage vehicles
│   ├── remote/          # Retrofit 2, Moshi, OkHttp 3, Gemini Multimodal REST API
│   ├── ApiKeyManager.kt # SharedPreferences & build config API key resolver
│   └── CarAnalyzerRepository.kt # Image processing, Gemini API invocation, and heuristic fallback
├── model/               # CarIdentificationResult, CarColorDetails, VehicleSpecProfile
└── util/                # ImageUtils (bounds checking, EXIF rotation, base64 encoding, color extraction)
```

### Libraries & Tools
* **Language**: Kotlin 2.0+
* **UI Toolkit**: Jetpack Compose (Material 3)
* **Architecture**: MVVM with Clean Data Layer
* **Reactive State**: Kotlin Coroutines & `StateFlow`
* **Local Storage**: Android Room Database (SQLite) with KSP
* **Networking**: Retrofit 2, OkHttp 3 with logging interceptors
* **Serialization**: Moshi with code generation
* **Image Loading**: Coil Compose
* **Testing**: JUnit 4, Robolectric

---

## 🚀 Getting Started

### Prerequisites
* Android Studio Ladybug (2024.2+) or newer
* Android SDK 35 (compileSdk 35, minSdk 26)
* JDK 17 or JDK 21

### Clone the Repository
```bash
git clone https://github.com/your-username/AutoLensAI.git
cd AutoLensAI
```

### Configure Your Gemini API Key
You can provide your Google Gemini API key in one of two ways:

#### Option A: In the `.env` file (Recommended for development)
1. Create a `.env` file in the project root directory:
```bash
echo "GEMINI_API_KEY=YOUR_GEMINI_API_KEY_HERE" > .env
```
2. Build the project. The Secrets Gradle plugin will inject `BuildConfig.GEMINI_API_KEY`.

#### Option B: In the App UI at Runtime
1. Launch the app on your device or emulator.
2. Tap the **Key icon** in the top navigation bar.
3. Paste your Gemini API key and tap **Test Key Now** to verify it.
4. Tap **Save Key**. The key is stored locally in encrypted app storage.

---

### Building & Running
From the terminal:
```bash
# Build the debug APK
./gradlew assembleDebug

# Run unit and Robolectric tests
./gradlew testDebugUnitTest

# Install on a connected device
./gradlew installDebug
```
Or open the project in **Android Studio** and click **Run** (`Shift + F10`).

---

## 📖 How to Use the App

1. **Snap or Upload a Photo**:
   * Tap **Gallery** to select any vehicle image from your photo album.
   * Tap **Camera** to capture a live photo of a car on the road.
   * Or tap any vehicle in the **Showcase Carousel** for instant demonstration.
2. **Review Systematic Specifications**:
   * Examine the Make, Model, Generation, and Segment.
   * Review the **Powertrain & Mechanical Architecture** table (Engine, Power, Torque, Transmission, Fuel, Mileage).
   * Inspect the **Exterior Color & Paint Specification** card for the OEM paint name, finish type, and hex color swatch.
   * If needed, use the **Quick Switch** chips or the search picker to verify/switch the model (e.g. Maruti Suzuki 800).
3. **Save and Share**:
   * Tap **Copy Spec Sheet** to copy the formatted systematic vehicle profile to your clipboard.
   * Tap **Save to Garage** to store the car in your local vehicle log.
   * Switch to the **Garage** tab to view your saved collection anytime offline.

---

## 🔒 Permissions & Privacy
* **Camera (`android.permission.CAMERA`)**: Used exclusively when capturing vehicle photos within the app.
* **Internet (`android.permission.INTERNET`)**: Used to communicate with the Google Gemini Multimodal Vision API.
* **No Background Telemetry**: AutoLens AI does not track user location, analyze contacts, or store photos on third-party servers outside of the user's configured Gemini API calls.

---

## 📄 License
This project is open-source and licensed under the [MIT License](LICENSE).
