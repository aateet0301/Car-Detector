# AutoLens AI 🏎️ — Automotive Vehicle & Spec Identifier

**AutoLens AI** is a automotive AI assistant for Android powered by Google Gemini Vision. It detects vehicles in photos, identifies their exact make, model, generation, and exterior paint color, and generates a structured technical specification report.

Optimized for modern Android devices, including high-resolution camera systems like the **Redmi Note 13 5G (108 MP)**.

---

## 🌟 Key Features

### 1. 🔍 Instant AI Vehicle Detection
- **Universal Gallery & Camera Guessing**: Automatically analyzes and identifies any car photo uploaded from your gallery or captured via your camera.
- **Robust Model Identification**: Accurately recognizes vehicles across multiple angles (front, rear, side, 3/4 angle), lighting conditions, and body types (sports cars, sedans, SUVs, trucks, EVs, classics).
- **Exact Structured Reports**:
  ```markdown
  **Make & Model:** [e.g., Ford Mustang GT]
  **Estimated Year/Gen:** [e.g., 2018-2023 (6th Generation)]
  **Exterior Color:** [e.g., Grabber Blue]
  **Body Style:** [e.g., 2-Door Fastback Coupe]

  **Key Specifications (Typical for this model):**
  * **Engine/Powertrain:** [e.g., 5.0L V8]
  * **Drivetrain:** [e.g., Rear-Wheel Drive (RWD)]
  * **Horsepower:** [e.g., ~450 hp]
  * **Notable Features:** [e.g., Quad exhaust, aggressive front fascia]

  **Confidence Level:** [High/Medium/Low - detailed rationale]
  ```

### 2. 📱 Redmi Note 13 5G & Universal Gallery Fix
- **Direct System Gallery Picker (`ActivityResultContracts.GetContent`)**: Replaces the buggy Google Play Services PhotoPicker that often failed on Xiaomi MIUI / HyperOS with "Could not process image. Please try again."
- **108 MP Stream-Based Sub-Sampling (`ImageUtils`)**: Prevents `OutOfMemoryError` (OOM) by reading image bounds first and sub-sampling high-resolution images safely during stream decoding.
- **EXIF Auto-Rotation**: Corrects image orientation tags automatically so portrait/vertical photos taken with Xiaomi/MIUI cameras are never processed sideways.
- **`FileProvider` Camera Integration**: Captures full-fidelity camera shots directly into a private app cache URI.
- **20:9 AMOLED Display Adaptation**: Handles punch-hole camera insets and gesture navigation bars cleanly.

### 3. 🎨 Detailed Paint & Color Analysis Engine
- **OEM Factory Paint Name & Codes**: Detects official manufacturer colorways and paint codes (e.g., *Grabber Blue [CI]*, *Guards Red [84A]*, *Isle of Man Green [C4G]*, *Nardo Gray [LY7C]*).
- **Paint Finish Classification**: Identifies specific finish types including *High-Gloss Solid Clearcoat, Multi-Coat Metallic Flake, Pearlescent / Pearl Tri-Coat, and Satin Matte / Magno*.
- **Shade & Undertone Breakdown**: Analyzes color saturation, reflection profiles, and metallic/pearl luster under ambient and direct light.
- **Two-Tone & Accent Styling**: Identifies contrast roofs (e.g., Carbon Fiber / Gloss Black), side splitters, mirror caps, and wheel finishes.
- **Interactive Color Swatch & Hex Code**: Visual color swatch with realistic clearcoat gloss reflection and one-tap HEX code clipboard copy (e.g., `#0077C8`).

### 4. 🔑 Gemini API Key Configuration
- Preconfigured with the latest `gemini-3.8-flash` and `gemini-3.5-flash` multimodal endpoints.
- Configure or update your Google Gemini API key at runtime via the top bar **Set API Key** dialog.
- Persisted securely in device preferences (`SharedPreferences`) with automatic `.env` fallback.

### 5. 🏎️ Real Car Showcase Gallery
- Preloaded photorealistic gallery vehicles ready for one-tap instant testing:
  - **Ford Mustang GT** (*Grabber Blue* · 5.0L Coyote V8)
  - **Porsche 911 Carrera (992)** (*Guards Red* · 3.0L Twin-Turbo Flat-6)
  - **Chevrolet Corvette C8 Stingray** (*Arctic White* · 6.2L LT2 Mid-Engine V8)
  - **BMW M4 Competition** (*Isle of Man Green* · 3.0L S58 Twin-Turbo Inline-6)

### 6. 🏠 Persistent "My Garage" History
- Saves all identified vehicles locally using **Android Room Database**.
- Review past vehicle scans, timestamps, photos, and specs offline.
- Easily copy full Markdown reports to your clipboard with a single tap.

---

## 🚀 How to Use

1. **Launch the App**:
   - Open AutoLens AI on your device or in the streaming emulator.
2. **Select or Snap a Car**:
   - Tap **Gallery** to select any photo of a car stored on your device (supports JPG, PNG, WebP, HEIC).
   - Tap **Camera** to snap a live photo of any vehicle on the street.
   - Or tap any of the preloaded showcase cards (Mustang, Porsche, Corvette, BMW).
3. **Instant AI Identification**:
   - The app will automatically analyze the image using Google Gemini Vision AI.
   - The exact Make, Model, Estimated Year, Exterior Color, Powertrain, and Specifications will be displayed.
4. **Copy & Save**:
   - Tap **Copy Report** to copy the formatted Markdown specification to your clipboard.
   - Tap **Save to Garage** to keep the vehicle in your saved collection.
